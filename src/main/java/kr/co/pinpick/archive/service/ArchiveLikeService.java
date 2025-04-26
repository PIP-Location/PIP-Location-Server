package kr.co.pinpick.archive.service;

import kr.co.pinpick.archive.repository.archiveLike.ArchiveLikeRepository;
import kr.co.pinpick.archive.repository.archive.ArchiveRepository;
import kr.co.pinpick.common.dto.request.OffsetPaginateRequest;
import kr.co.pinpick.common.dto.response.PaginateResponse;
import kr.co.pinpick.common.service.IUserLinkService;
import kr.co.pinpick.user.dto.response.UserCollectResponse;
import kr.co.pinpick.user.dto.response.UserResponse;
import kr.co.pinpick.user.entity.User;
import kr.co.pinpick.user.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArchiveLikeService implements IUserLinkService {
    private final ArchiveRepository archiveRepository;
    private final ArchiveLikeRepository archiveLikeRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserCollectResponse getLike(Long archiveId, OffsetPaginateRequest request) {
        var archive = archiveRepository.findByIdOrElseThrow(archiveId);
        var users = archiveLikeRepository.findLikeByArchive(archive, request);
        return UserCollectResponse.builder()
                .collect(users
                        .stream()
                        .map(UserResponse::fromEntity)
                        .toList())
                .meta(PaginateResponse.builder().count(users.size()).build())
                .build();
    }

    @Override
    @Transactional
    public void link(User source, Long targetId) {
        archiveLikeRepository.saveWithNativeQuery(source.getId(), targetId);
    }

    @Override
    @Transactional
    public int unlink(User source, Long targetId) {
        var target = archiveRepository.findByIdOrElseThrow(targetId);
        return archiveLikeRepository.deleteByUserAndArchive(source, target);
    }
}
