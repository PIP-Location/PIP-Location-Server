package kr.co.pinpick.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kr.co.pinpick.common.BaseEntity;
import kr.co.pinpick.user.dto.request.CreateFolderRequest;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "folders")
@Getter
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

    @NotNull
    @Column(name = "is_public", nullable = false)
    @Setter
    private boolean isPublic = false;

    @OneToMany(mappedBy = "folder", cascade = CascadeType.REMOVE)
    private List<FolderArchive> folderArchives;

    public static Folder of(User user, CreateFolderRequest request) {
        return builder()
                .user(user)
                .name(request.getName())
                .isPublic(request.isPublic())
                .folderArchives(new ArrayList<>())
                .build();
    }
}
