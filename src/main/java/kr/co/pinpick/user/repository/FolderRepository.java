package kr.co.pinpick.user.repository;

import kr.co.pinpick.user.entity.Folder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FolderRepository extends JpaRepository<Folder, Long> {
}
