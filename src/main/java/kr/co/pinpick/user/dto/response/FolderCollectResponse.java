package kr.co.pinpick.user.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.co.pinpick.common.dto.CollectResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class FolderCollectResponse extends CollectResponse<FolderResponse> {
    @JsonProperty("folders")
    private List<FolderResponse> collect;
}
