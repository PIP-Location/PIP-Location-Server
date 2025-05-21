package kr.co.pinpick.notification.entity.enumerated;

import com.fasterxml.jackson.annotation.JsonCreator;
import kr.co.pinpick.common.error.BusinessException;
import kr.co.pinpick.common.error.ErrorCode;

public enum DeviceType {
    IOS,
    ANDROID;

    @JsonCreator
    public static DeviceType from(String type) {
        for (DeviceType deviceType : DeviceType.values()) {
            if (type.equals(deviceType.name())) {
                return deviceType;
            }
        }
        throw new BusinessException(ErrorCode.INVALID_TYPE_VALUE);
    }
}
