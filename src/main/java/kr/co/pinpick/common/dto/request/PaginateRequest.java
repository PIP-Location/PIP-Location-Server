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
public class PaginateRequest {
    @Parameter(description = "마지막 아이디")
    private Long lastId;

    @Parameter(description = "조회 수")
    @Schema(defaultValue = "100")
    private Long limit = 100L;
}
