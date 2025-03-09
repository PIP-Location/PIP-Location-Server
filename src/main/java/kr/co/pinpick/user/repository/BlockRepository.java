package kr.co.pinpick.user.repository;

import kr.co.pinpick.user.entity.Block;
import kr.co.pinpick.user.entity.BlockId;
import kr.co.pinpick.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockRepository extends JpaRepository<Block, BlockId> {
    int deleteByAuthorAndBlock(User source, User target);
}
