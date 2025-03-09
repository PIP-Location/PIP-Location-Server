package kr.co.pinpick.archive.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.co.pinpick.common.dto.CollectResponse;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Getter
public class CommentCollectResponse extends CollectResponse<CommentResponse> {
    @JsonProperty("comments")
    List<CommentResponse> collect;
}