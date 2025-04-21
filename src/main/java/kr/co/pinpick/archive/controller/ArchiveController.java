package kr.co.pinpick.archive.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.pinpick.archive.dto.request.RepipArchiveRequest;
import kr.co.pinpick.archive.dto.response.ArchiveCollectResponse;
import kr.co.pinpick.archive.dto.response.ArchiveDetailResponse;
import kr.co.pinpick.archive.dto.request.ArchiveRetrieveRequest;
import kr.co.pinpick.archive.dto.request.CreateArchiveRequest;
import kr.co.pinpick.archive.dto.response.ArchiveSearchResponse;
import kr.co.pinpick.archive.service.ArchiveLikeService;
import kr.co.pinpick.archive.service.ArchiveService;
import kr.co.pinpick.common.dto.request.OffsetPaginateRequest;
import kr.co.pinpick.common.dto.request.SearchRequest;
import kr.co.pinpick.common.dto.response.BaseResponse;
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
    public ResponseEntity<BaseResponse<ArchiveDetailResponse>> create(
            @AuthenticationPrincipal User principal,
            @RequestPart(value = "request", name = "request") @Valid CreateArchiveRequest request,
            @RequestPart(required = false, name = "attaches") List<MultipartFile> attaches
    ) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.success(archiveService.create(principal, request ,attaches)));
    }

    @Operation(summary = "ID로 아카이브 조회")
    @ApiResponse(responseCode = "200")
    @GetMapping(path = "{archiveId}")
    public ResponseEntity<BaseResponse<ArchiveDetailResponse>> retrieve(
            @AuthenticationPrincipal User principal,
            @PathVariable(name = "archiveId") Long archiveId
    ) {
        return ResponseEntity.ok(BaseResponse.success(archiveService.get(principal, archiveId)));
    }

    @Operation(summary = "아카이브 조회")
    @ApiResponse(responseCode = "200")
    @GetMapping
    public ResponseEntity<BaseResponse<ArchiveCollectResponse>> retrieve(
            @AuthenticationPrincipal User principal,
            @ModelAttribute ArchiveRetrieveRequest request
    ) {
        return ResponseEntity.ok(BaseResponse.success(archiveService.retrieve(principal, request)));
    }

    @Operation(summary = "아카이브 검색")
    @ApiResponse(responseCode = "200")
    @GetMapping("search")
    public ResponseEntity<BaseResponse<ArchiveSearchResponse>> search(
            @AuthenticationPrincipal User ignoredPrincipal,
            @ModelAttribute SearchRequest request
    ) {
        return ResponseEntity.ok(BaseResponse.success(archiveService.search(request)));
    }

    @Operation(summary = "작성자로 아카이브 조회")
    @ApiResponse(responseCode = "200")
    @GetMapping(path = "users/{userId}")
    public ResponseEntity<BaseResponse<ArchiveCollectResponse>> getByUser(
            @AuthenticationPrincipal User principal,
            @PathVariable(name = "userId") Long userId
    ) {
        return ResponseEntity.ok(BaseResponse.success(archiveService.getByUser(principal, userId)));
    }

    @Operation(summary = "폴더로 아카이브 조회")
    @ApiResponse(responseCode = "200")
    @GetMapping(path = "folders/{folderId}")
    public ResponseEntity<BaseResponse<ArchiveCollectResponse>> getByFolder(
            @AuthenticationPrincipal User principal,
            @PathVariable(name = "folderId") Long folderId) {
        return ResponseEntity.ok(BaseResponse.success(archiveService.getByFolder(principal, folderId)));
    }

    @Operation(summary = "아카이브 삭제")
    @ApiResponse(responseCode = "204")
    @DeleteMapping("{archiveId}")
    public ResponseEntity<BaseResponse<Void>> deleteArchive(
            @AuthenticationPrincipal User principal,
            @PathVariable(name = "archiveId") Long archiveId
    ) {
        archiveService.delete(principal, archiveId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "공개/비공개 전환")
    @ApiResponse(responseCode = "200")
    @PatchMapping("{archiveId}/public/{isPublic}")
    public ResponseEntity<BaseResponse<Boolean>> changeIsPublic(
            @AuthenticationPrincipal User principal,
            @PathVariable(name = "archiveId") Long archiveId,
            @PathVariable(name = "isPublic") boolean isPublic
    ) {
        return ResponseEntity.ok(BaseResponse.success(archiveService.changeIsPublic(principal, archiveId, isPublic)));
    }

    //region 좋아요
    @Operation(summary = "좋아요한 사람 조회")
    @ApiResponse(responseCode = "200")
    @GetMapping("{archiveId}/like")
    public ResponseEntity<BaseResponse<UserCollectResponse>> getLike(
            @AuthenticationPrincipal User ignoredPrincipal,
            @PathVariable(name = "archiveId") Long archiveId,
            @ModelAttribute OffsetPaginateRequest request
    ) {
        return ResponseEntity.ok(BaseResponse.success(archiveLikeService.getLike(archiveId, request)));
    }

    @Operation(summary = "아카이브 좋아요")
    @ApiResponse(responseCode = "204")
    @PostMapping("{archiveId}/like")
    public ResponseEntity<Void> like(
            @AuthenticationPrincipal User principal,
            @PathVariable(name = "archiveId") Long archiveId
    ) {
        archiveLikeService.link(principal, archiveId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "아카이브 좋아요 취소")
    @ApiResponse(responseCode = "204")
    @DeleteMapping("{archiveId}/like")
    public ResponseEntity<Void> unlike(
            @AuthenticationPrincipal User principal,
            @PathVariable(name = "archiveId") Long archiveId
    ) {
        archiveLikeService.unlink(principal, archiveId);
        return ResponseEntity.noContent().build();
    }
    //endregion

    //region 리핍
    @Operation(summary = "리핍")
    @ApiResponse(responseCode = "201")
    @PostMapping("{archiveId}/repip")
    public ResponseEntity<BaseResponse<ArchiveDetailResponse>> repip(
            @AuthenticationPrincipal User principal,
            @PathVariable(name = "archiveId") long archiveId,
            @RequestPart(value = "request", name = "request") @Valid RepipArchiveRequest request,
            @RequestPart(required = false, name = "attaches") List<MultipartFile> attaches
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.success(archiveService.repip(principal, archiveId, request, attaches)));
    }

    @Operation(summary = "리핍한 사람 조회")
    @ApiResponse(responseCode = "200")
    @GetMapping("{archiveId}/repip")
    public ResponseEntity<BaseResponse<UserCollectResponse>> getRepip(
            @AuthenticationPrincipal User ignorePrincipal,
            @PathVariable(name = "archiveId") Long archiveId,
            @ModelAttribute OffsetPaginateRequest request
    ) {
        return ResponseEntity.ok(BaseResponse.success(archiveService.getRepip(archiveId, request)));
    }
    //endregion
}