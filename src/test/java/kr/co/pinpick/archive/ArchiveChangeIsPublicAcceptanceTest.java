package kr.co.pinpick.archive;

import kr.co.pinpick.AcceptanceTest;
import kr.co.pinpick.common.error.ErrorCode;
import kr.co.pinpick.user.UserFixture;
import kr.co.pinpick.user.UserSteps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ArchiveChangeIsPublicAcceptanceTest extends AcceptanceTest {
    String token;

    @BeforeEach
    public void setUp() {
        super.setUp();
//        given(storageManager.put(anyString(), any())).willReturn("test-image-path");
        UserSteps.signUp(UserFixture.defaultSignupRequest());
        token = UserSteps.testLogin(UserFixture.defaultLoginRequest()).getAccessToken();
    }

    @Test
    public void changeIsPublic_success() {
        var request = ArchiveFixture.defaultCreateArchiveRequest();
        var archiveResponse = ArchiveSteps.successCreateArchive(token, request);
        ArchiveSteps.successChangeIsPublic(token, archiveResponse.getId(), false);
        var response = ArchiveSteps.successRetrieveById(token, archiveResponse.getId());
        assertThat(response.getIsPublic()).isFalse();

        ArchiveSteps.successChangeIsPublic(token, archiveResponse.getId(), true);
        response = ArchiveSteps.successRetrieveById(token, archiveResponse.getId());
        assertThat(response.getIsPublic()).isTrue();
    }

    @Test
    public void changeIsPublic_failNotAuthorized() {
        UserSteps.signUp(UserFixture.blockedUserSignupRequest());
        String otherToken = UserSteps.testLogin(UserFixture.blockedUserLoginRequest()).getAccessToken();
        var request = ArchiveFixture.defaultCreateArchiveRequest();
        var archiveResponse = ArchiveSteps.successCreateArchive(token, request);
        var error = ArchiveSteps.failChangeIsPublic(otherToken, archiveResponse.getId(), false);
        assertThat(error.getCode()).isEqualTo(ErrorCode.ACCESS_DENIED.getCode());
    }
}