package kr.co.pinpick.archive.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.co.pinpick.common.dto.CollectResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
@NoArgsConstructor
public class ArchiveCollectResponse extends CollectResponse<ArchiveResponse> {
    @JsonProperty("archives")
    private List<ArchiveResponse> collect;
}
