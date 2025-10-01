package org.example.bookstoreapp.domain.searchHistory.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.common.response.ApiResponse;
import org.example.bookstoreapp.domain.auth.dto.AuthUser;
import org.example.bookstoreapp.domain.searchHistory.dto.response.SearchResponse;
import org.example.bookstoreapp.domain.searchHistory.service.SearchHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/search")
public class SearchHistoryController2 {

    private final SearchHistoryService searchHistoryService;

    // 나의 검색어 기반 도서 Top10
    // 캐싱 적용
    @GetMapping("/histories/popular/top10")
    public ResponseEntity<ApiResponse<List<SearchResponse>>> searchTop10BooksByMySearchHistoryV2(
            @AuthenticationPrincipal AuthUser authUser
    ) {
        List<SearchResponse> dtos = searchHistoryService.searchTop10BooksByMySearchHistoryV2(authUser);
        return ResponseEntity.ok(
                ApiResponse.success("검색어 기반 추천 도서 Top 10 조회입니다.", dtos)
        );
    }
}