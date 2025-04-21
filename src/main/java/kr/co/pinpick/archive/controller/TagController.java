package kr.co.pinpick.archive.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.pinpick.archive.dto.response.TagCollectResponse;
import kr.co.pinpick.archive.service.TagService;
import kr.co.pinpick.common.dto.request.SearchRequest;
import kr.co.pinpick.common.dto.response.BaseResponse;
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

    @Operation(summary = "태그 검색")
    @ApiResponse(responseCode = "200")
    @GetMapping("search")
    public ResponseEntity<BaseResponse<TagCollectResponse>> search(
            @AuthenticationPrincipal User ignoredPrincipal,
            @ModelAttribute SearchRequest request
    ) {
        return ResponseEntity.ok(BaseResponse.success(service.search(request)));
    }
}
