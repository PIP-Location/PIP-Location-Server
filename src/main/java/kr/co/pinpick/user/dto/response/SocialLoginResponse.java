package kr.co.pinpick.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocialLoginResponse {
    private String accessToken;

    private String nickname;

    @Builder.Default
    private Boolean isAgreeToTermsOfService = false;

    @Builder.Default
    private Boolean isAgreeToPrivacyPolicy = false;

    private HttpStatus status;

    private int code;
}
