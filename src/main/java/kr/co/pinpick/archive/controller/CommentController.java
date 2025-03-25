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
    public ResponseEntity<CommentResponse> create(
            @RequestBody @Valid CreateCommentRequest request,
            @PathVariable(name = "archiveId") Long archiveId,
            @AuthenticationPrincipal(errorOnInvalidType = true) User author
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(author, archiveId, request, null));
    }

    @Operation(summary = "대댓글 작성")
    @ApiResponse(responseCode = "201")
    @PostMapping("{commentId}")
    public ResponseEntity<CommentResponse> createSub(
            @RequestBody @Valid CreateCommentRequest request,
            @PathVariable(name = "archiveId") Long archiveId,
            @PathVariable(name = "commentId") Long commendId,
            @AuthenticationPrincipal(errorOnInvalidType = true) User author
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(author, archiveId, request, commendId));
    }

    @Operation(summary = "조회")
    @GetMapping
    public ResponseEntity<CommentCollectResponse> get(
            @PathVariable(name = "archiveId") Long archiveId,
            @AuthenticationPrincipal User ignoredAuthor
    ) {
        return ResponseEntity.ok(service.get(archiveId));
    }

    @Operation(summary = "대댓글 조회")
    @GetMapping("{commentId}")
    public ResponseEntity<CommentDetailResponse> find(
            @PathVariable(name = "archiveId") Long ignoreArchiveId,
            @PathVariable(name = "commentId") Long commentId,
            @AuthenticationPrincipal User ignoredAuthor
    ) {
        return ResponseEntity.ok(service.find(commentId));
    }
}
