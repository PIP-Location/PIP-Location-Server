package kr.co.pinpick.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "인증 API")
public class TestAuthController {

    private final TestAuthService authService;

    @Operation(summary = "테스트 로그인")
    @PostMapping("/testLogin")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<TokenResponse> testLogin(
            @RequestBody LoginRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(authService.testLogin(request));
    }

    @Operation(summary = "회원가입")
    @PostMapping("/signUp")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> signUp(
            @RequestBody @Valid SignupRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.signUp(request));
    }
}
