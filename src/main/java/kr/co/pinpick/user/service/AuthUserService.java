package kr.co.pinpick.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.pinpick.common.oauth.OAuth2Attributes;
import kr.co.pinpick.common.oauth.OAuthRequestBodyFactory;
import kr.co.pinpick.common.security.JwtUtil;
import kr.co.pinpick.user.dto.response.SocialLoginResponse;
import kr.co.pinpick.user.entity.User;
import kr.co.pinpick.user.entity.UserProvider;
import kr.co.pinpick.user.repository.UserProviderRepository;
import kr.co.pinpick.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthUserService {

    private final JwtUtil jwtUtil;

    private final UserRepository userRepository;

    private final UserProviderRepository userProviderRepository;

    @Transactional
    public SocialLoginResponse login(String token, OAuthRequestBodyFactory factory) {
        Map<String, Object> map = getUserAttributes(factory, token);
        OAuth2Attributes attributes = factory.createOauthAttribute(map);
        return processUserLogin(attributes);
    }

    private SocialLoginResponse processUserLogin(OAuth2Attributes attributes) {
        return userProviderRepository
                .findByProviderTypeAndProviderUserId(attributes.getProviderType(), attributes.getProviderUserId())
                .map(existingProvider -> buildLoginResponse(existingProvider.getUser()))
                .orElseGet(() -> buildNewUserResponse(attributes));
    }

    private SocialLoginResponse buildLoginResponse(User user) {
        return SocialLoginResponse.from(jwtUtil.createToken(user), HttpStatus.OK, 200);
    }

    private SocialLoginResponse buildNewUserResponse(OAuth2Attributes attributes) {
        User user = registerNewUser(attributes);
        return SocialLoginResponse.from(jwtUtil.createToken(user), HttpStatus.CREATED, 201);
    }

    private User registerNewUser(OAuth2Attributes attributes) {
        User user = userRepository.save(User.from(attributes));
        UserProvider userProvider = UserProvider.of(attributes.getProviderType(), user, attributes.getProviderUserId());
        userProviderRepository.save(userProvider);
        return user;
    }

    private Map<String, Object> getUserAttributes(OAuthRequestBodyFactory factory, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate
                .exchange(factory.getUserInfoRequestUrl(), HttpMethod.GET, request, String.class);
        return parseResponseBody(response.getBody());
    }

    private Map<String, Object> parseResponseBody(String responseBody) {
        try {
            return new ObjectMapper().readValue(responseBody, new TypeReference<>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse user attributes", e);
        }
    }

    @Transactional(readOnly = true)
    public String getToken(String code, OAuthRequestBodyFactory factory) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = factory.createRequestBody(code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                factory.getRequestUrl(),
                HttpMethod.POST,
                tokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }
}
