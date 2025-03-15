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

    @MapsId("author")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @MapsId("archive")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "archive_id", nullable = false)
    private Archive archive;
}