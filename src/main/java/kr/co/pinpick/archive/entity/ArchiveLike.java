package kr.co.pinpick.archive.entity;

import jakarta.persistence.*;
import kr.co.pinpick.user.entity.User;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@Table(name = "archive_likes")
@NoArgsConstructor
@AllArgsConstructor
public class ArchiveLike {
    @EmbeddedId
    private ArchiveLIkeId id;

    @MapsId("user")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @MapsId("archive")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "archive_id", nullable = false)
    private Archive archive;
}