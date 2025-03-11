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
import kr.co.pinpick.archive.service.ArchiveService;
import kr.co.pinpick.common.argumenthandler.Entity;
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
@RequestMapping("/api")
@Tag(name = "아카이브 API")
public class ArchiveController {

    private final ArchiveService service;

    @Operation(summary = "아카이브 생성")
    @ApiResponse(responseCode = "201")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, path = "archives")
    public ResponseEntity<ArchiveResponse> create(
            @AuthenticationPrincipal(errorOnInvalidType = true) User author,
            @RequestPart(value = "request", name = "request") @Valid CreateArchiveRequest request,
            @RequestPart(required = false, name = "attaches") List<MultipartFile> attaches
    ) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(author, request ,attaches));
    }

    @Operation(summary = "아카이브 조회")
    @ApiResponse(responseCode = "200")
    @GetMapping(path = "archives")
    public ResponseEntity<ArchiveCollectResponse> retrieve(
            @AuthenticationPrincipal User user,
            @ModelAttribute ArchiveRetrieveRequest request
    ) {
        return ResponseEntity.ok(service.retrieve(user, request));
    }

    @Operation(summary = "ID로 아카이브 조회")
    @ApiResponse(responseCode = "200")
    @GetMapping(path = "archives/{archiveId}")
    public ResponseEntity<ArchiveResponse> retrieve(
            @AuthenticationPrincipal User user,
            @Entity(name = "archiveId") Archive archive,
            @PathVariable(name = "archiveId") String ignoredAuthorId
    ) {
        return ResponseEntity.ok(service.get(user, archive));
    }

    @Operation(summary = "아카이브 삭제")
    @ApiResponse(responseCode = "204")
    @DeleteMapping("/archives/{archiveId}")
    public ResponseEntity<Void> deleteArchive(
            @AuthenticationPrincipal User ignoreduser,
            @Entity(name = "archiveId") Archive archive,
            @PathVariable(name = "archiveId") long ignoredAuthorId
    ) {
        service.delete(archive);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "공개/비공개 전환")
    @ApiResponse(responseCode = "200")
    @PatchMapping("/archives/{archiveId}/public/{isPublic}")
    public ResponseEntity<Boolean> changeIsPublic(
            @AuthenticationPrincipal User ignoreduser,
            @Entity(name = "archiveId") Archive archive,
            @PathVariable(name = "archiveId") long ignoredAuthorId,
            @PathVariable(name = "isPublic") boolean isPublic
    ) {
        return ResponseEntity.ok(service.changeIsPublic(archive, isPublic));
    }

    @Operation(summary = "아카이브 좋아요/좋아요 취소")
    @ApiResponse(responseCode = "204")
    @PostMapping("/archives/{archiveId}/like/{isLike}")
    public ResponseEntity<Void> like(
            @AuthenticationPrincipal User user,
            @Entity(name = "archiveId") Archive archive,
            @PathVariable(name = "archiveId") long ignoredArchiveId,
            @PathVariable(name = "isLike") boolean isLike
    ) {
        if (isLike) {
            service.like(user, archive);
        } else {
            service.dislike(user, archive);
        }
        return ResponseEntity.noContent().build();
    }
}
