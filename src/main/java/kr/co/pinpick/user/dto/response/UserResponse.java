package kr.co.pinpick.user.dto.response;

import kr.co.pinpick.archive.dto.response.AttachResponse;
import kr.co.pinpick.user.entity.User;
import kr.co.pinpick.user.entity.UserAttach;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Optional;

@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;

    private String nickname;

    private String email;

    private AttachResponse userAttach;

    private Boolean isFollow;

    public static UserResponse fromEntity(User user) {
        return fromEntity(user, builder());
    }

    public static UserResponse fromEntity(User user, boolean isFollow) {
        return fromEntity(user, isFollow, builder());
    }

    public static <T extends UserResponse> T fromEntity(User user, UserResponseBuilder<T, ?> builder) {
        return builder
                .id(user.getId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .userAttach(user.getUserAttach() != null ? AttachResponse.fromEntity(user.getUserAttach()) : null)
                .build();
    }

    public static <T extends UserResponse> T fromEntity(User user, boolean isFollow, UserResponseBuilder<T, ?> builder) {
        return builder
                .id(user.getId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .userAttach(user.getUserAttach() != null ? AttachResponse.fromEntity(user.getUserAttach()) : null)
                .isFollow(isFollow)
                .build();
    }
}
