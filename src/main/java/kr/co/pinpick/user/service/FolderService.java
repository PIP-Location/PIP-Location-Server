package kr.co.pinpick.user.service;

import kr.co.pinpick.archive.repository.archive.ArchiveRepository;
import kr.co.pinpick.common.dto.response.PaginateResponse;
import kr.co.pinpick.common.error.BusinessException;
import kr.co.pinpick.common.error.ErrorCode;
import kr.co.pinpick.common.storage.IStorageManager;
import kr.co.pinpick.user.dto.request.CreateFolderRequest;
import kr.co.pinpick.user.dto.response.FolderCollectResponse;
import kr.co.pinpick.user.dto.response.FolderResponse;
import kr.co.pinpick.user.entity.Folder;
import kr.co.pinpick.user.entity.FolderArchive;
import kr.co.pinpick.user.entity.User;
import kr.co.pinpick.user.repository.FolderArchiveRepository;
import kr.co.pinpick.user.repository.FolderRepository;
import kr.co.pinpick.user.repository.FollowerRepository;
import kr.co.pinpick.user.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FolderService {
    private final UserRepository userRepository;
    private final FolderRepository folderRepository;
    private final FolderArchiveRepository folderArchiveRepository;
    private final FollowerRepository followerRepository;
    private final ArchiveRepository archiveRepository;
    private final IStorageManager storageManager;

    @Transactional
    public FolderResponse create(User principal, CreateFolderRequest request, MultipartFile attach) throws IOException {
        var folder = Folder.builder()
                .user(principal)
                .name(request.getName())
                .folderImagePath(storageManager.upload(attach, "folder"))
                .isPublic(request.isPublic())
                .build();

        return FolderResponse.fromEntity(folderRepository.save(folder));
    }

    @Transactional(readOnly = true)
    public FolderCollectResponse getFolderList(User principal, Long userId) {
        var user = userRepository.findByIdOrElseThrow(userId);
        var isMe = principal.getId().equals(user.getId());
        List<Folder> folders;
        if (isMe) {
            folders = folderRepository.findAllByUser(user);
        } else {
            folders = folderRepository.findAllByUserAndIsPublic(user, true);
        }
        return FolderCollectResponse.builder()
                .collect(folders.stream().map(FolderResponse::fromEntity).toList())
                .meta(PaginateResponse.builder().count(folders.size()).build())
                .build();
    }

    @Transactional
    public void addArchiveToFolder(User principal, Long folderId, Long archiveId) {
        var folder = folderRepository.findByIdOrElseThrow(folderId);
        var archive = archiveRepository.findByIdOrElseThrow(archiveId);
        var isFollow = followerRepository.existsByFollowerAndFollow(principal, archive.getUser());
        if (!isFollow && !principal.getId().equals(archive.getUser().getId())) {
            throw new BusinessException(ErrorCode.ONLY_ADDABLE_FOLLOWERS_ARCHIVE);
        }
        var folderArchiveOptional = folderArchiveRepository.findByFolderAndArchive(folder, archive);
        if (folderArchiveOptional.isPresent()) {
            throw new BusinessException(ErrorCode.ARCHIVE_ALREADY_ADDED);
        }
        folderArchiveRepository.save(FolderArchive.builder().folder(folder).archive(archive).build());
    }

    @Transactional
    public void removeArchiveFromFolder(Long folderId, Long archiveId) {
        var folder = folderRepository.findByIdOrElseThrow(folderId);
        var archive = archiveRepository.findByIdOrElseThrow(archiveId);
        var folderArchiveOptional = folderArchiveRepository.findByFolderAndArchive(folder, archive);
        if (folderArchiveOptional.isEmpty()) {
            throw new BusinessException(ErrorCode.ARCHIVE_ALREADY_REMOVED);
        }
        folderArchiveRepository.deleteByFolderAndArchive(folder, archive);
    }

    @Transactional
    public Boolean changeIsPublic(User principal, Long folderId, boolean isPublic) {
        var folder = folderRepository.findByIdOrElseThrow(folderId);
        checkAuthorization(principal, folder);
        folder.setPublic(isPublic);
        return folder.isPublic();
    }

    @Transactional
    public void delete(User principal, Long folderId) {
        var folder = folderRepository.findByIdOrElseThrow(folderId);
        checkAuthorization(principal, folder);
        folderRepository.delete(folder);
    }

    private void checkAuthorization(User principal, Folder folder) {
        if (!folder.getUser().getId().equals(principal.getId())) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }
    }
}
