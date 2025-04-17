package kr.co.pinpick.user.dto.response;

import kr.co.pinpick.archive.dto.response.ArchiveDetailResponse;
import kr.co.pinpick.user.entity.Folder;
import kr.co.pinpick.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class FolderDetailResponse extends FolderResponse {
    private List<ArchiveDetailResponse> archiveDetailRespons;

    public static FolderDetailResponse fromEntity(Folder folder, User user, Map<Long, Boolean> isLikeMap) {
        return fromEntity(
                folder,
                builder().archiveDetailRespons(folder.getFolderArchives()
                        .stream()
                        .map(fa -> ArchiveDetailResponse.fromEntity(
                                fa.getArchive(),
                                !user.getId().equals(fa.getArchive().getId()),
                                isLikeMap.containsKey(fa.getArchive().getId())))
                        .toList()));
    }
}
