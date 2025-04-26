package kr.co.pinpick.common.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springdoc.core.annotations.ParameterObject;

@Getter
@Setter
@NoArgsConstructor
@ParameterObject
public class OffsetPaginateRequest {
    @Parameter(description = "페이지 번호")
    private int page;

    @Parameter(description = "조회 수")
    @Schema(defaultValue = "20")
    private Long limit = 20L;
}
