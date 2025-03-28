package kr.co.pinpick.user.dto.request;

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
public class UserRetrieveRequest {
    @Parameter(description = "검색어")
    private String q;

    @Parameter(description = "페이지 수")
    @Schema(defaultValue = "0")
    private int pageNo = 0;
}
