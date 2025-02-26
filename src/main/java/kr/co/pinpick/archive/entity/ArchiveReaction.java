package kr.co.pinpick.archive.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kr.co.pinpick.archive.entity.enumerated.ReactionType;
import kr.co.pinpick.common.BaseEntity;
import kr.co.pinpick.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@SuperBuilder
@Table(name = "archive_reactions")
@NoArgsConstructor
@AllArgsConstructor
public class ArchiveReaction extends BaseEntity {
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "archive_id", nullable = false)
    private Archive archive;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "reaction_type", nullable = false)
    private ReactionType reactionType;

}