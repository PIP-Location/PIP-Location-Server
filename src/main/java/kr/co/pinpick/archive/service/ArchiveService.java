package kr.co.pinpick.archive.service;

import kr.co.pinpick.archive.dto.request.*;
import kr.co.pinpick.archive.dto.response.ArchiveCollectResponse;
import kr.co.pinpick.archive.dto.response.ArchiveDetailResponse;
import kr.co.pinpick.archive.dto.response.ArchiveSearchResponse;
import kr.co.pinpick.archive.entity.Archive;
import kr.co.pinpick.archive.entity.ArchiveAttach;
import kr.co.pinpick.archive.entity.ArchiveTag;
import kr.co.pinpick.archive.repository.ArchiveAttachRepository;
import kr.co.pinpick.archive.repository.ArchiveTagRepository;
import kr.co.pinpick.archive.repository.archiveLike.ArchiveLikeRepository;
import kr.co.pinpick.archive.repository.archive.ArchiveRepository;
import kr.co.pinpick.common.dto.request.OffsetPaginateRequest;
import kr.co.pinpick.common.dto.request.SearchRequest;
import kr.co.pinpick.common.dto.response.PaginateResponse;
import kr.co.pinpick.common.error.BusinessException;
import kr.co.pinpick.common.error.ErrorCode;
import kr.co.pinpick.common.extension.FileExtension;
import kr.co.pinpick.common.storage.IStorageManager;
import kr.co.pinpick.user.dto.response.UserCollectResponse;
import kr.co.pinpick.user.dto.response.UserResponse;
import kr.co.pinpick.user.entity.User;
import kr.co.pinpick.user.repository.FolderArchiveRepository;
import kr.co.pinpick.user.repository.FolderRepository;
import kr.co.pinpick.user.repository.FollowerRepository;
import kr.co.pinpick.user.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.ExtensionMethod;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Service
@RequiredArgsConstructor
@ExtensionMethod(FileExtension.class)
public class ArchiveService {
    private final ArchiveRepository archiveRepository;
    private final ArchiveLikeRepository archiveLikeRepository;
    private final FollowerRepository followerRepository;
    private final GeometryFactory geometryFactory;
    private final FolderRepository folderRepository;
    private final FolderArchiveRepository folderArchiveRepository;
    private final UserRepository userRepository;
    private final IStorageManager storageManager;
    private final ArchiveAttachRepository archiveAttachRepository;
    private final ArchiveTagRepository archiveTagRepository;

    @Transactional
    public ArchiveDetailResponse create(User principal, CreateArchiveRequest request, List<MultipartFile> attaches) throws IOException {
        if (attaches == null) {
            attaches = new ArrayList<>();
        }

        // 아카이브 생성
        var archive = Archive.builder()
                .location(geometryFactory.createPoint(new Coordinate(request.getPositionX(), request.getPositionY())))
                .positionX(request.getPositionX())
                .positionY(request.getPositionY())
                .address(request.getAddress())
                .name(request.getName())
                .content(request.getContent())
                .isPublic(request.getIsPublic())
                .user(principal)
                .build();

        archiveRepository.save(archive);

        // 첨부 생성
        saveArchiveAttaches(archive, attaches);

        // 태그 저장
        saveArchiveTag(archive, request.getTags());

        return ArchiveDetailResponse.fromEntity(archive, false, false);
    }

    private void saveArchiveAttaches(Archive archive, List<MultipartFile> attaches) throws IOException {
        // 첨부 생성
        List<ArchiveAttach> attachEntities = new ArrayList<>();
        for (byte i = 0; i < attaches.size(); i++) {
            MultipartFile o = attaches.get(i);
            var size = o.getImageSize();
            ArchiveAttach build = ArchiveAttach.builder()
                    .name(o.getOriginalFilename())
                    .path(storageManager.upload(o, "archive"))
                    .width(size.getLeft())
                    .height(size.getRight())
                    .sequence(i)
                    .archive(archive)
                    .build();

            attachEntities.add(build);
        }
        archive.setArchiveAttaches(attachEntities);
    }

