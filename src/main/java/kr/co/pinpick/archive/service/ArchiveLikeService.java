package kr.co.pinpick.archive.service;

import kr.co.pinpick.archive.entity.Archive;
import kr.co.pinpick.archive.repository.ArchiveLikeRepository;
import kr.co.pinpick.common.service.IUserLinkService;
import kr.co.pinpick.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArchiveLikeService implements IUserLinkService<Archive> {

    private final ArchiveLikeRepository archiveLikeRepository;

    @Override
    @Transactional
    public void link(User source, Archive target) {
        archiveLikeRepository.saveWithNativeQuery(source.getId(), target.getId());
    }

    @Override
    @Transactional
    public int unlink(User source, Archive target) {
        return archiveLikeRepository.deleteByAuthorAndArchive(source, target);
    }
}
