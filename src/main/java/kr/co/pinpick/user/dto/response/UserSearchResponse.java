package kr.co.pinpick.user.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.co.pinpick.common.dto.response.CollectResponse;
import kr.co.pinpick.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserSearchResponse extends CollectResponse<UserSearchResponse.SearchResponse> {
    @JsonProperty("users")
    private List<SearchResponse> collect;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SearchResponse {
        private Long id;
        private String nickname;
        private int archiveCount;

        public static SearchResponse fromEntity(User user) {
            return builder()
                    .id(user.getId())
                    .nickname(user.getNickname())
                    .archiveCount(user.getArchives().size())
                    .build();
        }
    }
}
