package kr.co.pinpick.folder;

import kr.co.pinpick.user.dto.request.CreateFolderRequest;

public class FolderFixture {

    public static CreateFolderRequest defaultCreateFolderRequest(int i) {
        return CreateFolderRequest.builder()
                .name("folder" + i)
                .build();
    }
}
