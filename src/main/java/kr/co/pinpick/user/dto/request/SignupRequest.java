package kr.co.pinpick.user.dto.request;
import kr.co.pinpick.common.rule.Regex;
import kr.co.pinpick.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    @Regex(pattern = "^[A-z0-9]+\\@[A-z0-9]+\\.[A-z]+$")
    private String email;
    private String password;

    public User toEntity() {
        return User.builder()
                .email(email)
                .password(password)
                .build();
    }
}
