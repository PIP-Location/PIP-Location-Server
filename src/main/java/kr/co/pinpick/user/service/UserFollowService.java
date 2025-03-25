package kr.co.pinpick.user.service;

import kr.co.pinpick.common.service.IUserLinkService;
import kr.co.pinpick.user.entity.User;
import kr.co.pinpick.user.repository.FollowerRepository;
import kr.co.pinpick.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserFollowService implements IUserLinkService {
    private final UserRepository userRepository;
    private final FollowerRepository followerRepository;

    @Override
    @Transactional
    public void link(User source, Long targetId) {
        followerRepository.saveWithNativeQuery(source.getId(), targetId);
    }

    @Override
    @Transactional
    public int unlink(User source, Long targetId) {
        var target = userRepository.findByIdOrElseThrow(targetId);
        return followerRepository.deleteByFollowerAndFollow(source, target);
    }
}

