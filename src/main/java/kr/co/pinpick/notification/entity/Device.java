package kr.co.pinpick.notification.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kr.co.pinpick.common.entity.BaseEntity;
import kr.co.pinpick.notification.entity.enumerated.DeviceType;
import kr.co.pinpick.user.entity.User;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "devices")
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Device extends BaseEntity {
    @NotNull
    @Column(name = "device_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private DeviceType deviceType;

    @Size(max = 200)
    @NotNull
    @Column(name = "device_token", nullable = false, length = 200)
    private String deviceToken;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "device")
    private Set<DeviceNotification> deviceNotifications = new LinkedHashSet<>();
}