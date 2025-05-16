package kr.co.pinpick.archive.repository.archiveComment;

import kr.co.pinpick.archive.entity.ArchiveComment;
import kr.co.pinpick.common.error.EntityNotFoundException;
import kr.co.pinpick.common.error.ErrorCode;
import kr.co.pinpick.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArchiveCommentRepository extends JpaRepository<ArchiveComment, Long>, ArchiveCommentRepositoryCustom {
    default ArchiveComment findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND, ""));
    }

    @Modifying(clearAutomatically = true)
    @Query("update ArchiveComment ac set ac.isDeleted = true where ac.user = :principal")
    void bulkUpdateIsDeleted(@Param("principal") User principal);
}
