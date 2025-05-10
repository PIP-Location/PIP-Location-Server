package kr.co.pinpick.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateFolderRequest {
    private String name;

    @Builder.Default
    private Boolean isPublic = true;
}
