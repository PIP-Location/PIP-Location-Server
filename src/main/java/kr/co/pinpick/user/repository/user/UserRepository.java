package kr.co.pinpick.user.repository.user;

import kr.co.pinpick.common.error.EntityNotFoundException;
import kr.co.pinpick.common.error.ErrorCode;
import kr.co.pinpick.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    default User findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND, "User"));
    }

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByIdIn(Set<Long> userIds);
}
