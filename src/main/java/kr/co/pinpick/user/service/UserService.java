package kr.co.pinpick.user.service;

import kr.co.pinpick.archive.repository.archive.ArchiveRepository;
import kr.co.pinpick.archive.repository.archiveComment.ArchiveCommentRepository;
import kr.co.pinpick.archive.repository.archiveLike.ArchiveLikeRepository;
import kr.co.pinpick.common.dto.request.SearchRequest;
import kr.co.pinpick.common.dto.response.PaginateResponse;
import kr.co.pinpick.common.extension.FileExtension;
import kr.co.pinpick.common.storage.IStorageManager;
import kr.co.pinpick.user.dto.request.UpdateUserRequest;
import kr.co.pinpick.user.dto.response.UserDetailResponse;
import kr.co.pinpick.user.dto.response.UserSearchResponse;
import kr.co.pinpick.user.entity.User;
import kr.co.pinpick.user.entity.UserAttach;
import kr.co.pinpick.user.repository.BlockRepository;
import kr.co.pinpick.user.repository.FollowerRepository;
import kr.co.pinpick.user.repository.UserAttachRepository;
import kr.co.pinpick.user.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
@ExtensionMethod(FileExtension.class)
public class UserService {
    private final ArchiveRepository archiveRepository;
    private final ArchiveCommentRepository archiveCommentRepository;
    private final UserRepository userRepository;
    private final FollowerRepository followerRepository;
    private final IStorageManager storageManager;
    private final UserAttachRepository userAttachRepository;
    private final ArchiveLikeRepository archiveLikeRepository;
    private final BlockRepository blockRepository;

    @Transactional(readOnly = true)

    public UserSearchResponse search(User user, SearchRequest request) {
        var users = userRepository.search(user, request);
        return UserSearchResponse.builder()
                .collect(users.stream().map(UserSearchResponse.SearchResponse::fromEntity).toList())
                .meta(PaginateResponse.builder().count(users.size()).build())
                .build();
    }

    @Transactional(readOnly = true)
    public UserDetailResponse find(User principal, Long userId) {
        var target = userRepository.findByIdOrElseThrow(userId);
        var isFollow = followerRepository.existsByFollowerAndFollowAndIsDeletedFalse(principal, target);
        return UserDetailResponse.fromEntity(target, isFollow);
    }

    @Transactional
    public UserDetailResponse update(User user, UpdateUserRequest request, MultipartFile attach) throws IOException {
        user = userRepository.findByIdOrElseThrow(user.getId());
        user.updateUserInfo(request);

        if (attach != null) {
            UserAttach oldAttach = user.getUserAttach();
            if (oldAttach != null) {
                user.setUserAttach(null);
                userAttachRepository.delete(oldAttach);
                userAttachRepository.flush();
            }

            var userAttach = UserAttach.builder()
                    .name(attach.getOriginalFilename())
                    .path(storageManager.upload(attach, "user"))
                    .width(attach.getImageSize().getLeft())
                    .height(attach.getImageSize().getRight())
                    .sequence((byte) 0)
                    .user(user)
                    .build();

            user.setUserAttach(userAttach);
        }

        return UserDetailResponse.fromEntity(user, false);
    }

    @Transactional
    public void signOut(User principal) {
        principal = userRepository.findByIdOrElseThrow(principal.getId());
        principal.setIsDeleted(true);
        archiveRepository.bulkUpdateIsDeleted(principal);
        archiveCommentRepository.bulkUpdateIsDeleted(principal);
        followerRepository.bulkUpdateIsDeleted(principal);
        archiveLikeRepository.bulkUpdateIsDeleted(principal);
        blockRepository.bulkUpdateIsDeleted(principal);
    }
}
