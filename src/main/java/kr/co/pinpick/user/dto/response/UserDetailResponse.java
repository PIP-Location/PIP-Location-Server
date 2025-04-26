package kr.co.pinpick.user.dto.response;

import kr.co.pinpick.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailResponse extends UserResponse {
    private String description;

    private int followerCount;

    private int archiveCount;

    public static UserDetailResponse fromEntity(User user, boolean isFollow) {
        return fromEntity(
                user,
                builder()
                    .description(user.getDescription())
                    .isFollow(isFollow)
                    .followerCount(user.getFollowers().size())
                    .archiveCount(user.getArchives().size())
        );
    }
}
