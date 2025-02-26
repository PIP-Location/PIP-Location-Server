package kr.co.pinpick.archive.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.pinpick.archive.dto.ArchiveResponse;
import kr.co.pinpick.archive.dto.CreateArchiveRequest;
import kr.co.pinpick.archive.service.ArchiveService;
import kr.co.pinpick.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
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
}
