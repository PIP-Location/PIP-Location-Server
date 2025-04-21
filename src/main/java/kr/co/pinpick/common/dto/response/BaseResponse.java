package kr.co.pinpick.common.dto.response;

import kr.co.pinpick.common.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaseResponse<T> {
    private boolean success;
    private Integer errorCode;
    private String message;
    private T data;

    public static <T> BaseResponse<T> success(T data) {
        return BaseResponse.<T>builder()
                .success(true)
                .data(data)
                .build();
    }

    public static <T> BaseResponse<T> error(ErrorCode exception) {
        return BaseResponse.<T>builder()
                .success(false)
                .errorCode(exception.getStatus())
                .message(exception.getMessage())
                .build();
    }
}
