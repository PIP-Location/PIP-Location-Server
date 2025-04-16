package kr.co.pinpick.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.pinpick.user.dto.request.CreateFolderRequest;
import kr.co.pinpick.user.dto.response.FolderCollectResponse;
import kr.co.pinpick.user.dto.response.FolderResponse;
import kr.co.pinpick.user.entity.User;
import kr.co.pinpick.user.service.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/folders")
@Tag(name = "폴더 API")
public class FolderController {
    private final FolderService service;

    @Operation(summary = "폴더 생성")
    @ApiResponse(responseCode = "201")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<FolderResponse> create(
            @AuthenticationPrincipal User principal,
            @RequestPart(value = "request", name = "request") @Valid CreateFolderRequest request,
            @RequestPart(required = false, name = "attach") MultipartFile attach
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(principal, request, attach));
    }

    @Operation(summary = "폴더 리스트 조회")
    @ApiResponse(responseCode = "200")
    @GetMapping("users/{userId}")
    public ResponseEntity<FolderCollectResponse> getFolderList(
            @AuthenticationPrincipal User principal,
            @PathVariable(name = "userId") Long userId
    ) {
        return ResponseEntity.ok(service.getFolderList(principal, userId));
    }

    @Operation(summary = "아카이브 폴더에 등록")
    @ApiResponse(responseCode = "204")
    @PostMapping("/{folderId}/archives/{archiveId}")
    public ResponseEntity<Void> addArchiveToFolder(
            @AuthenticationPrincipal User principal,
            @PathVariable(name = "folderId") Long folderId,
            @PathVariable(name = "archiveId") Long archiveId
    ) {
        service.addArchiveToFolder(principal, folderId, archiveId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "아카이브 폴더에서 삭제")
    @ApiResponse(responseCode = "204")
    @DeleteMapping("/{folderId}/archives/{archiveId}")
    public ResponseEntity<Void> removeArchiveFromFolder(
            @AuthenticationPrincipal User ignorePrincipal,
            @PathVariable(name = "folderId") Long folderId,
            @PathVariable(name = "archiveId") Long archiveId
    ) {
        service.removeArchiveFromFolder(folderId, archiveId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "폴더 공개/비공개 전환")
    @ApiResponse(responseCode = "200")
    @PatchMapping("/{folderId}/public/{isPublic}")
    public ResponseEntity<Boolean> changeIsPublic(
            @AuthenticationPrincipal User principal,
            @PathVariable(name = "folderId") Long folderId,
            @PathVariable(name = "isPublic") boolean isPublic
    ) {
        return ResponseEntity.ok(service.changeIsPublic(principal, folderId, isPublic));
    }

    @Operation(summary = "폴더 삭제")
    @ApiResponse(responseCode = "204")
    @DeleteMapping("/{folderId}")
    public ResponseEntity<Void> deleteFolder(
            @AuthenticationPrincipal User principal,
            @PathVariable(name = "folderId") Long folderId
    ) {
        service.delete(principal, folderId);
        return ResponseEntity.noContent().build();
    }
}
