package kr.co.pinpick.archive.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.co.pinpick.archive.entity.Archive;
import kr.co.pinpick.common.dto.response.CollectResponse;
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
public class ArchiveSearchResponse extends CollectResponse<ArchiveSearchResponse.SearchResponse> {
    @JsonProperty("archives")
    private List<ArchiveSearchResponse.SearchResponse> collect;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SearchResponse {
        private Long id;
        private String name;
        private String address;

        public static SearchResponse fromEntity(Archive archive) {
            return builder()
                    .id(archive.getId())
                    .name(archive.getName())
                    .address(archive.getAddress())
                    .build();
        }
    }
}
