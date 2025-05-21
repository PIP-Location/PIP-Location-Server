package kr.co.pinpick.notification.service;

import kr.co.pinpick.notification.dto.request.NotificationTokenRequest;
import kr.co.pinpick.notification.entity.Device;
import kr.co.pinpick.notification.repository.DeviceRepository;
import kr.co.pinpick.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final DeviceRepository deviceRepository;

    @Transactional
    public void saveDeviceToken(User user, NotificationTokenRequest request) {
        Device device = Device.builder()
                .deviceType(request.getDeviceType())
                .deviceToken(request.getDeviceToken())
                .user(user)
                .build();

        deviceRepository.save(device);
    }
}
