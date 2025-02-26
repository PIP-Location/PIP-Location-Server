package kr.co.pinpick.user.entity;


import jakarta.persistence.*;
import kr.co.pinpick.archive.entity.Archive;
import kr.co.pinpick.common.BaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Table(name = "folder_archives")
@Getter
@Entity
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FolderArchive extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id")
    private Folder folder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "archive_id")
    private Archive archive;

    public static FolderArchive of(Folder folder, Archive archive) {
        return builder()
                .folder(folder)
                .archive(archive)
                .build();
    }
}
