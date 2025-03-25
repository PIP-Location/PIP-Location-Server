package kr.co.pinpick.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.pinpick.user.dto.request.UpdateUserRequest;
import kr.co.pinpick.user.dto.response.UserDetailResponse;
import kr.co.pinpick.user.entity.User;
import kr.co.pinpick.user.service.UserService;
import kr.co.pinpick.user.service.UserBlockService;
import kr.co.pinpick.user.service.UserFollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "유저 관련 API")
public class UserController {
    private final UserService userService;
    private final UserFollowService userFollowService;
    private final UserBlockService userBlockService;

    //region 조회
    @Operation(summary = "로그인된 회원 조회")
    @GetMapping("@me")
    public ResponseEntity<UserDetailResponse> me(
            @AuthenticationPrincipal(errorOnInvalidType = true) User user
    ) {
        return ResponseEntity.ok(userService.find(user, user.getId()));
    }
    @Operation(summary = "회원 상세 조회")
    @GetMapping("{userId}")
    public ResponseEntity<UserDetailResponse> getUserDetail(
            @AuthenticationPrincipal(errorOnInvalidType = true) User user,
            @PathVariable(name = "userId") Long targetId
    ) {
        return ResponseEntity.ok(userService.find(user, targetId));
    }
    //endregion

    @Operation(summary = "회원정보 수정")
    @PatchMapping
    public ResponseEntity<UserDetailResponse> update(
            @AuthenticationPrincipal(errorOnInvalidType = true) User user,
            @RequestPart(value = "request", name = "request") @Valid UpdateUserRequest request,
            @RequestPart(required = false, name = "attaches") MultipartFile profileImage
    ) {
        return ResponseEntity.ok(userService.update(user, request, profileImage));
    }

    //region 차단
    @Operation(summary = "회원 차단")
    @PostMapping("{userId}/block")
    public ResponseEntity<Void> block(
            @PathVariable(name = "userId") Long userId,
            @AuthenticationPrincipal(errorOnInvalidType = true) User author
    ) {
        userBlockService.link(author, userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "회원 차단해제")
    @DeleteMapping("{userId}/unblock")
    public ResponseEntity<Void> unblock(
            @PathVariable(name = "userId") Long userId,
            @AuthenticationPrincipal(errorOnInvalidType = true) User author
    ) {
        userBlockService.unlink(author, userId);
        return ResponseEntity.noContent().build();
    }
    //endregion

    //region 팔로우
    @Operation(summary = "회원 팔로우")
    @PostMapping("{userId}/follow")
    public ResponseEntity<Void> follow(
            @PathVariable(name = "userId") Long userId,
            @AuthenticationPrincipal(errorOnInvalidType = true) User author
    ) {
        userFollowService.link(author, userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "회원 팔로우 해제")
    @DeleteMapping("{userId}/unfollow")
    public ResponseEntity<Void> unfollow(
            @PathVariable(name = "userId") Long userId,
            @AuthenticationPrincipal(errorOnInvalidType = true) User author
    ) {
        userFollowService.unlink(author, userId);
        return ResponseEntity.noContent().build();
    }
    //endregion
}
