package kr.co.pinpick.common.error;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