    private void saveArchiveTag(Archive archive, List<CreateTagRequest> tags) {
        var index = new AtomicInteger();
        List<ArchiveTag> archiveTags = tags
                .stream()
                .map(o -> ArchiveTag.builder()
                        .archive(archive)
                        .name(o.getName())
                        .sequence(index.getAndIncrement())
                        .build())
                .collect(toList());

        archive.setTags(archiveTags);
    }

    @Transactional(readOnly = true)
    public ArchiveCollectResponse retrieve(User principal, ArchiveRetrieveRequest request) {
        var archives = archiveRepository.retrieve(principal, request);

        Set<Long> userIds = new HashSet<>();
        Set<Long> archiveIds = new HashSet<>();
        archives.forEach(a -> {
            userIds.add(a.getUser().getId());
            archiveIds.add(a.getId());
        });

        var isFollowMap = getIsFollowMap(principal, userIds);
        var isLikeMap = getIsLikeMap(principal, archiveIds);

        return ArchiveCollectResponse.builder()
                .collect(archives
                        .stream()
                        .map(a -> ArchiveDetailResponse.fromEntity(a, isFollowMap.containsKey(a.getUser().getId()), isLikeMap.containsKey(a.getId())))
                        .toList())
                .meta(PaginateResponse.builder().count(archives.size()).build())
                .build();
    }

    private Map<Long, Boolean> getIsLikeMap(User principal, Set<Long> archiveIds) {
        var likes = archiveLikeRepository.findByUserAndArchiveIdIn(principal, archiveIds);
        return likes.stream().collect(Collectors.toMap(k -> k.getArchive().getId(), v -> true));
    }

    private Map<Long, Boolean> getIsFollowMap(User principal, Set<Long> userIds) {
        var follows = followerRepository.findByFollowerAndFollowIdInAndIsDeletedFalse(principal, userIds);
        return follows.stream().collect(Collectors.toMap(k -> k.getFollow().getId(), v -> true));
    }

    @Transactional(readOnly = true)
    public ArchiveSearchResponse search(SearchRequest request) {
        var archives = archiveRepository.search(request);
        return ArchiveSearchResponse.builder()
                .collect(archives.stream().map(ArchiveSearchResponse.SearchResponse::fromEntity).toList())
                .meta(PaginateResponse.builder().count(archives.size()).build())
                .build();
    }

