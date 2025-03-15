package kr.co.pinpick.user.service;

import kr.co.pinpick.common.service.IUserLinkService;
import kr.co.pinpick.user.entity.User;
import kr.co.pinpick.user.repository.BlockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserBlockService implements IUserLinkService<User> {
    private final BlockRepository blockRepository;

    @Override
    @Transactional
    public void link(User source, User target) {
        blockRepository.saveWithNativeQuery(source.getId(), target.getId());
    }

    @Override
    @Transactional
    public int unlink(User source, User target) {
        return blockRepository.deleteByAuthorAndBlock(source, target);
    }
}
