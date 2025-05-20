package kr.co.pinpick.archive.repository.archiveLike;

import kr.co.pinpick.archive.entity.Archive;
import kr.co.pinpick.archive.entity.ArchiveLike;
import kr.co.pinpick.archive.entity.ArchiveLikeId;
import kr.co.pinpick.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface ArchiveLikeRepository extends JpaRepository<ArchiveLike, ArchiveLikeId>, ArchiveLikeRepositoryCustom {
    List<ArchiveLike> findByUserAndArchiveIdInAndIsDeletedFalse(User principal, Set<Long> archiveIds);

    boolean existsByUserAndArchiveAndIsDeletedFalse(User principal, Archive archive);

    @Query(value = "insert into archive_likes(user_id, archive_id, is_deleted) values(:userId, :archiveId, false)", nativeQuery = true)
    @Modifying
    void saveWithNativeQuery(@Param("userId") Long userId, @Param("archiveId") Long archiveId);

    int deleteByUserAndArchive(User source, Archive target);

    @Modifying(clearAutomatically = true)
    @Query("update ArchiveLike al set al.isDeleted = true where al.user = :principal")
    void bulkUpdateIsDeleted(@Param("principal") User principal);
}
