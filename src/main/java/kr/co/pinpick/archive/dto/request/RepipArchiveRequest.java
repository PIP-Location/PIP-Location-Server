package kr.co.pinpick.archive.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kr.co.pinpick.common.rule.UniqueElementsBy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RepipArchiveRequest {
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
