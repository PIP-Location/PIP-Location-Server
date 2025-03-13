package kr.co.pinpick.user.repository;

import kr.co.pinpick.user.entity.Block;
import kr.co.pinpick.user.entity.BlockId;
import kr.co.pinpick.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BlockRepository extends JpaRepository<Block, BlockId> {
    int deleteByAuthorAndBlock(User source, User target);

    @Query(value = "insert into blocks(author_id, block_id) values(:authorId, :blockId)", nativeQuery = true)
    @Modifying
    void saveWithNativeQuery(@Param("authorId") Long authorId, @Param("blockId") Long blockId);
}
