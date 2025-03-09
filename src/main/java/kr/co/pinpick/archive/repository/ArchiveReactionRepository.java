package kr.co.pinpick.archive.repository;

import kr.co.pinpick.archive.entity.Archive;
import kr.co.pinpick.archive.entity.ArchiveReaction;
import kr.co.pinpick.archive.entity.enumerated.ReactionType;
import kr.co.pinpick.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ArchiveReactionRepository extends JpaRepository<ArchiveReaction, Long> {
    List<ArchiveReaction> findByAuthorAndReactionTypeAndArchiveIdIn(@Param(value = "author") User author, @Param(value = "reactionType") ReactionType reactionType, @Param("archiveIds") Set<Long> archiveIds);

    Optional<ArchiveReaction> findByAuthorAndArchive(User user, Archive archive);

    boolean existsByAuthorAndReactionTypeAndArchive(User user, ReactionType like, Archive archive);
}
