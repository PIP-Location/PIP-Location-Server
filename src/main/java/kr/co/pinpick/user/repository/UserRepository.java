package kr.co.pinpick.user.repository;

import kr.co.pinpick.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
