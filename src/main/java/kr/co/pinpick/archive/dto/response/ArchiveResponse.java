package kr.co.pinpick.archive.dto.response;

import kr.co.pinpick.archive.entity.Archive;
import kr.co.pinpick.user.dto.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ArchiveResponse {
    private Long id;

    private UserResponse user;

    private Double positionX;

    private Double positionY;

    private String address;

    private String name;

    private String content;

    private Boolean isPublic;

    private LocalDateTime createdAt;

    private List<AttachResponse> archiveAttaches;

    private List<ArchiveTagResponse> tags;

    public static ArchiveResponse fromEntity(Archive archive, boolean isFollow) {
        return fromEntity(archive, isFollow, builder());
    }

    public static <T extends ArchiveResponse> T fromEntity(Archive archive, boolean isFollow, ArchiveResponse.ArchiveResponseBuilder<T, ?> builder) {
        return builder
                .id(archive.getId())
                .user(UserResponse.fromEntity(archive.getUser(), isFollow))
                .positionX(archive.getPositionX())
                .positionY(archive.getPositionY())
                .address(archive.getAddress())
                .name(archive.getName())
                .content(archive.getContent())
                .isPublic(archive.getIsPublic())
                .createdAt(archive.getCreatedAt())
                .archiveAttaches(archive.getArchiveAttaches().stream().map(AttachResponse::fromEntity).toList())
                .tags(archive.getTags().stream().map(ArchiveTagResponse::fromEntity).toList())
                .build();
    }
}