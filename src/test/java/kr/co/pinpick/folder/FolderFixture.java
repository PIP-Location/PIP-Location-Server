package kr.co.pinpick.folder;

import kr.co.pinpick.user.dto.request.CreateFolderRequest;
import kr.co.pinpick.user.dto.request.UpdateFolderRequest;

public class FolderFixture {

    public static CreateFolderRequest defaultCreateFolderRequest(int i) {
        return CreateFolderRequest.builder()
                .name("folder" + i)
                .build();
    }

    public static UpdateFolderRequest updateFolderRequest() {
        return UpdateFolderRequest.builder()
                .name("folder2")
                .isPublic(false)
                .build();
    }
}
