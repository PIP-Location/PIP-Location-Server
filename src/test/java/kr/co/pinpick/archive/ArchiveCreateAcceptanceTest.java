package kr.co.pinpick.archive;

import kr.co.pinpick.AcceptanceTest;
import kr.co.pinpick.archive.dto.request.CreateTagRequest;
import kr.co.pinpick.archive.dto.response.ArchiveCollectResponse;
import kr.co.pinpick.archive.dto.response.ArchiveDetailResponse;
import kr.co.pinpick.archive.dto.response.ArchiveTagResponse;
import kr.co.pinpick.common.error.ErrorCode;
import kr.co.pinpick.folder.FolderFixture;
import kr.co.pinpick.folder.FolderSteps;
import kr.co.pinpick.user.UserFixture;
import kr.co.pinpick.user.UserSteps;
import kr.co.pinpick.user.dto.response.FolderResponse;
import kr.co.pinpick.util.MockMultipartFileFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

public class ArchiveCreateAcceptanceTest extends AcceptanceTest {
    String token;

    @BeforeEach
    public void setUp() {
        super.setUp();
//        given(storageManager.put(anyString(), any())).willReturn("test-image-path");
        UserSteps.signUp(UserFixture.defaultSignupRequest());
        token = UserSteps.testLogin(UserFixture.defaultLoginRequest()).getAccessToken();
    }

    private void assertArchive(ArchiveDetailResponse archive) {
        assertThat(archive.getId()).isNotNull();
        assertThat(archive.getUser()).isNotNull();
        assertThat(archive.getPositionX()).isNotNull();
        assertThat(archive.getPositionY()).isNotNull();
        assertThat(archive.getAddress()).isNotNull();
        assertThat(archive.getName()).isNotNull();
        assertThat(archive.getContent()).isNotNull();
        assertThat(archive.getIsPublic()).isNotNull();
        assertThat(archive.getArchiveAttaches()).isNotNull();
        assertThat(archive.getTags()).isNotNull();
    }

    //region 성공
    @Test
    public void successWithImageAndTag() throws IOException {
        var request = ArchiveFixture.defaultCreateArchiveRequest();
        var tags = IntStream.range(1, 5).boxed().map(o -> new CreateTagRequest(o.toString())).toList();
        request.setTags(tags);
        var archive = ArchiveSteps.successCreateArchive(
                token,
                request,
                MockMultipartFileFixture.mockImageFile(100, 101, "jpeg"),
                MockMultipartFileFixture.mockImageFile(200, 201, "jpeg"),
                MockMultipartFileFixture.mockImageFile(300, 301, "jpeg")
        );

        // archive 검증
        assertArchive(archive);

        // tag 순서 검증
        assertThat(archive.getTags().stream().map(ArchiveTagResponse::getName)).isSorted();

        // file 이미지 사이즈 검증, S3 저장소 구현 후 활성화
//        var attaches = archive.getArchiveAttaches();
//        assertThat(attaches.size()).isEqualTo(3);
//        assertThat(attaches.get(0).getWidth()).isEqualTo(100);
//        assertThat(attaches.get(0).getHeight()).isEqualTo(101);
//        assertThat(attaches.get(1).getWidth()).isEqualTo(200);
//        assertThat(attaches.get(1).getHeight()).isEqualTo(201);
//        assertThat(attaches.get(2).getWidth()).isEqualTo(300);
//        assertThat(attaches.get(2).getHeight()).isEqualTo(301);
    }

    @Test
    public void successWithoutImageAndTag() throws IOException {
        var request = ArchiveFixture.defaultCreateArchiveRequest();
        var archive = ArchiveSteps.successCreateArchive(token, request);

        // archive 검증
        assertArchive(archive);
    }
    //endregion

    //region 실패
    @Test
    public void failBecauseOverMaxTagCount() {
        var request = ArchiveFixture.defaultCreateArchiveRequest();
        var tags = IntStream.range(1, 12).boxed().map(o -> new CreateTagRequest(o.toString())).toList();
        request.setTags(tags);
        var error = ArchiveSteps.failCreateArchiveBecauseValidation(token, request);

        assertThat(error.getErrors()).isNotNull();
        assertThat(error.getErrors().size()).isEqualTo(1);
        assertThat(error.getErrors().get(0).getField()).isEqualTo("tags");
        assertThat(error.getErrors().get(0).getCode()).isEqualTo("Size");
    }

