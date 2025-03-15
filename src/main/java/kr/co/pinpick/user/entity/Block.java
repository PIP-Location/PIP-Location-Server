package kr.co.pinpick.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@Table(name = "blocks")
@NoArgsConstructor
@AllArgsConstructor
public class Block {
    @EmbeddedId
    private BlockId id;

    @MapsId("author")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @MapsId("block")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "block_id", nullable = false)
    private User block;
}