package kr.co.pinpick.archive.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.pinpick.archive.dto.response.ArchiveCollectResponse;
import kr.co.pinpick.archive.dto.response.ArchiveResponse;
import kr.co.pinpick.archive.dto.request.ArchiveRetrieveRequest;
import kr.co.pinpick.archive.dto.request.CreateArchiveRequest;
import kr.co.pinpick.archive.service.ArchiveLikeService;
import kr.co.pinpick.archive.service.ArchiveService;
import kr.co.pinpick.user.dto.response.FolderDetailResponse;
import kr.co.pinpick.user.dto.response.UserCollectResponse;
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

    @Operation(summary = "ID로 아카이브 조회")
    @ApiResponse(responseCode = "200")
    @GetMapping(path = "{archiveId}")
    public ResponseEntity<ArchiveResponse> retrieve(
            @AuthenticationPrincipal User user,
            @PathVariable(name = "archiveId") Long archiveId
    ) {
        return ResponseEntity.ok(archiveService.get(user, archiveId));
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

    @Operation(summary = "작성자로 아카이브 조회")
    @ApiResponse(responseCode = "200")
    @GetMapping(path = "users/{authorId}")
    public ResponseEntity<ArchiveCollectResponse> getByAuthor(
            @AuthenticationPrincipal User user,
            @PathVariable(name = "authorId") Long authorId
    ) {
        return ResponseEntity.ok(archiveService.getByUser(user, authorId));
    }

    @Operation(summary = "폴더로 아카이브 조회")
    @ApiResponse(responseCode = "200")
    @GetMapping(path = "folders/{folderId}")
    public ResponseEntity<ArchiveCollectResponse> getByFolder(
            @AuthenticationPrincipal User user,
            @PathVariable(name = "folderId") Long folderId) {
        return ResponseEntity.ok(archiveService.getByFolder(user, folderId));
    }

    @Operation(summary = "폴더정보와 함께 아카이브 조회")
    @ApiResponse(responseCode = "200")
    @GetMapping(path = "folders/{folderId}/info")
    public ResponseEntity<FolderDetailResponse> getArchivesWithFolderInfo(
            @AuthenticationPrincipal User user,
            @PathVariable(name = "folderId") Long folderId) {
        return ResponseEntity.ok(archiveService.getArchivesWithFolderInfo(user, folderId));
    }

    @Operation(summary = "아카이브 삭제")
    @ApiResponse(responseCode = "204")
    @DeleteMapping("{archiveId}")
    public ResponseEntity<Void> deleteArchive(
            @AuthenticationPrincipal User author,
            @PathVariable(name = "archiveId") Long archiveId
    ) {
        archiveService.delete(author, archiveId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "공개/비공개 전환")
    @ApiResponse(responseCode = "200")
    @PatchMapping("{archiveId}/public/{isPublic}")
    public ResponseEntity<Boolean> changeIsPublic(
            @AuthenticationPrincipal User author,
            @PathVariable(name = "archiveId") Long archiveId,
            @PathVariable(name = "isPublic") boolean isPublic
    ) {
        return ResponseEntity.ok(archiveService.changeIsPublic(author, archiveId, isPublic));
    }

    //region 좋아요
    @Operation(summary = "좋아요한 사람 목록 조회")
    @ApiResponse(responseCode = "200")
    @GetMapping("{archiveId}/like")
    public ResponseEntity<UserCollectResponse> get(
            @PathVariable(name = "archiveId") Long archiveId,
            @AuthenticationPrincipal(errorOnInvalidType = true) User user
    ) {
        return ResponseEntity.ok(archiveLikeService.get(user, archiveId));
    }

    @Operation(summary = "아카이브 좋아요")
    @ApiResponse(responseCode = "204")
    @PostMapping("{archiveId}/like")
    public ResponseEntity<Void> like(
            @PathVariable(name = "archiveId") Long archiveId,
            @AuthenticationPrincipal(errorOnInvalidType = true) User user
    ) {
        archiveLikeService.link(user, archiveId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "아카이브 좋아요 취소")
    @ApiResponse(responseCode = "204")
    @DeleteMapping("{archiveId}/like")
    public ResponseEntity<Void> unlike(
            @PathVariable(name = "archiveId") long archiveId,
            @AuthenticationPrincipal(errorOnInvalidType = true) User user
    ) {
        archiveLikeService.unlink(user, archiveId);
        return ResponseEntity.noContent().build();
    }
    //endregion
}
