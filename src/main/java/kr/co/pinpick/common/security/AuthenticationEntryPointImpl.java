package kr.co.pinpick.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.pinpick.common.error.ErrorCode;
import kr.co.pinpick.common.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) {
        ErrorCode exception = (ErrorCode) request.getAttribute("exception");
        if (exception == null) exception = ErrorCode.UNAUTHENTICATED;
        exceptionHandler(response, exception);
    }

    public void exceptionHandler(HttpServletResponse response, ErrorCode exception) {
        response.setStatus(exception.getStatus());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            String json = new ObjectMapper().writeValueAsString(ErrorResponse.of(exception));
            response.getWriter().write(json);
            log.error(exception.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
