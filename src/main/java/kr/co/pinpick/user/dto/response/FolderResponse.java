package kr.co.pinpick.user.dto.response;

import kr.co.pinpick.archive.dto.response.AttachResponse;
import kr.co.pinpick.user.entity.Folder;
import kr.co.pinpick.user.entity.FolderAttach;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Collections;
import java.util.Optional;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class FolderResponse {
    private Long id;

    private String name;

    private Boolean isPublic;

    private int count;

    private AttachResponse folderAttach;

    public static FolderResponse fromEntity(Folder folder) {
        return fromEntity(folder, builder());
    }

    public static <T extends FolderResponse> T fromEntity(Folder folder, FolderResponse.FolderResponseBuilder<T, ?> builder) {
        return builder
                .id(folder.getId())
                .name(folder.getName())
                .isPublic(folder.isPublic())
                .count(Optional.ofNullable(folder.getFolderArchives())
                        .orElse(Collections.emptyList())
                        .size())
                .folderAttach(folder.getFolderAttach() != null ? AttachResponse.fromEntity(folder.getFolderAttach()) : null)
                .build();
    }
}
