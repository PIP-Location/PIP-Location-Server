package kr.co.pinpick.archive.service;

import kr.co.pinpick.archive.repository.ArchiveLikeRepository;
import kr.co.pinpick.archive.repository.archive.ArchiveRepository;
import kr.co.pinpick.common.dto.PaginateResponse;
import kr.co.pinpick.common.service.IUserLinkService;
import kr.co.pinpick.user.dto.response.UserCollectResponse;
import kr.co.pinpick.user.dto.response.UserResponse;
import kr.co.pinpick.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArchiveLikeService implements IUserLinkService {
    private final ArchiveRepository archiveRepository;
    private final ArchiveLikeRepository archiveLikeRepository;

    @Transactional(readOnly = true)
    public UserCollectResponse get(User user, Long archiveId) {
        var archive = archiveRepository.findByIdOrElseThrow(archiveId);
        var archiveLikes = archiveLikeRepository.findByAuthorAndArchive(user, archive);
        return UserCollectResponse.builder()
                .collect(archiveLikes
                        .stream()
                        .map(archiveLike -> UserResponse.fromEntity(archiveLike.getAuthor()))
                        .toList())
                .meta(PaginateResponse.builder().count(archiveLikes.size()).build())
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
        return archiveLikeRepository.deleteByAuthorAndArchive(source, target);
    }
}
