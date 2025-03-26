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
        select `name`, count(*) as `count` from archive_tags group by `name`
        on duplicate KEY UPDATE
        `count` = values(`count`)
    """)
    void collectTagCount();

    List<Tag> findAllByOrderByCountDesc(Limit limit);

    List<Tag> findByNameContainingOrderByCountDesc(@Size(max = 50) @NotNull String name, Limit limit);
}