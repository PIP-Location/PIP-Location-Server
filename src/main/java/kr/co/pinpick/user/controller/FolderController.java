package kr.co.pinpick.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.pinpick.archive.entity.Archive;
import kr.co.pinpick.common.argumenthandler.Entity;
import kr.co.pinpick.user.dto.request.CreateFolderRequest;
import kr.co.pinpick.user.dto.response.FolderCollectResponse;
import kr.co.pinpick.user.dto.response.FolderResponse;
import kr.co.pinpick.user.entity.Folder;
import kr.co.pinpick.user.entity.User;
import kr.co.pinpick.user.service.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/folders")
@Tag(name = "폴더 API")
public class FolderController {
    private final FolderService folderService;

    @Operation(summary = "폴더 생성")
    @ApiResponse(responseCode = "201")
    @PostMapping
    public ResponseEntity<FolderResponse> createFolder(
            @AuthenticationPrincipal(errorOnInvalidType = true) User user,
            @RequestBody @Valid CreateFolderRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(folderService.createFolder(user, request));
    }

    @Operation(summary = "폴더 리스트 조회")
    @ApiResponse(responseCode = "200")
    @GetMapping
    public ResponseEntity<FolderCollectResponse> getFolderList(
            @AuthenticationPrincipal(errorOnInvalidType = true) User user
    ) {
        return ResponseEntity.ok(folderService.getFolderList(user));
    }

    @Operation(summary = "아카이브 폴더에 등록")
    @ApiResponse(responseCode = "204")
    @PostMapping("/{folderId}/archives/{archiveId}")
    public ResponseEntity<Void> addArchiveToFolder(
            @AuthenticationPrincipal(errorOnInvalidType = true) User user,
            @PathVariable(name = "folderId") long ignoredFolderId,
            @Entity(name = "folderId") Folder folder,
            @PathVariable(name = "archiveId") long ignoredArchiveId,
            @Entity(name = "archiveId") Archive archive
    ) {
        folderService.addArchiveToFolder(user, folder, archive);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "아카이브 폴더에서 삭제")
    @ApiResponse(responseCode = "204")
    @DeleteMapping("/{folderId}/archives/{archiveId}")
    public ResponseEntity<Void> removeArchiveFromFolder(
            @AuthenticationPrincipal(errorOnInvalidType = true) User ignoreUser,
            @PathVariable(name = "folderId") long ignoredFolderId,
            @Entity(name = "folderId") Folder folder,
            @PathVariable(name = "archiveId") long ignoredArchiveId,
            @Entity(name = "archiveId") Archive archive
    ) {
        folderService.removeArchiveFromFolder(folder, archive);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "폴더 공개/비공개 전환")
    @ApiResponse(responseCode = "200")
    @PatchMapping("/{folderId}/public/{isPublic}")
    public ResponseEntity<Boolean> changeIsPublic(
            @AuthenticationPrincipal User ignoreUser,
            @Entity(name = "folderId") Folder folder,
            @PathVariable(name = "folderId") long ignoredFolderId,
            @PathVariable(name = "isPublic") boolean isPublic
    ) {
        return ResponseEntity.ok(folderService.changeIsPublic(folder, isPublic));
    }

    @Operation(summary = "폴더 삭제")
    @ApiResponse(responseCode = "204")
    @DeleteMapping("/{folderId}")
    public ResponseEntity<Void> deleteFolder(
            @AuthenticationPrincipal User ignoreUser,
            @Entity(name = "folderId") Folder folder,
            @PathVariable(name = "folderId") long ignoredFolderId
    ) {
        folderService.deleteFolder(folder);
        return ResponseEntity.noContent().build();
    }
}
