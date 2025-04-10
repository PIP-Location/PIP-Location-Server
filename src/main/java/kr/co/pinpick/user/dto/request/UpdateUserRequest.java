package kr.co.pinpick.user.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class UpdateUserRequest {
    @NotNull
    private String nickname;

    private String description;

    @NotNull
    private Boolean isAgreeToTermsOfService;

    @NotNull
    private Boolean isAgreeToPrivacyPolicy;
}
