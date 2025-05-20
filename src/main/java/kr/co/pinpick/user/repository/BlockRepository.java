package kr.co.pinpick.user.repository;

import kr.co.pinpick.user.entity.Block;
import kr.co.pinpick.user.entity.BlockId;
import kr.co.pinpick.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BlockRepository extends JpaRepository<Block, BlockId> {
    int deleteByUserAndBlock(User source, User target);

    @Query(value = "insert into blocks(user_id, block_id, is_deleted) values(:userId, :blockId, false)", nativeQuery = true)
    @Modifying
    void saveWithNativeQuery(@Param("userId") Long userId, @Param("blockId") Long blockId);

    @Modifying(clearAutomatically = true)
    @Query("update Block b set b.isDeleted = true where b.user = :principal or b.block = :principal")
    void bulkUpdateIsDeleted(@Param("principal") User principal);
}
