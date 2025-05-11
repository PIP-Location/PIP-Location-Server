package kr.co.pinpick.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.pinpick.common.dto.response.BaseResponse;
import kr.co.pinpick.user.dto.request.CreateFolderRequest;
import kr.co.pinpick.user.dto.request.UpdateFolderRequest;
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

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/folders")
@Tag(name = "폴더 API")
public class FolderController {
    private final FolderService service;

    @Operation(summary = "폴더 생성")
    @ApiResponse(responseCode = "201")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BaseResponse<FolderResponse>> create(
            @AuthenticationPrincipal User principal,
            @Parameter(description = "CreateFolderRequest")
            @RequestPart(value = "request", name = "request") @Valid CreateFolderRequest request,
            @RequestPart(required = false, name = "attach") MultipartFile attach
    ) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.success(service.create(principal, request, attach)));
    }

    @Operation(summary = "폴더 리스트 조회")
    @ApiResponse(responseCode = "200")
    @GetMapping("users/{userId}")
    public ResponseEntity<BaseResponse<FolderCollectResponse>> getFolderList(
            @AuthenticationPrincipal User principal,
            @PathVariable(name = "userId") Long userId
    ) {
        return ResponseEntity.ok(BaseResponse.success(service.getFolderList(principal, userId)));
    }

    @Operation(summary = "아카이브 폴더에 등록")
    @ApiResponse(responseCode = "200")
    @PostMapping("{folderId}/archives/{archiveId}")
    public ResponseEntity<BaseResponse<Void>> addArchiveToFolder(
            @AuthenticationPrincipal User principal,
            @PathVariable(name = "folderId") Long folderId,
            @PathVariable(name = "archiveId") Long archiveId
    ) {
        service.addArchiveToFolder(principal, folderId, archiveId);
        return ResponseEntity.ok(BaseResponse.success(null));
    }

    @Operation(summary = "아카이브 폴더에서 삭제")
    @ApiResponse(responseCode = "200")
    @DeleteMapping("{folderId}/archives/{archiveId}")
    public ResponseEntity<BaseResponse<Void>> removeArchiveFromFolder(
            @AuthenticationPrincipal User ignorePrincipal,
            @PathVariable(name = "folderId") Long folderId,
            @PathVariable(name = "archiveId") Long archiveId
    ) {
        service.removeArchiveFromFolder(folderId, archiveId);
        return ResponseEntity.ok(BaseResponse.success(null));
    }

    @Operation(summary = "폴더 수정")
    @ApiResponse(responseCode = "200")
    @PatchMapping(value = "{folderId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BaseResponse<FolderResponse>> updateFolder(
            @AuthenticationPrincipal User principal,
            @PathVariable(name = "folderId") Long folderId,
            @Parameter(description = "UpdateFolderRequest")
            @RequestPart(value = "request", name = "request") @Valid UpdateFolderRequest request,
            @RequestPart(required = false, name = "attach") MultipartFile attach
    ) throws IOException {
        return ResponseEntity.ok(BaseResponse.success(service.updateFolder(principal, folderId, request, attach)));
    }

    @Operation(summary = "폴더 삭제")
    @ApiResponse(responseCode = "200")
    @DeleteMapping("{folderId}")
    public ResponseEntity<BaseResponse<Void>> deleteFolder(
            @AuthenticationPrincipal User principal,
            @PathVariable(name = "folderId") Long folderId
    ) {
        service.delete(principal, folderId);
        return ResponseEntity.ok(BaseResponse.success(null));
    }
}