package kr.co.pinpick.archive;

import kr.co.pinpick.AcceptanceTest;
import kr.co.pinpick.archive.dto.response.ArchiveResponse;
import kr.co.pinpick.user.UserFixture;
import kr.co.pinpick.user.UserSteps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ArchiveRetrieveAcceptanceTest extends AcceptanceTest {
    String token;
    String blockedUserToken;
    Long blockedUserId;

    @BeforeEach
    public void setUp() {
        super.setUp();

        UserSteps.signUp(UserFixture.defaultSignupRequest());
        token = UserSteps.testLogin(UserFixture.defaultLoginRequest()).getAccessToken();

        blockedUserId = UserSteps.signUp(UserFixture.blockedUserSignupRequest());
        blockedUserToken = UserSteps.testLogin(UserFixture.blockedUserLoginRequest()).getAccessToken();
    }

    @Test
    public void success() throws IOException {
        // given
        ArchiveSteps.successCreateArchive(token, ArchiveFixture.defaultCreateArchiveRequest());
        ArchiveResponse songpa = ArchiveSteps.successCreateArchive(token, ArchiveFixture.inSongpaCreateArchiveRequest());
        ArchiveResponse youngdeungpo = ArchiveSteps.successCreateArchive(token, ArchiveFixture.inYoungdeungpoCreateArchiveRequest());

        // when
        List<ArchiveResponse> archives = ArchiveSteps.retrieve(token, ArchiveFixture.defaultRetrieveRequest()).getCollect();

        // then
        assertThat(archives.size()).isEqualTo(2);
        assertThat(archives).noneMatch(archive -> archive.getId().equals(youngdeungpo.getId()));
        assertThat(archives).anyMatch(archive -> archive.getId().equals(songpa.getId()));
    }

    @Test
    public void successWithBlock() throws IOException {
        // given
        ArchiveSteps.successCreateArchive(token, ArchiveFixture.defaultCreateArchiveRequest());
        ArchiveResponse blockedArchive = ArchiveSteps.successCreateArchive(blockedUserToken, ArchiveFixture.inSongpaCreateArchiveRequest());

        // when
        UserSteps.block(token, blockedUserId);
        var request = ArchiveFixture.defaultRetrieveRequest();
        List<ArchiveResponse> archives = ArchiveSteps.retrieve(token, request).getCollect();

        // then
        assertThat(archives.size()).isEqualTo(1);
        assertThat(archives).noneMatch(archive -> archive.getId().equals(blockedArchive.getId()));
    }
}