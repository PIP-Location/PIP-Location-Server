package kr.co.pinpick.archive.repository;

import kr.co.pinpick.archive.entity.ArchiveTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArchiveTagRepository extends JpaRepository<ArchiveTag, Long> {
}
