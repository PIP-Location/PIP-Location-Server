package kr.co.pinpick.archive.repository.archive;

import kr.co.pinpick.archive.entity.Archive;
import kr.co.pinpick.common.error.EntityNotFoundException;
import kr.co.pinpick.common.error.ErrorCode;
import kr.co.pinpick.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArchiveRepository extends JpaRepository<Archive, Long>, ArchiveRepositoryCustom {
    default Archive findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND, "Archive"));
    }

    @Modifying(clearAutomatically = true)
    @Query("update Archive a set a.isDeleted = true where a.user = :principal")
    void bulkUpdateIsDeleted(@Param("principal") User principal);
}
