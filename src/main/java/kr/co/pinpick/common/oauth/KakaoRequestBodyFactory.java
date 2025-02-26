package kr.co.pinpick.common.oauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.util.Map;

@Component
public class KakaoRequestBodyFactory implements OAuthRequestBodyFactory {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String REDIRECT_URI;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String CLIENT_SECRET;

    @Override
    public MultiValueMap<String, String> createRequestBody(String code) {
        return createDefaultRequestBody(CLIENT_ID, REDIRECT_URI, CLIENT_SECRET, code);
    }

    @Override
    public String getRequestUrl() {
        return "https://kauth.kakao.com/oauth/token";
    }

    @Override
    public String getUserInfoRequestUrl() {
        return "https://kapi.kakao.com/v2/user/me";
    }

    @Override
    public OAuth2Attributes createOauthAttribute(Map<String, Object> map) {
        return OAuth2Attributes.ofKakao(map);
    }
}
