package kr.co.pinpick.archive.dto.response;

import kr.co.pinpick.archive.entity.ArchiveComment;
import kr.co.pinpick.user.dto.response.UserResponse;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

@Getter
@SuperBuilder
public class CommentResponse {
    private long id;

    private String content;

    private UserResponse user;

    private int subCommentCount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static CommentResponse fromEntity(ArchiveComment comment) {
        return fromEntity(comment, builder());
    }

    public static <T extends CommentResponse> T fromEntity(ArchiveComment comment, CommentResponse.CommentResponseBuilder<T, ?> builder) {
        return builder
                .id(comment.getId())
                .content(comment.getContent())
                .user(UserResponse.fromEntity(comment.getUser()))
                .subCommentCount(Optional.ofNullable(comment.getSubComments())
                        .orElse(Collections.emptyList())
                        .size())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}