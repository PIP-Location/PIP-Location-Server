package kr.co.pinpick.archive.dto.response;

import kr.co.pinpick.archive.entity.ArchiveTag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ArchiveTagResponse {
    private String name;

    public static ArchiveTagResponse fromEntity(ArchiveTag tag) {
        return builder()
                .name(tag.getName())
                .build();
    }
}
