package kr.co.pinpick.search.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.pinpick.search.dto.request.SearchRequest;
import kr.co.pinpick.common.dto.response.BaseResponse;
import kr.co.pinpick.search.dto.response.ArchiveSearchResponse;
import kr.co.pinpick.search.dto.response.SearchCollectResponse;
import kr.co.pinpick.search.dto.response.TagSearchResponse;
import kr.co.pinpick.search.dto.response.UserSearchResponse;
import kr.co.pinpick.search.service.SearchService;
import kr.co.pinpick.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
@Tag(name = "검색 API")
public class SearchController {
    private final SearchService service;

    @Operation(summary = "전체 검색")
    @ApiResponse(responseCode = "200")
    @GetMapping
    public ResponseEntity<BaseResponse<?>> search(
            @AuthenticationPrincipal User ignoredPrincipal,
            @ModelAttribute SearchRequest request
    ) {
        return ResponseEntity.ok(BaseResponse.success(service.search(request)));
    }

    @Operation(summary = "아카이브 검색")
    @ApiResponse(responseCode = "200")
    @GetMapping("archive")
    public ResponseEntity<BaseResponse<SearchCollectResponse<ArchiveSearchResponse>>> searchArchive(
            @AuthenticationPrincipal User ignoredPrincipal,
            @ModelAttribute SearchRequest request
    ) {
        return ResponseEntity.ok(BaseResponse.success(service.searchArchive(request)));
    }

    @Operation(summary = "유저 검색")
    @ApiResponse(responseCode = "200")
    @GetMapping("user")
    public ResponseEntity<BaseResponse<SearchCollectResponse<UserSearchResponse>>> searchUser(
            @AuthenticationPrincipal User principal,
            @ModelAttribute SearchRequest request
    ) {
        return ResponseEntity.ok(BaseResponse.success(service.searchUser(principal, request)));
    }

    @Operation(summary = "태그 검색")
    @ApiResponse(responseCode = "200")
    @GetMapping("tag")
    public ResponseEntity<BaseResponse<SearchCollectResponse<TagSearchResponse>>> searchTag(
            @AuthenticationPrincipal User ignoredPrincipal,
            @ModelAttribute SearchRequest request
    ) {
        return ResponseEntity.ok(BaseResponse.success(service.searchTag(request)));
    }
}
