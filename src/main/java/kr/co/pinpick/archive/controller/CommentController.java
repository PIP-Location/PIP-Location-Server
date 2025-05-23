package kr.co.pinpick.archive.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.pinpick.archive.dto.response.CommentCollectResponse;
import kr.co.pinpick.archive.dto.response.CommentDetailResponse;
import kr.co.pinpick.archive.dto.response.CommentResponse;
import kr.co.pinpick.archive.dto.request.CreateCommentRequest;
import kr.co.pinpick.archive.service.CommentService;
import kr.co.pinpick.common.dto.request.NoOffsetPaginateRequest;
import kr.co.pinpick.common.dto.response.BaseResponse;
import kr.co.pinpick.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/archives/{archiveId}/comments")
@Tag(name = "댓글 API")
public class CommentController {
    private final CommentService service;

    @Operation(summary = "댓글 작성")
    @ApiResponse(responseCode = "201")
    @PostMapping
    public ResponseEntity<BaseResponse<CommentResponse>> create(
            @AuthenticationPrincipal User principal,
            @RequestBody @Valid CreateCommentRequest request,
            @PathVariable(name = "archiveId") Long archiveId
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.success(service.create(principal, archiveId, request, null)));
    }

    @Operation(summary = "대댓글 작성")
    @ApiResponse(responseCode = "201")
    @PostMapping("{commentId}")
    public ResponseEntity<BaseResponse<CommentResponse>> createSub(
            @AuthenticationPrincipal User principal,
            @RequestBody @Valid CreateCommentRequest request,
            @PathVariable(name = "archiveId") Long archiveId,
            @PathVariable(name = "commentId") Long commendId
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.success(service.create(principal, archiveId, request, commendId)));
    }

    @Operation(summary = "조회")
    @ApiResponse(responseCode = "200")
    @GetMapping
    public ResponseEntity<BaseResponse<CommentCollectResponse>> get(
            @AuthenticationPrincipal User ignoredPrincipal,
            @PathVariable(name = "archiveId") Long archiveId,
            @ModelAttribute NoOffsetPaginateRequest request
    ) {
        return ResponseEntity.ok(BaseResponse.success(service.get(archiveId, request)));
    }

    @Operation(summary = "대댓글 조회")
    @ApiResponse(responseCode = "200")
    @GetMapping("{commentId}")
    public ResponseEntity<BaseResponse<CommentDetailResponse>> find(
            @AuthenticationPrincipal User ignoredPrincipal,
            @PathVariable(name = "archiveId") Long ignoreArchiveId,
            @PathVariable(name = "commentId") Long commentId
    ) {
        return ResponseEntity.ok(BaseResponse.success(service.find(commentId)));
    }
}