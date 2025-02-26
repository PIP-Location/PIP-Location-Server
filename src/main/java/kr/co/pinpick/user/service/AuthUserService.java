package kr.co.pinpick.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.co.pinpick.common.oauth.OAuthRequestBodyFactory;
import kr.co.pinpick.user.dto.response.SocialLoginResponse;

public interface AuthUserService {
    SocialLoginResponse login(String token, OAuthRequestBodyFactory factory);

    String getToken(String code, OAuthRequestBodyFactory factory) throws JsonProcessingException;
}
