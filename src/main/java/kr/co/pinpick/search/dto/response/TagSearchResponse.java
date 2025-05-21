package kr.co.pinpick.search.dto.response;

import kr.co.pinpick.archive.entity.Archive;
import kr.co.pinpick.archive.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagSearchResponse {
    private String name;
    private long count;

    public static TagSearchResponse fromEntity(Tag tag) {
        return builder()
                .name(tag.getName())
                .count(tag.getCount())
                .build();
    }
}
