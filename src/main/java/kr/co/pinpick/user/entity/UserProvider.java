package kr.co.pinpick.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kr.co.pinpick.common.BaseEntity;
import kr.co.pinpick.user.entity.enumerated.ProviderType;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "user_providers")
public class UserProvider extends BaseEntity {

    @NotNull
    @Column(name = "provider_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Size(max = 100)
    @NotNull
    @Column(name = "provider_user_id", nullable = false, length = 100)
    private String providerUserId;

    public static UserProvider of(ProviderType providerType, User user, String providerUserId) {
        return builder()
                .providerType(providerType)
                .user(user)
                .providerUserId(providerUserId)
                .build();
    }
}