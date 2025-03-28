package kr.co.pinpick.user.service;

import kr.co.pinpick.common.service.IUserLinkService;
import kr.co.pinpick.user.entity.User;
import kr.co.pinpick.user.repository.BlockRepository;
import kr.co.pinpick.user.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserBlockService implements IUserLinkService {
    private final UserRepository userRepository;
    private final BlockRepository blockRepository;

    @Override
    @Transactional
    public void link(User source, Long targetId) {
        blockRepository.saveWithNativeQuery(source.getId(), targetId);
    }

    @Override
    @Transactional
    public int unlink(User source, Long targetId) {
        var target = userRepository.findByIdOrElseThrow(targetId);
        return blockRepository.deleteByAuthorAndBlock(source, target);
    }
}
