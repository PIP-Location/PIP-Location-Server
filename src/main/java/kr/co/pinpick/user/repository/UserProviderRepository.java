package kr.co.pinpick.user.repository;

import kr.co.pinpick.user.entity.UserProvider;
import kr.co.pinpick.user.entity.enumerated.ProviderType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProviderRepository extends JpaRepository<UserProvider, Long> {
    Optional<UserProvider> findByProviderTypeAndProviderUserId(ProviderType providerType, String providerUserId);
}
