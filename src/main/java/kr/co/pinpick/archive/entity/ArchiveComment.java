package kr.co.pinpick.archive.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kr.co.pinpick.common.BaseEntity;
import kr.co.pinpick.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "archive_comments")
@SuperBuilder
@NoArgsConstructor
public class ArchiveComment extends BaseEntity {
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "archive_id", nullable = false)
    private Archive archive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private ArchiveComment parent;

    @Size(max = 2000)
    @NotNull
    @Column(name = "content", nullable = false, length = 200)
    private String content;

    @OneToMany(mappedBy = "parent")
    private List<ArchiveComment> subComments = new ArrayList<>();

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}

