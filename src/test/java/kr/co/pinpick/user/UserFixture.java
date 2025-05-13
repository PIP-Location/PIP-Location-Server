package kr.co.pinpick.user;

import kr.co.pinpick.user.dto.request.LoginRequest;
import kr.co.pinpick.user.dto.request.SignupRequest;
import kr.co.pinpick.user.dto.request.UpdateUserRequest;

public class UserFixture {
    public static final String DEFAULT_EMAIL = "vegielcl@gmail.com";
    public static final String DEFAULT_PASSWORD = "password";
    public static final String BLOCKED_EMAIL = "keeeeeey@gmail.com";

    public static SignupRequest defaultSignupRequest() {
        return new SignupRequest(DEFAULT_EMAIL, DEFAULT_PASSWORD);
    }

    public static LoginRequest defaultLoginRequest() {
        return new LoginRequest(DEFAULT_EMAIL, DEFAULT_PASSWORD);
    }

    public static SignupRequest blockedUserSignupRequest() {
        return new SignupRequest(BLOCKED_EMAIL, DEFAULT_PASSWORD);
    }

    public static LoginRequest blockedUserLoginRequest() {
        return new LoginRequest(BLOCKED_EMAIL, DEFAULT_PASSWORD);
    }

    public static UpdateUserRequest updateUserRequest() {
        return UpdateUserRequest.builder()
                .nickname("keeeeeey")
                .description("desc")
                .isAgreeToPrivacyPolicy(true)
                .isAgreeToTermsOfService(true)
                .build();
    }
}
