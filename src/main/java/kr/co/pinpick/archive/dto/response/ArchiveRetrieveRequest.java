package kr.co.pinpick.archive.dto.response;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springdoc.core.annotations.ParameterObject;

@Getter
@Setter
@NoArgsConstructor
@ParameterObject
@AllArgsConstructor
public class ArchiveRetrieveRequest {
    Double topLeftLatitude;

    Double topLeftLongitude;

    Double bottomRightLatitude;

    Double bottomRightLongitude;

    @Parameter(description = "입력안함: 전체조회, true: 팔로우한 사용자가 작성한것 조회, false: 팔로우 안한 사용자가 작성한것 조회")
    private Boolean follow = null;

    private String tag = null;

    private Long archiveId;
}
