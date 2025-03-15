package kr.co.pinpick.user.service;

import kr.co.pinpick.common.service.IUserLinkService;
import kr.co.pinpick.user.entity.User;
import kr.co.pinpick.user.repository.FollowerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserFollowService implements IUserLinkService<User> {
    private final FollowerRepository followerRepository;

    @Override
    @Transactional
    public void link(User source, User target) {
        followerRepository.saveWithNativeQuery(source.getId(), target.getId());
    }

    @Override
    @Transactional
    public int unlink(User source, User target) {
        return followerRepository.deleteByFollowerAndFollow(source, target);
    }
}

