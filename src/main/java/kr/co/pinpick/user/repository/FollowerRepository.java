package kr.co.pinpick.user.repository;

import kr.co.pinpick.user.entity.Follower;
import kr.co.pinpick.user.entity.FollowerId;
import kr.co.pinpick.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface FollowerRepository extends JpaRepository<Follower, FollowerId> {
    List<Follower> findByFollowerAndFollowIdInAndIsDeletedFalse(User principal, Set<Long> userIds);

    boolean existsByFollowerAndFollowAndIsDeletedFalse(User principal, User user);

    int deleteByFollowerAndFollow(User source, User target);

    @Query(value = "insert into followers(follower_id, follow_id, is_deleted) values(:followerId, :followId, false)", nativeQuery = true)
    @Modifying
    void saveWithNativeQuery(@Param("followerId") Long followerId, @Param("followId") Long followId);

    @Modifying(clearAutomatically = true)
    @Query("update Follower f set f.isDeleted = true where f.follow = :principal or f.follower = :principal")
    void bulkUpdateIsDeleted(User principal);
}