    @Transactional(readOnly = true)
    public ArchiveDetailResponse get(User principal, Long archiveId) {
        var archive = archiveRepository.findByIdOrElseThrow(archiveId);
        if (!archive.getIsPublic() && !archive.getUser().getId().equals(principal.getId())) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }
        var isFollow = followerRepository.existsByFollowerAndFollowAndIsDeletedFalse(principal, archive.getUser());
        var isLike = archiveLikeRepository.existsByUserAndArchive(principal, archive);
        return ArchiveDetailResponse.fromEntity(archive, isFollow, isLike);
    }

    @Transactional(readOnly = true)
    public ArchiveCollectResponse getByUser(User principal, Long userId) {
        var user = userRepository.findByIdOrElseThrow(userId);
        var archives = archiveRepository.findAllByUser(principal, user);
        var archiveIds = archives.stream().map(Archive::getId).collect(toSet());
        var isFollow = followerRepository.existsByFollowerAndFollowAndIsDeletedFalse(principal, user);
        var isLikeMap = getIsLikeMap(principal, archiveIds);
        return ArchiveCollectResponse.builder()
                .collect(archives.stream().map(a -> ArchiveDetailResponse.fromEntity(a, isFollow, isLikeMap.containsKey(a.getId()))).toList())
                .meta(PaginateResponse.builder().count(archives.size()).build())
                .build();
    }

    @Transactional(readOnly = true)
    public ArchiveCollectResponse getByFolder(User user, Long folderId) {
        var folder = folderRepository.findByIdOrElseThrow(folderId);
        var folderArchives = folderArchiveRepository.getByFolder(folder);
        var archiveIds = folderArchives.stream().map(fa -> fa.getArchive().getId()).collect(toSet());
        var isLikeMap = getIsLikeMap(user, archiveIds);
        return ArchiveCollectResponse.builder()
                .collect(folderArchives.stream().map(fa -> ArchiveDetailResponse
                        .fromEntity(fa.getArchive(), !user.getId().equals(fa.getArchive().getId()), isLikeMap.containsKey(fa.getArchive().getId()))).toList())
                .meta(PaginateResponse.builder().count(folderArchives.size()).build())
                .build();
    }

    @Transactional
    public ArchiveDetailResponse updateArchive(User principal, Long archiveId, UpdateArchiveRequest request, List<MultipartFile> attaches) throws IOException {
        var archive = archiveRepository.findByIdOrElseThrow(archiveId);
        checkAuthorization(principal, archive);
        archiveAttachRepository.deleteAll(archive.getArchiveAttaches());
        archiveTagRepository.deleteAll(archive.getTags());
        if (attaches == null) {
            attaches = new ArrayList<>();
        }

        // 아카이브 수정
        archive.updateArchive(request);

        // 첨부 생성
        saveArchiveAttaches(archive, attaches);

        // 태그 저장
        saveArchiveTag(archive, request.getTags());

        return ArchiveDetailResponse.fromEntity(archive, false, false);
    }

    @Transactional
    public void delete(User principal, Long archiveId) {
        var archive = archiveRepository.findByIdOrElseThrow(archiveId);
        checkAuthorization(principal, archive);
        archiveRepository.delete(archive);
    }

    @Transactional
    public Boolean changeIsPublic(User principal, Long archiveId, boolean isPublic) {
        var archive = archiveRepository.findByIdOrElseThrow(archiveId);
        checkAuthorization(principal, archive);
        archive.setIsPublic(isPublic);
        return archive.getIsPublic();
    }

    private void checkAuthorization(User principal, Archive archive) {
        if (!archive.getUser().getId().equals(principal.getId())) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }
    }

    @Transactional
    public ArchiveDetailResponse repip(User principal, Long archiveId, RepipArchiveRequest request, List<MultipartFile> attaches) throws IOException {
        if (attaches == null) {
            attaches = new ArrayList<>();
        }

        var archive = archiveRepository.findByIdOrElseThrow(archiveId);

        var isFollow = followerRepository.existsByFollowerAndFollowAndIsDeletedFalse(principal, archive.getUser());
        if (!isFollow) throw new BusinessException(ErrorCode.ONLY_CAN_REPIP_FOLLOWS_ARCHIVE);

        var repipArchive = Archive.builder()
                .location(archive.getLocation())
                .positionX(archive.getPositionX())
                .positionY(archive.getPositionY())
                .address(archive.getAddress())
                .name(archive.getName())
                .user(principal)
                .isPublic(request.isPublic())
                .content(request.getContent())
                .repipArchive(archive)
                .build();

        archiveRepository.save(repipArchive);

        // TODO: Amazon S3 save attaches
        saveArchiveAttaches(repipArchive, attaches);

        // 태그 저장
        saveArchiveTag(repipArchive, request.getTags());

        return ArchiveDetailResponse.fromEntity(repipArchive, false, false);
    }

    @Transactional(readOnly = true)
    public UserCollectResponse getRepip(Long archiveId, OffsetPaginateRequest request) {
        var archive = archiveRepository.findByIdOrElseThrow(archiveId);
        var users = archiveRepository.findRepipByArchive(archive, request);
        return UserCollectResponse.builder()
                .collect(users
                        .stream()
                        .map(UserResponse::fromEntity)
                        .toList())
                .meta(PaginateResponse.builder().count(users.size()).build())
                .build();
    }
}
