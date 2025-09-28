package org.example.bookstoreapp.domain.searchHistory.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.common.response.ApiResponse;
import org.example.bookstoreapp.domain.auth.dto.AuthUser;
import org.example.bookstoreapp.domain.book.entity.BookCategory;
import org.example.bookstoreapp.domain.searchHistory.dto.response.SearchResponse;
import org.example.bookstoreapp.domain.searchHistory.service.SearchHistoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/search")
public class SearchHistoryController {

    private final SearchHistoryService searchHistoryService;

    // 키워드 검색
    @GetMapping
    public ResponseEntity<ApiResponse<Page<SearchResponse>>> searchKeyword(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal @Nullable AuthUser authUser    // null 가능 -> 비로그인한 유저도 검색 가능하도록
    ) {
        BookCategory bookCategory = BookCategory.from(category);
        Page<SearchResponse> response = searchHistoryService.searchKeyword(
                title,
                name,
                bookCategory,
                pageable,
                authUser
        );

        return ResponseEntity.ok(
                ApiResponse.success("검색이 완료되었습니다.", response)
        );
    }

        // 내 검색 기록 조회
        // @GetMapping

        // 인기 검색어 조회
        // @GetMapping()
}
