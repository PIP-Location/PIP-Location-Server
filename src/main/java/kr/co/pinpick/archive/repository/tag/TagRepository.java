package kr.co.pinpick.archive.repository.tag;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kr.co.pinpick.archive.entity.Tag;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long>, TagRepositoryCustom {
    @Modifying
    @Query(nativeQuery = true, value = """
    insert into tags (`name`, `count`)
        select archive_tags.name, count(*) as `count` from archive_tags 
        join archives on archive_tags.archive_id = archives.id
        where archives.is_deleted = false 
        group by archive_tags.name
        on duplicate KEY UPDATE
        `count` = values(`count`)
    """)
    void collectTagCount();
}