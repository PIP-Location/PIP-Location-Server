package kr.co.pinpick.archive.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.pinpick.archive.dto.request.TagRetrieveRequest;
import kr.co.pinpick.archive.dto.response.TagCollectResponse;
import kr.co.pinpick.archive.service.TagService;
import kr.co.pinpick.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/tags")
@Tag(name = "태그 API")
@RestController
@RequiredArgsConstructor
public class TagController {
    private final TagService service;

    @Operation(summary = "사용된 태그 조회")
    @GetMapping
    public ResponseEntity<TagCollectResponse> get(
            @AuthenticationPrincipal User ignoredUser,
            @ModelAttribute TagRetrieveRequest request
    ) {
        return ResponseEntity.ok(service.get(request));
    }
}
