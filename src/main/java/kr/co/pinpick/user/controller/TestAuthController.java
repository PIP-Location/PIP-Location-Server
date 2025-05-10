package kr.co.pinpick.user.controller;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import kr.co.pinpick.common.dto.response.BaseResponse;
import kr.co.pinpick.user.dto.request.LoginRequest;
import kr.co.pinpick.user.dto.request.SignupRequest;
import kr.co.pinpick.user.dto.response.TokenResponse;
import kr.co.pinpick.user.service.TestAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Hidden
public class TestAuthController {

    private final TestAuthService authService;

    @PostMapping("/testLogin")
    public ResponseEntity<BaseResponse<TokenResponse>> testLogin(
            @RequestBody LoginRequest request
    ) {
        TokenResponse response = authService.testLogin(request);
        return ResponseEntity.ok(BaseResponse.success(response));
    }

    @PostMapping("/signUp")
    public ResponseEntity<BaseResponse<Long>> signUp(
            @RequestBody @Valid SignupRequest request
    ) {
        Long userId = authService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.success(userId));
    }
}