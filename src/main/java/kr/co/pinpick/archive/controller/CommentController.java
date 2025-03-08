package kr.co.pinpick.archive.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.pinpick.archive.dto.CommentCollectResponse;
import kr.co.pinpick.archive.dto.CommentDetailResponse;
import kr.co.pinpick.archive.dto.CommentResponse;
import kr.co.pinpick.archive.dto.CreateCommentRequest;
import kr.co.pinpick.archive.entity.Archive;
import kr.co.pinpick.archive.entity.ArchiveComment;
import kr.co.pinpick.archive.service.CommentService;
import kr.co.pinpick.common.argumenthandler.Entity;
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
            @PathVariable(name = "archiveId") long ignoredArchiveId,
            @Entity(name = "archiveId") Archive archive,
            @AuthenticationPrincipal(errorOnInvalidType = true) User author
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(author, archive, request, null));
    }

    @Operation(summary = "대댓글 작성")
    @ApiResponse(responseCode = "201")
    @PostMapping("{commentId}")
    public ResponseEntity<CommentResponse> createSub(
            @RequestBody @Valid CreateCommentRequest request,
            @PathVariable(name = "archiveId") long ignoredArchiveId,
            @Entity(name = "archiveId") Archive archive,
            @PathVariable(name = "commentId") long ignoredCommentId,
            @Entity(name = "commentId") ArchiveComment comment,
            @AuthenticationPrincipal(errorOnInvalidType = true) User author
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(author, archive, request, comment));
    }

    @Operation(summary = "조회")
    @GetMapping
    public ResponseEntity<CommentCollectResponse> get(
            @PathVariable(name = "archiveId") String ignoredArchiveId,
            @Entity(name = "archiveId") @Valid Archive archive,
            @AuthenticationPrincipal User ignoredAuthor
    ) {
        return ResponseEntity.ok(service.get(archive));
    }

    @Operation(summary = "대댓글 조회")
    @GetMapping("{commentId}")
    public ResponseEntity<CommentDetailResponse> find(
            @PathVariable(name = "archiveId") long ignoredArchiveId,
            @Entity(name = "archiveId") Archive ignoredArchive,
            @PathVariable(name = "commentId") long ignoredCommentId,
            @Entity(name = "commentId") ArchiveComment comment,
            @AuthenticationPrincipal User ignoredAuthor
    ) {
        return ResponseEntity.ok(service.find(comment));
    }
}
