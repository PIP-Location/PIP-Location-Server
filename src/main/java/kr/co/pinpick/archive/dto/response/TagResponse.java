package kr.co.pinpick.archive.dto.response;

import kr.co.pinpick.archive.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TagResponse {
    private Long id;
    private String name;
    private int count;

    public static TagResponse fromEntity(Tag tag) {
        return builder()
                .id(tag.getId())
                .name(tag.getName())
                .count(tag.getCount())
                .build();
    }
}
