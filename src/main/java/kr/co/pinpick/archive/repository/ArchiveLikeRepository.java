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

    List<ArchiveLike> findByAuthorAndArchive(User user, Archive archive);

    List<ArchiveLike> findByAuthorAndArchiveIdIn(@Param(value = "author") User author, @Param("archiveIds") Set<Long> archiveIds);

    boolean existsByAuthorAndArchive(User user, Archive archive);

    @Query(value = "insert into archive_likes(author_id, archive_id) values(:authorId, :archiveId)", nativeQuery = true)
    @Modifying
    void saveWithNativeQuery(@Param("authorId") Long authorId, @Param("archiveId") Long archiveId);

    int deleteByAuthorAndArchive(User source, Archive target);
}
