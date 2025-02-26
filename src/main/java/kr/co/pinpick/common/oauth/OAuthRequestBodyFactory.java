package kr.co.pinpick.common.oauth;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

public interface OAuthRequestBodyFactory {

    MultiValueMap<String, String> createRequestBody(String token);

    default MultiValueMap<String, String> createDefaultRequestBody(
            String clientId,
            String redirectUri,
            String createSecret,
            String code
    ) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("client_secret", createSecret);
        body.add("code", code);
        return body;
    }

    String getRequestUrl();

    String getUserInfoRequestUrl();

    OAuth2Attributes createOauthAttribute(Map<String, Object> map);
}
