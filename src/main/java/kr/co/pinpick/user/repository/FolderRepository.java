package kr.co.pinpick.user.repository;

import kr.co.pinpick.common.error.EntityNotFoundException;
import kr.co.pinpick.common.error.ErrorCode;
import kr.co.pinpick.user.entity.Folder;
import kr.co.pinpick.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FolderRepository extends JpaRepository<Folder, Long> {
    default Folder findByIdOrElseThrow(Long folderId) {
        return findById(folderId).orElseThrow(() -> new EntityNotFoundException(ErrorCode.FOLDER_NOT_FOUND, ""));
    };

    List<Folder> findAllByUserAndIsPublic(User user, boolean isPublic);

    List<Folder> findAllByUser(User user);
}
