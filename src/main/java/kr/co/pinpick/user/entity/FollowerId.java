package kr.co.pinpick.user.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Embeddable
public class FollowerId implements Serializable {
    private Long follower;
    private Long follow;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FollowerId followerId = (FollowerId) o;
        return Objects.equals(follower, followerId.follower) && Objects.equals(follow, followerId.follow);
    }

    @Override
    public int hashCode() {
        return Objects.hash(follower, follow);
    }
}
