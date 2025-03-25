package kr.co.pinpick.archive.repository.archive;

import kr.co.pinpick.archive.entity.Archive;
import kr.co.pinpick.common.error.EntityNotFoundException;
import kr.co.pinpick.common.error.ErrorCode;
import kr.co.pinpick.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArchiveRepository extends JpaRepository<Archive, Long>, ArchiveRepositoryCustom {
    default Archive findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new EntityNotFoundException(ErrorCode.ARCHIVE_NOT_FOUND, ""));
    }

    List<Archive> findAllByAuthorAndIsPublic(User author, boolean isPublic);

    List<Archive> findAllByAuthor(User author);
}
