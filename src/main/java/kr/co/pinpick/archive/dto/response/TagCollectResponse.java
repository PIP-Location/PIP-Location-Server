package kr.co.pinpick.archive.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.co.pinpick.common.dto.response.CollectResponse;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Getter
public class TagCollectResponse extends CollectResponse<TagResponse> {
    @JsonProperty("tags")
    List<TagResponse> collect;
}
