package kr.co.pinpick.archive.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.pinpick.archive.dto.response.ArchiveCollectResponse;
import kr.co.pinpick.archive.dto.response.ArchiveResponse;
import kr.co.pinpick.archive.dto.response.ArchiveRetrieveRequest;
import kr.co.pinpick.archive.dto.request.CreateArchiveRequest;
import kr.co.pinpick.archive.entity.Archive;
import kr.co.pinpick.archive.service.ArchiveLikeService;
import kr.co.pinpick.archive.service.ArchiveService;
import kr.co.pinpick.common.argumenthandler.Entity;
import kr.co.pinpick.common.aspect.CheckArchiveAuthorization;
import kr.co.pinpick.user.dto.response.UserResponse;
import kr.co.pinpick.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/archives")
@Tag(name = "아카이브 API")
public class ArchiveController {
    private final ArchiveService archiveService;
    private final ArchiveLikeService archiveLikeService;

    @Operation(summary = "아카이브 생성")
    @ApiResponse(responseCode = "201")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ArchiveResponse> create(
            @AuthenticationPrincipal(errorOnInvalidType = true) User author,
            @RequestPart(value = "request", name = "request") @Valid CreateArchiveRequest request,
            @RequestPart(required = false, name = "attaches") List<MultipartFile> attaches
    ) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(archiveService.create(author, request ,attaches));
    }

    @Operation(summary = "아카이브 조회")
    @ApiResponse(responseCode = "200")
    @GetMapping
    public ResponseEntity<ArchiveCollectResponse> retrieve(
            @AuthenticationPrincipal User user,
            @ModelAttribute ArchiveRetrieveRequest request
    ) {
        return ResponseEntity.ok(archiveService.retrieve(user, request));
    }

    @Operation(summary = "ID로 아카이브 조회")
    @ApiResponse(responseCode = "200")
    @GetMapping(path = "{archiveId}")
    public ResponseEntity<ArchiveResponse> retrieve(
            @AuthenticationPrincipal User user,
            @Entity(name = "archiveId") Archive archive,
            @PathVariable(name = "archiveId") long ignoredArchiveId
    ) {
        return ResponseEntity.ok(archiveService.get(user, archive));
    }

    @Operation(summary = "작성자로 아카이브 조회")
    @ApiResponse(responseCode = "200")
    @GetMapping(path = "users/{authorId}")
    public ResponseEntity<ArchiveCollectResponse> getByAuthor(
            @AuthenticationPrincipal User user,
            @Entity(name = "authorId") User author,
            @PathVariable(name = "authorId") long ignoredAuthorId
    ) {
        return ResponseEntity.ok(archiveService.getByUser(user, author));
    }

    @Operation(summary = "아카이브 삭제")
    @ApiResponse(responseCode = "204")
    @CheckArchiveAuthorization
    @DeleteMapping("{archiveId}")
    public ResponseEntity<Void> deleteArchive(
            @AuthenticationPrincipal User ignoredAuthor,
            @Entity(name = "archiveId") Archive archive,
            @PathVariable(name = "archiveId") long ignoredArchiveId
    ) {
        archiveService.delete(archive);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "공개/비공개 전환")
    @ApiResponse(responseCode = "200")
    @CheckArchiveAuthorization
    @PatchMapping("{archiveId}/public/{isPublic}")
    public ResponseEntity<Boolean> changeIsPublic(
            @AuthenticationPrincipal User ignoredAuthor,
            @Entity(name = "archiveId") Archive archive,
            @PathVariable(name = "archiveId") long ignoredArchiveId,
            @PathVariable(name = "isPublic") boolean isPublic
    ) {
        return ResponseEntity.ok(archiveService.changeIsPublic(archive, isPublic));
    }

    //region 좋아요
    @Operation(summary = "아카이브 좋아요")
    @PostMapping("{archiveId}/like")
    public ResponseEntity<UserResponse> like(
            @PathVariable(name = "archiveId") long ignoredArchiveId,
            @Entity(name = "archiveId") Archive archive,
            @AuthenticationPrincipal(errorOnInvalidType = true) User user
    ) {
        archiveLikeService.link(user, archive);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "아카이브 좋아요 취소")
    @DeleteMapping("{archiveId}/like")
    public ResponseEntity<UserResponse> unlike(
            @PathVariable(name = "archiveId") long ignoredArchiveId,
            @Entity(name = "archiveId") Archive archive,
            @AuthenticationPrincipal(errorOnInvalidType = true) User user
    ) {
        archiveLikeService.unlink(user, archive);
        return ResponseEntity.noContent().build();
    }
    //endregion
}
