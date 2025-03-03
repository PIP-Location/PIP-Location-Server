package kr.co.pinpick.archive.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kr.co.pinpick.common.rule.UniqueElementsBy;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class CreateArchiveRequest {
    @NotNull
    private double positionX;

    @NotNull
    private double positionY;

    @Size(max = 200)
    @NotNull
    private String address;

    @Size(max = 200)
    @NotNull
    private String name;

    @Size(max = 2000)
    @NotNull
    private String content;

    @Builder.Default
    private boolean isPublic = true;

    @Valid
    @Size(max = 10)
    @UniqueElementsBy
    @Builder.Default
    private List<CreateTagRequest> tags = new ArrayList<>();
}
