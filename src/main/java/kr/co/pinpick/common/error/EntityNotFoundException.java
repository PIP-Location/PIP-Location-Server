package kr.co.pinpick.common.error;

public class EntityNotFoundException extends BusinessException {
    public EntityNotFoundException(ErrorCode errorCode, long target) {
        this(errorCode, String.format("%d", target));
    }

    public EntityNotFoundException(ErrorCode errorCode, String target) {
        super(target + " is not found", errorCode);
    }
}
