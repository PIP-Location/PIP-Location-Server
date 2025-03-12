package kr.co.pinpick.user.repository;

import kr.co.pinpick.archive.entity.Archive;
import kr.co.pinpick.user.entity.Folder;
import kr.co.pinpick.user.entity.FolderArchive;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FolderArchiveRepository extends JpaRepository<FolderArchive, Long> {
    Optional<FolderArchive> findByFolderAndArchive(Folder folder, Archive archive);

    void deleteByFolderAndArchive(Folder folder, Archive archive);
}
