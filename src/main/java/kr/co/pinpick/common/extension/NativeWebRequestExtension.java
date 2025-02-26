package kr.co.pinpick.common.extension;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.HandlerMapping;

import java.util.HashMap;
import java.util.Map;

public class NativeWebRequestExtension {
    public static Map<String, String> getPathParameters(NativeWebRequest self) {
        HttpServletRequest httpServletRequest = self.getNativeRequest(HttpServletRequest.class);

        if (httpServletRequest == null) {
            return new HashMap<>();
        }
        var attribute = httpServletRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        return (Map<String, String>) attribute;
    }
}
