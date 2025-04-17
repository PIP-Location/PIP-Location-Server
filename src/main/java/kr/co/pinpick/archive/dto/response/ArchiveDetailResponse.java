package kr.co.pinpick.archive.dto.response;

import kr.co.pinpick.archive.entity.Archive;
import kr.co.pinpick.archive.entity.ArchiveComment;
import kr.co.pinpick.user.dto.response.UserResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Collections;
import java.util.Optional;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ArchiveDetailResponse extends ArchiveResponse {
    private Boolean isLike;
    private int likeCount;
    private int commentCount;
    private ArchiveResponse repipArchive;

    public static ArchiveDetailResponse fromEntity(Archive archive, boolean isFollow, boolean isLike) {
        return fromEntity(
                archive,
                isFollow,
                builder()
                        .isLike(isLike)
                        .likeCount(Optional.ofNullable(archive.getArchiveLikes())
                                .orElse(Collections.emptySet())
                                .size())
                        .commentCount(Optional.ofNullable(archive.getArchiveComments())
                                .orElse(Collections.emptySet())
                                .size())
                        .repipArchive(archive.getRepipArchive() == null ? null : fromEntity(archive.getRepipArchive(), isFollow))
        );
    }
}
