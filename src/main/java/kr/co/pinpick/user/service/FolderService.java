package kr.co.pinpick.user.service;

import kr.co.pinpick.archive.entity.Archive;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FolderService {
    private final FolderRepository folderRepository;
    private final FolderArchiveRepository folderArchiveRepository;
    private final FollowerRepository followerRepository;

    @Transactional
    public FolderResponse createFolder(User user, CreateFolderRequest request) {
        var folder = Folder.builder()
                .user(user)
                .name(request.getName())
                .isPublic(request.isPublic())
                .build();

        return FolderResponse.fromEntity(folderRepository.save(folder));
    }

    @Transactional(readOnly = true)
    public FolderCollectResponse getFolderList(User user, User author) {
        var isAuthor = user.getId().equals(author.getId());
        List<Folder> folders;
        if (isAuthor) {
            folders = folderRepository.findAllByUser(author);
        } else {
            folders = folderRepository.findAllByUserAndIsPublic(author, true);
        }
        return FolderCollectResponse.builder()
                .folders(folders.stream().map(FolderResponse::fromEntity).toList())
                .build();
    }

    @Transactional
    public void addArchiveToFolder(User user, Folder folder, Archive archive) {
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
    public void removeArchiveFromFolder(Folder folder, Archive archive) {
        var folderArchiveOptional = folderArchiveRepository.findByFolderAndArchive(folder, archive);
        if (folderArchiveOptional.isEmpty()) {
            throw new BusinessException(ErrorCode.FOLDERARCHIVE_ALREADY_REMOVED);
        }
        folderArchiveRepository.deleteByFolderAndArchive(folder, archive);
    }

    @Transactional
    public Boolean changeIsPublic(Folder folder, boolean isPublic) {
        folder.setPublic(isPublic);
        return folder.isPublic();
    }

    @Transactional
    public void deleteFolder(Folder folder) {
        folderRepository.delete(folder);
    }
}
