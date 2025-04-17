package kr.co.pinpick.archive.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.co.pinpick.common.dto.response.CollectResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
@NoArgsConstructor
public class ArchiveCollectResponse extends CollectResponse<ArchiveDetailResponse> {
    @JsonProperty("archives")
    private List<ArchiveDetailResponse> collect;
}
