package kr.co.pinpick.search.dto.response;

import kr.co.pinpick.archive.entity.Archive;
import kr.co.pinpick.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArchiveSearchResponse {
    private String name;
    private long count;

    public static ArchiveSearchResponse from(String name, long count) {
        return builder()
                .name(name)
                .count(count)
                .build();
    }
}
