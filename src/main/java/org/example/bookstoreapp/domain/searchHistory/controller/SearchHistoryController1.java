package org.example.bookstoreapp.domain.searchHistory.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.common.response.ApiResponse;
import org.example.bookstoreapp.domain.auth.dto.AuthUser;
import org.example.bookstoreapp.domain.book.entity.BookCategory;
import org.example.bookstoreapp.domain.searchHistory.dto.response.MySearchHistoryResponse;
import org.example.bookstoreapp.domain.searchHistory.dto.response.SearchResponse;
import org.example.bookstoreapp.domain.searchHistory.repository.SearchHistoryRepository;
import org.example.bookstoreapp.domain.searchHistory.service.SearchHistoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/search")
public class SearchHistoryController1 {

    private final SearchHistoryService searchHistoryService;

    // 키워드 검색
    @GetMapping
    public ResponseEntity<ApiResponse<Page<SearchResponse>>> searchKeywordV2(
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

    // 나의 검색어 기록 조회
    @GetMapping("/histories")
    public ResponseEntity<ApiResponse<Page<MySearchHistoryResponse>>> mySearchHistory(
            @AuthenticationPrincipal AuthUser authUser,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<MySearchHistoryResponse> histories = searchHistoryService.mySearchHistory(authUser, pageable);
        return ResponseEntity.ok(
                ApiResponse.success("조회가 완료되었습니다.", histories)
        );
    }

    // 인기 키워드 조회
    // 인기 키워드 title별 조회
    @GetMapping("/popularKeywords/titles")
    public ResponseEntity<ApiResponse<Page<SearchHistoryRepository.PopularKeywordCount>>> searchPopularTitles(
            @PageableDefault(sort = "cnt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(
                ApiResponse.success("제목별 인기 검색순 조회 완료되었습니다", searchHistoryService.searchPopularTitles(pageable))
        );
    }

    // 인기 키워드 name별 조회
    @GetMapping("/popularKeywords/names")
    public ResponseEntity<ApiResponse<Page<SearchHistoryRepository.PopularKeywordCount>>> searchPopularNames(
            @PageableDefault(sort = "cnt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(
                ApiResponse.success("기여자별 인기 검색순 조회 완료되었습니다", searchHistoryService.searchPopularNames(pageable))
        );
    }

    // 인기 키워드 category별 조회
    @GetMapping("/popularKeywords/categories")
    public ResponseEntity<ApiResponse<Page<SearchHistoryRepository.PopularKeywordCount>>> searchPopularCategories(
            @PageableDefault(sort = "cnt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(
                ApiResponse.success("카테고리별 인기 검색순 조회 완료되었습니다", searchHistoryService.searchPopularCategories(pageable))
        );
    }

    // 나의 검색어 기반 도서 Top10
    @GetMapping("/histories/popular/top10")
    public ResponseEntity<ApiResponse<List<SearchResponse>>> searchTop10BooksByMySearchHistoryV1(
            @AuthenticationPrincipal AuthUser authUser
    ) {
        List<SearchResponse> dtos = searchHistoryService.searchTop10BooksByMySearchHistoryV1(authUser);
        return ResponseEntity.ok(
                ApiResponse.success("검색어 기반 추천 도서 Top 10 조회입니다.", dtos)
        );
    }
}