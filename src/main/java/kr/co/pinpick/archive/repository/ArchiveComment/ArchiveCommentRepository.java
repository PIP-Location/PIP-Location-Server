package kr.co.pinpick.archive.repository.ArchiveComment;

import kr.co.pinpick.archive.entity.Archive;
import kr.co.pinpick.archive.entity.ArchiveComment;
import kr.co.pinpick.common.error.EntityNotFoundException;
import kr.co.pinpick.common.error.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArchiveCommentRepository extends JpaRepository<ArchiveComment, Long>, ArchiveCommentRepositoryCustom {
    default ArchiveComment findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND, ""));
    }
}
