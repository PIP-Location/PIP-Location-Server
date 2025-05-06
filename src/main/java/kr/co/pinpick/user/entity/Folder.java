package kr.co.pinpick.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kr.co.pinpick.common.entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Table(name = "folders")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Folder extends BaseEntity {
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    private String name;

    @OneToOne(mappedBy = "folder", cascade = CascadeType.ALL)
    private FolderAttach folderAttach;

    @NotNull
    @Column(name = "is_public", nullable = false)
    @Setter
    @Builder.Default
    private boolean isPublic = false;

    @OneToMany(mappedBy = "folder", cascade = CascadeType.REMOVE)
    private List<FolderArchive> folderArchives;
}
