package kr.co.pinpick.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.pinpick.common.dto.request.SearchRequest;
import kr.co.pinpick.common.dto.response.BaseResponse;
import kr.co.pinpick.user.dto.request.UpdateUserRequest;
import kr.co.pinpick.user.dto.response.UserDetailResponse;
import kr.co.pinpick.user.dto.response.UserSearchResponse;
import kr.co.pinpick.user.entity.User;
import kr.co.pinpick.user.service.UserService;
import kr.co.pinpick.user.service.UserBlockService;
import kr.co.pinpick.user.service.UserFollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "유저 관련 API")
public class UserController {
    private final UserService userService;
    private final UserFollowService userFollowService;
    private final UserBlockService userBlockService;

    //region 조회
    @Operation(summary = "회원 검색")
    @ApiResponse(responseCode = "200")
    @GetMapping("search")
    public ResponseEntity<BaseResponse<UserSearchResponse>> search(
            @AuthenticationPrincipal User principal,
            @ModelAttribute SearchRequest request
    ) {
        return ResponseEntity.ok(BaseResponse.success(userService.search(principal, request)));
    }

    @Operation(summary = "로그인된 회원 조회")
    @ApiResponse(responseCode = "200")
    @GetMapping("@me")
    public ResponseEntity<BaseResponse<UserDetailResponse>> me(
            @AuthenticationPrincipal User principal
    ) {
        return ResponseEntity.ok(BaseResponse.success(userService.find(principal, principal.getId())));
    }

    @Operation(summary = "회원 상세 조회")
    @ApiResponse(responseCode = "200")
    @GetMapping("{userId}")
    public ResponseEntity<BaseResponse<UserDetailResponse>> getUserDetail(
            @AuthenticationPrincipal User principal,
            @PathVariable(name = "userId") Long userId
    ) {
        return ResponseEntity.ok(BaseResponse.success(userService.find(principal, userId)));
    }
    //endregion

    @Operation(summary = "회원정보 수정")
    @ApiResponse(responseCode = "200")
    @PatchMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BaseResponse<UserDetailResponse>> update(
            @AuthenticationPrincipal User principal,
            @Parameter(description = "UpdateUserRequest")
            @RequestPart(value = "request", name = "request") @Valid UpdateUserRequest request,
            @RequestPart(required = false, name = "attach") MultipartFile attach
    ) throws IOException {
        return ResponseEntity.ok(BaseResponse.success(userService.update(principal, request, attach)));
    }

    //region 차단
    @Operation(summary = "회원 차단")
    @ApiResponse(responseCode = "204")
    @PostMapping("{userId}/block")
    public ResponseEntity<Void> block(
            @AuthenticationPrincipal User principal,
            @PathVariable(name = "userId") Long userId
    ) {
        userBlockService.link(principal, userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "회원 차단해제")
    @ApiResponse(responseCode = "204")
    @DeleteMapping("{userId}/unblock")
    public ResponseEntity<Void> unblock(
            @AuthenticationPrincipal User principal,
            @PathVariable(name = "userId") Long userId
    ) {
        userBlockService.unlink(principal, userId);
        return ResponseEntity.noContent().build();
    }
    //endregion

    //region 팔로우
    @Operation(summary = "회원 팔로우")
    @ApiResponse(responseCode = "204")
    @PostMapping("{userId}/follow")
    public ResponseEntity<Void> follow(
            @AuthenticationPrincipal User principal,
            @PathVariable(name = "userId") Long userId
    ) {
        userFollowService.link(principal, userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "회원 팔로우 해제")
    @ApiResponse(responseCode = "204")
    @DeleteMapping("{userId}/unfollow")
    public ResponseEntity<Void> unfollow(
            @AuthenticationPrincipal User principal,
            @PathVariable(name = "userId") Long userId
    ) {
        userFollowService.unlink(principal, userId);
        return ResponseEntity.noContent().build();
    }
    //endregion
}