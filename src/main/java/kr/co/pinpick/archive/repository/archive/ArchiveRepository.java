package kr.co.pinpick.archive.repository.archive;

import kr.co.pinpick.archive.entity.Archive;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArchiveRepository extends JpaRepository<Archive, Long>, ArchiveRepositoryCustom {
}
