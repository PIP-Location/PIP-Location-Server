package kr.co.pinpick.archive;

import kr.co.pinpick.AcceptanceTest;
import kr.co.pinpick.common.error.ErrorCode;
import kr.co.pinpick.user.UserFixture;
import kr.co.pinpick.user.UserSteps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ArchiveDeleteAcceptanceTest extends AcceptanceTest {
    String token;

    @BeforeEach
    public void setUp() {
        super.setUp();
//        given(storageManager.put(anyString(), any())).willReturn("test-image-path");
        UserSteps.signUp(UserFixture.defaultSignupRequest());
        token = UserSteps.testLogin(UserFixture.defaultLoginRequest()).getAccessToken();
    }

    @Test
    public void deleteArchive() {
        var request = ArchiveFixture.defaultCreateArchiveRequest();
        var archiveResponse = ArchiveSteps.successCreateArchive(token, request);
        ArchiveSteps.successDeleteArchive(token, archiveResponse.getId());
        var error = ArchiveSteps.failRetrieveById(token, archiveResponse.getId());
        assertThat(error.getCode()).isEqualTo(ErrorCode.ENTITY_NOT_FOUND.getCode());
    }

    @Test
    public void deleteArchive_failNotAuthorized() {
        UserSteps.signUp(UserFixture.blockedUserSignupRequest());
        String otherToken = UserSteps.testLogin(UserFixture.blockedUserLoginRequest()).getAccessToken();
        var request = ArchiveFixture.defaultCreateArchiveRequest();
        var archiveResponse = ArchiveSteps.successCreateArchive(token, request);
        var error = ArchiveSteps.failDeleteArchive(otherToken, archiveResponse.getId());
        assertThat(error.getCode()).isEqualTo(ErrorCode.UNAUTHORIZED.getCode());
    }
}
