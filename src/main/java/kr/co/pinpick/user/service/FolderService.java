package kr.co.pinpick.user.service;

import kr.co.pinpick.archive.entity.Archive;
import kr.co.pinpick.archive.repository.archive.ArchiveRepository;
import kr.co.pinpick.common.dto.PaginateResponse;
import kr.co.pinpick.common.error.BusinessException;
import kr.co.pinpick.common.error.ErrorCode;
import kr.co.pinpick.user.dto.request.CreateFolderRequest;
import kr.co.pinpick.user.dto.response.FolderCollectResponse;
import kr.co.pinpick.user.dto.response.FolderResponse;
import kr.co.pinpick.user.entity.Folder;
import kr.co.pinpick.user.entity.FolderArchive;
import kr.co.pinpick.user.entity.User;
import kr.co.pinpick.user.repository.FolderArchiveRepository;
import kr.co.pinpick.user.repository.FolderRepository;
import kr.co.pinpick.user.repository.FollowerRepository;
import kr.co.pinpick.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FolderService {
    private final UserRepository userRepository;
    private final FolderRepository folderRepository;
    private final FolderArchiveRepository folderArchiveRepository;
    private final FollowerRepository followerRepository;
    private final ArchiveRepository archiveRepository;

    @Transactional
    public FolderResponse create(User user, CreateFolderRequest request, MultipartFile attach) {
        var folder = Folder.builder()
                .user(user)
                .name(request.getName())
                .isPublic(request.isPublic())
                .build();

        // TODO: 라이브러리 타이틀 이미지 저장

        return FolderResponse.fromEntity(folderRepository.save(folder));
    }

    @Transactional(readOnly = true)
    public FolderCollectResponse getFolderList(User user, Long authorId) {
        var author = userRepository.findByIdOrElseThrow(authorId);
        var isAuthor = user.getId().equals(author.getId());
        List<Folder> folders;
        if (isAuthor) {
            folders = folderRepository.findAllByUser(author);
        } else {
            folders = folderRepository.findAllByUserAndIsPublic(author, true);
        }
        return FolderCollectResponse.builder()
                .collect(folders.stream().map(FolderResponse::fromEntity).toList())
                .meta(PaginateResponse.builder().count(folders.size()).build())
                .build();
    }

    @Transactional
    public void addArchiveToFolder(User user, Long folderId, Long archiveId) {
        var folder = folderRepository.findByIdOrElseThrow(folderId);
        var archive = archiveRepository.findByIdOrElseThrow(archiveId);
        var isFollow = followerRepository.existsByFollowerAndFollow(user, archive.getAuthor());
        if (!isFollow && !user.getId().equals(archive.getAuthor().getId())) {
            throw new BusinessException(ErrorCode.ONLY_ADDABLE_FOLLOWERS_ARCHIVE);
        }
        var folderArchiveOptional = folderArchiveRepository.findByFolderAndArchive(folder, archive);
        if (folderArchiveOptional.isPresent()) {
            throw new BusinessException(ErrorCode.FOLDERARCHIVE_ALREADY_ADDED);
        }
        folderArchiveRepository.save(FolderArchive.builder().folder(folder).archive(archive).build());
    }

    @Transactional
    public void removeArchiveFromFolder(Long folderId, Long archiveId) {
        var folder = folderRepository.findByIdOrElseThrow(folderId);
        var archive = archiveRepository.findByIdOrElseThrow(archiveId);
        var folderArchiveOptional = folderArchiveRepository.findByFolderAndArchive(folder, archive);
        if (folderArchiveOptional.isEmpty()) {
            throw new BusinessException(ErrorCode.FOLDERARCHIVE_ALREADY_REMOVED);
        }
        folderArchiveRepository.deleteByFolderAndArchive(folder, archive);
    }

    @Transactional
    public Boolean changeIsPublic(User user, Long folderId, boolean isPublic) {
        var folder = folderRepository.findByIdOrElseThrow(folderId);
        checkAuthorization(user, folder);
        folder.setPublic(isPublic);
        return folder.isPublic();
    }

    @Transactional
    public void delete(User user, Long folderId) {
        var folder = folderRepository.findByIdOrElseThrow(folderId);
        checkAuthorization(user, folder);
        folderRepository.delete(folder);
    }

    private void checkAuthorization(User user, Folder folder) {
        if (!folder.getUser().getId().equals(user.getId())) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
    }
}
