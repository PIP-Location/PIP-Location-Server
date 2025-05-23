package kr.co.pinpick.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kr.co.pinpick.common.entity.AttachEntity;
import kr.co.pinpick.common.entity.listener.AttachListener;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@SuperBuilder
@Table(name = "folder_attaches")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AttachListener.class)
public class FolderAttach extends AttachEntity {
    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "folder_id", nullable = false)
    private Folder folder;

    public void setFolder(Folder folder) {
        this.folder = folder;
        if (folder != null && folder.getFolderAttach() != this) {
            folder.setFolderAttach(this); // 양방향 유지
        }
    }
}
