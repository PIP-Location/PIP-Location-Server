package kr.co.pinpick.archive.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.co.pinpick.archive.entity.Archive;
import kr.co.pinpick.user.dto.response.UserResponse;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArchiveResponse {
    private Long id;

    private UserResponse author;

    private Double positionX;

    private Double positionY;

    private String address;

    private String name;

    private String content;

    private Boolean isPublic;

    private Boolean isLike;

    private int likeCount;

    private int commentCount;

    private LocalDateTime createdAt;

    private List<AttachResponse> archiveAttaches;

    private List<ArchiveTagResponse> tags;

    public static ArchiveResponse fromEntity(Archive archive, boolean isFollow, boolean isLike) {
        return builder()
                .id(archive.getId())
                .author(UserResponse.fromEntity(archive.getAuthor(), isFollow))
                .positionX(archive.getPositionX())
                .positionY(archive.getPositionY())
                .address(archive.getAddress())
                .name(archive.getName())
                .content(archive.getContent())
                .isPublic(archive.getIsPublic())
                .isLike(isLike)
                .likeCount(Optional.ofNullable(archive.getArchiveLikes())
                        .orElse(Collections.emptySet())
                        .size())
                .commentCount(Optional.ofNullable(archive.getArchiveComments())
                        .orElse(Collections.emptySet())
                        .size())
                .createdAt(archive.getCreatedAt())
                .archiveAttaches(archive.getArchiveAttaches().stream().map(AttachResponse::fromEntity).toList())
                .tags(archive.getTags().stream().map(ArchiveTagResponse::fromEntity).toList())
                .build();
    }
}
