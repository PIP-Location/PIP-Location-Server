package kr.co.pinpick.notification.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.pinpick.notification.dto.request.NotificationTokenRequest;
import kr.co.pinpick.notification.service.NotificationService;
import kr.co.pinpick.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
@Tag(name = "알림 API")
public class NotificationController {
    private final NotificationService notificationService;

    @Operation(summary = "디바이스 토큰 정보 저장")
    @ApiResponse(responseCode = "201")
    @PostMapping
    public ResponseEntity<Void> saveDeviceToken(@AuthenticationPrincipal User principal,
                                                @RequestBody NotificationTokenRequest request) {
        notificationService.saveDeviceToken(principal, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
