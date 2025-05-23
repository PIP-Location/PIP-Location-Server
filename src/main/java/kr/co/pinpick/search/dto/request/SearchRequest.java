package kr.co.pinpick.search.dto.request;

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
public class SearchRequest {
    @Parameter(description = "검색어")
    private String q;

    @Parameter(description = "조회 수")
    @Schema(defaultValue = "100")
    private Long limit = 100L;
}
