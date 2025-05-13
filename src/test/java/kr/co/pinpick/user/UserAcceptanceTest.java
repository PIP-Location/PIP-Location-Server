package kr.co.pinpick.user;

import kr.co.pinpick.AcceptanceTest;
import kr.co.pinpick.util.MockMultipartFileFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserAcceptanceTest extends AcceptanceTest {
    String token;

    Long userId;

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
        userId = UserSteps.signUp(UserFixture.defaultSignupRequest());
        token = UserSteps.testLogin(UserFixture.defaultLoginRequest()).getAccessToken();
    }

    @Test
    public void updateUser() throws Exception {
        var updateUserRequest = UserFixture.updateUserRequest();
        var user = UserSteps.update(
                token,
                updateUserRequest,
                MockMultipartFileFixture.mockImageFile(100, 101, "jpeg")
        );

        assertThat(user.getNickname()).isEqualTo("keeeeeey");
        assertThat(user.getDescription()).isEqualTo("desc");
    }
}
