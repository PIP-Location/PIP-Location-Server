package kr.co.pinpick.archive.service;

import kr.co.pinpick.archive.dto.response.ArchiveCollectResponse;
import kr.co.pinpick.archive.dto.response.ArchiveResponse;
import kr.co.pinpick.archive.dto.response.ArchiveRetrieveRequest;
import kr.co.pinpick.archive.dto.request.CreateArchiveRequest;
import kr.co.pinpick.archive.entity.Archive;
import kr.co.pinpick.archive.entity.ArchiveTag;
import kr.co.pinpick.archive.repository.ArchiveLikeRepository;
import kr.co.pinpick.archive.repository.archive.ArchiveRepository;
import kr.co.pinpick.archive.repository.ArchiveTagRepository;
import kr.co.pinpick.common.dto.PaginateResponse;
import kr.co.pinpick.user.entity.User;
import kr.co.pinpick.user.repository.FollowerRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class ArchiveService {

    private final ArchiveRepository archiveRepository;

    private final ArchiveTagRepository archiveTagRepository;

    private final ArchiveLikeRepository archiveLikeRepository;

    private final FollowerRepository followerRepository;

    private final GeometryFactory geometryFactory;

    @Transactional
    public ArchiveResponse create(User author, CreateArchiveRequest request, List<MultipartFile> attaches) {
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
                .isPublic(request.isPublic())
                .author(author)
                .build();

        archiveRepository.save(archive);

        // TODO: Amazon S3 save attaches
        archive.setArchiveAttaches(new ArrayList<>());

        // 태그 저장
        var index = new AtomicInteger();
        List<ArchiveTag> archiveTags = request.getTags()
                .stream()
                .map(o -> ArchiveTag.builder()
                        .archive(archive)
                        .name(o.getName())
                        .sequence(index.getAndIncrement())
                        .build())
                .collect(toList());

        archiveTagRepository.saveAll(archiveTags);
        archive.setTags(archiveTags);

        return ArchiveResponse.fromEntity(archive, false, false);
    }

    @Transactional(readOnly = true)
    public ArchiveCollectResponse retrieve(User user, ArchiveRetrieveRequest request) {
        var archives = archiveRepository.retrieve(user, request);

        Set<Long> authorIds = new HashSet<>();
        Set<Long> archiveIds = new HashSet<>();
        archives.forEach(a -> {
            authorIds.add(a.getAuthor().getId());
            archiveIds.add(a.getId());
        });

        Map<Long, Boolean> isFollowMap = getIsFollowMap(user, authorIds);
        Map<Long, Boolean> isLikeMap = getIsLikeMap(user, archiveIds);

        return ArchiveCollectResponse.builder()
                .collect(archives
                        .stream()
                        .map(a -> ArchiveResponse.fromEntity(a, isFollowMap.containsKey(a.getAuthor().getId()), isLikeMap.containsKey(a.getId())))
                        .toList())
                .meta(PaginateResponse.builder().count(archives.size()).build())
                .build();
    }

    private Map<Long, Boolean> getIsLikeMap(User author, Set<Long> archiveIds) {
        var likes = archiveLikeRepository.findByAuthorAndArchiveIdIn(author, archiveIds);
        return likes.stream().collect(Collectors.toMap(k -> k.getArchive().getId(), v -> true));
    }

    private Map<Long, Boolean> getIsFollowMap(User author, Set<Long> authorIds) {
        var follows = followerRepository.findByFollowerAndFollowIdIn(author, authorIds);
        return follows.stream().collect(Collectors.toMap(k -> k.getFollow().getId(), v -> true));
    }

    @Transactional
    public ArchiveResponse get(User user, Archive archive) {
        var isFollow = followerRepository.existsByFollowerAndFollow(user, archive.getAuthor());
        var isLike = archiveLikeRepository.existsByAuthorAndArchive(user, archive);
        return ArchiveResponse.fromEntity(archive, isFollow, isLike);
    }

    @Transactional
    public void delete(Archive archive) {
        archiveRepository.delete(archive);
    }

    @Transactional
    public Boolean changeIsPublic(Archive archive, boolean isPublic) {
        archive.setIsPublic(isPublic);
        return archive.getIsPublic();
    }
}
