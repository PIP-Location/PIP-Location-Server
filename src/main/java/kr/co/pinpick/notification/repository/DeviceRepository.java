package kr.co.pinpick.notification.repository;

import kr.co.pinpick.notification.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, Long> {
}
