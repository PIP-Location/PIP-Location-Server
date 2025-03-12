package kr.co.pinpick.user.repository;

import kr.co.pinpick.user.entity.Folder;
import kr.co.pinpick.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FolderRepository extends JpaRepository<Folder, Long> {
    List<Folder> findAllByUser(User user);
}
