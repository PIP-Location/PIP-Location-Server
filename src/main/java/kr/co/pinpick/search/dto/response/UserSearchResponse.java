package kr.co.pinpick.search.dto.response;

import kr.co.pinpick.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSearchResponse {
    private String name;
    private long count;

    public static UserSearchResponse fromEntity(User user) {
        return builder()
                .name(user.getNickname())
                .count(user.getArchives().size())
                .build();
    }
}
