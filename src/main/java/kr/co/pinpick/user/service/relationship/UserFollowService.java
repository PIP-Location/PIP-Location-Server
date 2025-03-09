package kr.co.pinpick.user.service.relationship;

import kr.co.pinpick.user.entity.Follower;
import kr.co.pinpick.user.entity.User;
import kr.co.pinpick.user.repository.FollowerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserFollowService implements IUserRelationshipService {

    private final FollowerRepository followerRepository;

    @Override
    @Transactional
    public void link(User source, User target) {
        var block = Follower.builder()
                .follower(source)
                .follow(target)
                .build();
        followerRepository.save(block);
    }

    @Override
    @Transactional
    public int unlink(User source, User target) {
        return followerRepository.deleteByFollowerAndFollow(source, target);
    }
}

