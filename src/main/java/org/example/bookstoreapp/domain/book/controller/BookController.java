package org.example.bookstoreapp.domain.book.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.common.response.ApiResponse;
import org.example.bookstoreapp.domain.auth.dto.AuthUser;
import org.example.bookstoreapp.domain.book.dto.BookCreateRequest;
import org.example.bookstoreapp.domain.book.dto.BookResponse;
import org.example.bookstoreapp.domain.book.dto.BookSingleResponse;
import org.example.bookstoreapp.domain.book.dto.BookUpdateRequest;
import org.example.bookstoreapp.domain.book.service.BookService;
import org.example.bookstoreapp.domain.bookcontributor.dto.BookContributorRequest;
import org.example.bookstoreapp.domain.bookcontributor.dto.BookContributorResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService service;

    // 도서 등록
    @PostMapping("/admin/books")
    public ResponseEntity<ApiResponse<BookResponse>> create(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody BookCreateRequest request,
            UriComponentsBuilder uriBuilder) {

        BookResponse created = service.create(
                request,
                authUser.getId()
        );

        return ResponseEntity.created(
                uriBuilder.path("/api/admin/books/{id}")
                        .buildAndExpand(created.getId())
                        .toUri()
        ).body(ApiResponse.success("도서를 등록했습니다.", created));
    }

    /**
     * UriComponentsBuilder 설명
     * * - 스프링이 제공하는 URI 빌더
     * * - 컨트롤러 메서드 파라미터로 선언 시 스프링이 자동 주입
     * * - URI 템플릿 변수 치환, 쿼리 파라미터 추가 등 지원
     * * - 주로 POST 요청 후 생성된 리소스의 URI를 Location 헤더에 포함할 때 사용
     */

    // 도서 단건 조회
    @GetMapping("/admin/books/{bookId}")
    public ResponseEntity<ApiResponse<BookSingleResponse>> get(
            @PathVariable Long bookId,
            @RequestParam(required = false) Long lastReviewId,
            @RequestParam(required = false) LocalDateTime lastModifiedAt,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(ApiResponse.success("해당 도서를 조회했습니다.", service.get(bookId, lastReviewId, lastModifiedAt, size)));
    }

    // 도서 검색
    @GetMapping("/admin/books")
    // 검색 조건: title, category, publisher, isbn
    // 페이징: page, size
    // 정렬: sort=createdAt,desc
//    @GetMapping("/books?title=String&isbn=String&category=String&publisher=String&page=0&size=0&sort=created_at,desc")
    // OpenAPI 스펙에 맞추기 위해 쿼리 파라미터로 받음
    public ResponseEntity<ApiResponse<Page<BookResponse>>> search(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String publisher,
            @RequestParam(required = false) String isbn,
            @PageableDefault(size = 20, sort = "id") Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.success("도서를 조회했습니다.", service.search(title, category, publisher, isbn, pageable)));
    }

    // 도서 수정
    @PatchMapping("/admin/books/{bookId}")
    public ResponseEntity<ApiResponse<BookResponse>> update(
            @PathVariable Long bookId,
            @Valid @RequestBody BookUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success("해당 도서의 정보를 수정했습니다.", service.update(bookId, request)));
    }

    // 도서 삭제
    @DeleteMapping("/admin/books/{bookId}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long bookId
    ) {
        service.delete(authUser, bookId);
        return ResponseEntity.ok(ApiResponse.success("해당 도서를 삭제했습니다."));
    }

    // 도서랑 기여자 연관관계 추가 - 병수,지나,현경의 작품!!!!
    @PostMapping("/admin/books/{bookId}/contributors/{contributorId}")
    public ResponseEntity<ApiResponse<BookContributorResponse>> linkContributor(
            @PathVariable Long bookId,
            @PathVariable Long contributorId,
            @Valid @RequestBody BookContributorRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success("해당 도서에 기여자 등록을 했습니다.", service.linkContributor(bookId, contributorId, request)));
    }
}
