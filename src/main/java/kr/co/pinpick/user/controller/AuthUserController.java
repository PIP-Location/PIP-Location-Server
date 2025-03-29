package kr.co.pinpick.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.pinpick.common.oauth.GoogleRequestBodyFactory;
import kr.co.pinpick.common.oauth.KakaoRequestBodyFactory;
import kr.co.pinpick.user.dto.request.SocialLoginRequest;
import kr.co.pinpick.user.dto.response.SocialLoginResponse;
import kr.co.pinpick.user.service.AuthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "인증 관련 API")
public class AuthUserController {
    private final AuthUserService service;
    private final GoogleRequestBodyFactory googleRequestBodyFactory;
    private final KakaoRequestBodyFactory kakaoRequestBodyFactory;

    @Operation(summary = "카카오 로그인")
    @ApiResponse(responseCode = "200")
    @PostMapping("/kakao/login")
    public ResponseEntity<SocialLoginResponse> kakaoLogin(@RequestBody SocialLoginRequest requestDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.login(requestDto.getAccessToken(), kakaoRequestBodyFactory));
    }

    @Operation(summary = "구글 로그인")
    @ApiResponse(responseCode = "200")
    @PostMapping("/google/login")
    public ResponseEntity<SocialLoginResponse> googleLogin(@RequestBody SocialLoginRequest requestDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.login(requestDto.getAccessToken(), googleRequestBodyFactory));
    }

    @Operation(summary = "카카오 로그인")
    @ApiResponse(responseCode = "200")
    @GetMapping("/user/kakao/callback")
    public ResponseEntity<SocialLoginResponse> kakaoLogin(@RequestParam("code") String code) throws JsonProcessingException {
        String accessToken = service.getToken(code, kakaoRequestBodyFactory);
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.login(accessToken, kakaoRequestBodyFactory));
    }

    @Operation(summary = "구글 로그인")
    @ApiResponse(responseCode = "200")
    @GetMapping("/user/google/callback")
    public ResponseEntity<SocialLoginResponse> googleLogin(@RequestParam("code") String code) throws JsonProcessingException {
        String accessToken = service.getToken(code, googleRequestBodyFactory);
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.login(accessToken, googleRequestBodyFactory));
    }
}