    @Test
    public void failBecauseTagDuplicated() {
        var request = ArchiveFixture.defaultCreateArchiveRequest();
        var tags = Stream.of("0", "0").map(CreateTagRequest::new).toList();
        request.setTags(tags);
        var error = ArchiveSteps.failCreateArchiveBecauseValidation(token, request);

        assertThat(error.getErrors()).hasSize(1).isNotNull();
        assertThat(error.getErrors()).filteredOn(o -> o.getField().equals("tags") && o.getCode().equals("UniqueElementsBy")).hasSize(1);
    }

    @Test
    public void failBecauseOverTagNameLength() {
        var request = ArchiveFixture.defaultCreateArchiveRequest();
        var tags = Stream.of("", "0123456789".repeat(5) + "1").map(CreateTagRequest::new).toList(); // 51 글자
        request.setTags(tags);
        var error = ArchiveSteps.failCreateArchiveBecauseValidation(token, request);

        assertThat(error.getErrors()).hasSize(2).isNotNull();
        assertThat(error.getErrors()).filteredOn(
                o -> o.getField().equals("tags[0].name") && o.getCode().equals("Size")
        ).hasSize(1);
        assertThat(error.getErrors()).filteredOn(
                o -> o.getField().equals("tags[1].name") && o.getCode().equals("Size")
        ).hasSize(1);
    }

    @Test
    public void failBecauseOverMaxLength() {
        var request = ArchiveFixture.defaultCreateArchiveRequest();
        request.setAddress("0123456789".repeat(20) + "1"); // 201 글자
        request.setName("0123456789".repeat(20) + "1"); // 201 글자
        request.setContent("0123456789".repeat(200) + "1"); // 2001 글자
        var error = ArchiveSteps.failCreateArchiveBecauseValidation(token, request);


        assertThat(error.getErrors()).hasSize(3).isNotNull();
        assertThat(error.getErrors()).filteredOn(
                o -> o.getField().equals("address") && o.getCode().equals("Size")
        ).hasSize(1);
        assertThat(error.getErrors()).filteredOn(
                o -> o.getField().equals("name") && o.getCode().equals("Size")
        ).hasSize(1);
        assertThat(error.getErrors()).filteredOn(
                o -> o.getField().equals("content") && o.getCode().equals("Size")
        ).hasSize(1);
    }

    // @Test
    // S3 구현 후 활성화
    public void failBecauseInvalidImage() throws IOException {
        var request = ArchiveFixture.defaultCreateArchiveRequest();
        var tags = IntStream.range(1, 5).boxed().map(o -> new CreateTagRequest(o.toString())).toList();
        request.setTags(tags);
        var error = ArchiveSteps.failCreateArchiveBecauseValidation(
                token,
                request,
                MockMultipartFileFixture.mockImageFile()
        );

        assertThat(error.getCode()).isEqualTo(ErrorCode.INVALID_TYPE_VALUE.getCode());
    }
    //endregion

    @Test
    public void getByFolder() throws IOException {
        FolderResponse folder1 = FolderSteps.createFolder(token, FolderFixture.defaultCreateFolderRequest(1), MockMultipartFileFixture.mockImageFile(100, 101, "jpeg"));
        FolderResponse folder2 = FolderSteps.createFolder(token, FolderFixture.defaultCreateFolderRequest(2), MockMultipartFileFixture.mockImageFile(100, 101, "jpeg"));

        for (int i = 1; i <= 10; i++) {
            var request = ArchiveFixture.createArchiveRequest(i);
            ArchiveDetailResponse archiveDetailResponse = ArchiveSteps.successCreateArchive(
                    token,
                    request,
                    MockMultipartFileFixture.mockImageFile(100, 101, "jpeg"),
                    MockMultipartFileFixture.mockImageFile(200, 201, "jpeg"),
                    MockMultipartFileFixture.mockImageFile(300, 301, "jpeg"));

            if (i <= 5) {
                FolderSteps.addArchiveToFolder(token, folder1.getId(), archiveDetailResponse.getId());
            } else {
                FolderSteps.addArchiveToFolder(token, folder2.getId(), archiveDetailResponse.getId());
            }
        }

        ArchiveCollectResponse archives = ArchiveSteps.getByFolder(token, 1);
        assertThat(archives.getCollect().size()).isEqualTo(5);
        for (int i = 1; i <= 5; i++) {
            assertThat(archives.getCollect().get(i - 1).getName()).isEqualTo("킨더커피" + i);
            assertThat(archives.getCollect().get(i - 1).getContent()).isEqualTo("킨더커피 크렘브륄레 마카롱 맛있어요" + i);
            assertThat(archives.getCollect().get(i - 1).getAddress()).isEqualTo("서울특별시 송파구 석촌호수로 135" + i);
            assertThat(archives.getCollect().get(i - 1).getTags().size()).isEqualTo(i);
        }
    }
}
