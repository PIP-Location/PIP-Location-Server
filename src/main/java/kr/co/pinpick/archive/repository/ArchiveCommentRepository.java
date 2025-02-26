package kr.co.pinpick.archive.repository;

import kr.co.pinpick.archive.entity.ArchiveComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArchiveCommentRepository extends JpaRepository<ArchiveComment, Long> {
}
