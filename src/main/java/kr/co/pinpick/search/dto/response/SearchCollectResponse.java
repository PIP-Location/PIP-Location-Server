package kr.co.pinpick.search.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.co.pinpick.common.dto.response.CollectResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SearchCollectResponse<T> extends CollectResponse<T> {
    @JsonProperty("results")
    private List<T> collect;
}
