package kr.co.pinpick.archive.repository;

import kr.co.pinpick.archive.entity.Archive;
import kr.co.pinpick.archive.entity.ArchiveLike;
import kr.co.pinpick.archive.entity.ArchiveLIkeId;
import kr.co.pinpick.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface ArchiveLikeRepository extends JpaRepository<ArchiveLike, ArchiveLIkeId> {

    List<ArchiveLike> findByUserAndArchive(User user, Archive archive);

    List<ArchiveLike> findByUserAndArchiveIdIn(User principal, Set<Long> archiveIds);

    boolean existsByUserAndArchive(User principal, Archive archive);

    @Query(value = "insert into archive_likes(user_id, archive_id) values(:userId, :archiveId)", nativeQuery = true)
    @Modifying
    void saveWithNativeQuery(@Param("userId") Long userId, @Param("archiveId") Long archiveId);

    int deleteByUserAndArchive(User source, Archive target);
}
