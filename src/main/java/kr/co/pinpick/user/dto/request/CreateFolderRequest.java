package kr.co.pinpick.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateFolderRequest {
    private String name;

    @Builder.Default
    private boolean isPublic = true;
}
