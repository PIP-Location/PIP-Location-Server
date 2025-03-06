package kr.co.pinpick.user.repository;

import kr.co.pinpick.user.entity.Follower;
import kr.co.pinpick.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface FollowerRepository extends JpaRepository<Follower, Long> {
    List<Follower> findByFollowerAndFollowIdIn(@Param("follower") User follower, @Param("authorIds") Set<Long> authorIds);
}
