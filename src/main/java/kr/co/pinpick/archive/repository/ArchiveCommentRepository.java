package kr.co.pinpick.archive.repository;

import kr.co.pinpick.archive.entity.Archive;
import kr.co.pinpick.archive.entity.ArchiveComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArchiveCommentRepository extends JpaRepository<ArchiveComment, Long> {
    List<ArchiveComment> findByArchiveAndParentIsNull(@Param("archive") Archive archive);
}
