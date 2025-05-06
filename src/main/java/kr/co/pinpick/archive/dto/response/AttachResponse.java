package kr.co.pinpick.archive.dto.response;

import kr.co.pinpick.common.entity.AttachEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttachResponse {
    private String name;

    private String path;

    private Integer width;

    private Integer height;

    public static AttachResponse fromEntity(AttachEntity attach) {
        return builder()
                .name(attach.getName())
                .path(attach.getPath())
                .width(attach.getWidth())
                .height(attach.getHeight())
                .build();
    }
}
