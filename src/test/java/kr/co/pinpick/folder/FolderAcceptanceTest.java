package kr.co.pinpick.folder;

import kr.co.pinpick.AcceptanceTest;
import kr.co.pinpick.archive.ArchiveFixture;
import kr.co.pinpick.archive.ArchiveSteps;
import kr.co.pinpick.archive.dto.response.ArchiveDetailResponse;
import kr.co.pinpick.user.UserFixture;
import kr.co.pinpick.user.UserSteps;
import kr.co.pinpick.user.dto.request.UpdateFolderRequest;
import kr.co.pinpick.user.dto.response.FolderResponse;
import kr.co.pinpick.util.MockMultipartFileFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class FolderAcceptanceTest extends AcceptanceTest {
    String token;

    Long userId;

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
        userId = UserSteps.signUp(UserFixture.defaultSignupRequest());
        token = UserSteps.testLogin(UserFixture.defaultLoginRequest()).getAccessToken();
    }

    @Test
    public void createFolderSuccess() throws IOException {
        // given && when
        FolderResponse response = FolderSteps
                .createFolder(
                        token, FolderFixture.defaultCreateFolderRequest(1),
                        MockMultipartFileFixture.mockImageFile(100, 100, "jpeg")
                );

        // then
        assertThat(response.getId()).isNotNull();
        assertThat(response.getName()).isEqualTo("folder1");
        assertThat(response.getCount()).isEqualTo(0);
    }

    @Test
    public void getFolderListSuccess() throws IOException {
        // given
        for (int i = 1; i <= 5; i++) {
            FolderSteps.createFolder(token, FolderFixture.defaultCreateFolderRequest(i), MockMultipartFileFixture.mockImageFile(100, 100, "jpeg"));
        }

        // when
        List<FolderResponse> folders = FolderSteps.getFolderList(token, userId).getCollect();

        // then
        assertThat(folders.size()).isEqualTo(5);
    }

    @Test
    public void addArchiveToFolderSuccess() throws IOException {
        // given
        ArchiveDetailResponse archiveDetailResponse1 = ArchiveSteps.successCreateArchive(token, ArchiveFixture.defaultCreateArchiveRequest());
        ArchiveDetailResponse archiveDetailResponse2 = ArchiveSteps.successCreateArchive(token, ArchiveFixture.defaultCreateArchiveRequest());
        FolderResponse folderResponse = FolderSteps.createFolder(token, FolderFixture.defaultCreateFolderRequest(1), MockMultipartFileFixture.mockImageFile(100, 101, "jpeg"));

        // when
        FolderSteps.addArchiveToFolder(token, folderResponse.getId(), archiveDetailResponse1.getId());
        FolderSteps.addArchiveToFolder(token, folderResponse.getId(), archiveDetailResponse2.getId());
        List<FolderResponse> folders = FolderSteps.getFolderList(token, userId).getCollect();

        // then
        assertThat(folders.size()).isEqualTo(1);
        assertThat(folders.get(0).getCount()).isEqualTo(2);
    }

    @Test
    public void removeArchiveToFolderSuccess() throws IOException {
        // given
        ArchiveDetailResponse archiveDetailResponse1 = ArchiveSteps.successCreateArchive(token, ArchiveFixture.defaultCreateArchiveRequest());
        ArchiveDetailResponse archiveDetailResponse2 = ArchiveSteps.successCreateArchive(token, ArchiveFixture.defaultCreateArchiveRequest());
        FolderResponse folderResponse = FolderSteps.createFolder(token, FolderFixture.defaultCreateFolderRequest(1), MockMultipartFileFixture.mockImageFile(100, 101, "jpeg"));
        FolderSteps.addArchiveToFolder(token, folderResponse.getId(), archiveDetailResponse1.getId());
        FolderSteps.addArchiveToFolder(token, folderResponse.getId(), archiveDetailResponse2.getId());

        // when
        FolderSteps.removeArchiveToFolder(token, folderResponse.getId(), archiveDetailResponse1.getId());
        List<FolderResponse> folders = FolderSteps.getFolderList(token, userId).getCollect();

        // then
        assertThat(folders.size()).isEqualTo(1);
        assertThat(folders.get(0).getCount()).isEqualTo(1);
    }

    @Test
    public void updateFolder() throws IOException {
        var updateFolderRequest = FolderFixture.updateFolderRequest();

    }

    @Test
    public void deleteFolder() throws IOException {

    }
}
