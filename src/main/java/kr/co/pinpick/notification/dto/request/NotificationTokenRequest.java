package kr.co.pinpick.notification.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.pinpick.notification.entity.enumerated.DeviceType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(title = "디바이스 등록 요청 바디")
public class NotificationTokenRequest {
    private String deviceToken;

    @Schema(description = "디바이스 타입", example = "IOS, ANDROID", required = true)
    private DeviceType deviceType;
}
