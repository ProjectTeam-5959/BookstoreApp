package org.example.bookstoreapp.domain.book.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.domain.auth.dto.AuthUser;
import org.example.bookstoreapp.domain.book.dto.BookCreateRequest;
import org.example.bookstoreapp.domain.book.dto.BookResponse;
import org.example.bookstoreapp.domain.book.dto.BookUpdateRequest;
import org.example.bookstoreapp.domain.book.service.BookService;
import org.example.bookstoreapp.domain.bookcontributor.dto.BookContributorRequest;
import org.example.bookstoreapp.domain.bookcontributor.dto.BookContributorResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService service;

    // 도서 등록
    @PostMapping("/admin/books")
    public ResponseEntity<BookResponse> create(
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
        ).body(created);
    }

    /**
     * UriComponentsBuilder 설명
     * * - 스프링이 제공하는 URI 빌더
     * * - 컨트롤러 메서드 파라미터로 선언 시 스프링이 자동 주입
     * * - URI 템플릿 변수 치환, 쿼리 파라미터 추가 등 지원
     * * - 주로 POST 요청 후 생성된 리소스의 URI를 Location 헤더에 포함할 때 사용
     */

    // 도서 단건 조회
    @GetMapping("/books/{bookId}")
    public BookResponse get(
            @PathVariable Long id,
            @PageableDefault(size = 20, sort = "id") Pageable pageable
    ) {
        return service.get(id);
    }

    // 도서 검색
    @GetMapping("/books")
    // 검색 조건: title, category, publisher, isbn
    // 페이징: page, size
    // 정렬: sort=createdAt,desc
//    @GetMapping("/books?title=String&isbn=String&category=String&publisher=String&page=0&size=0&sort=created_at,desc")
    // OpenAPI 스펙에 맞추기 위해 쿼리 파라미터로 받음
    public Page<BookResponse> search(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String publisher,
            @RequestParam(required = false) String isbn,
            @PageableDefault(size = 20, sort = "id") Pageable pageable
    ) {
        return service.search(title, category, publisher, isbn, pageable);
    }

    // 도서 수정
    @PatchMapping("/admin/books/{bookId}")
    public BookResponse update(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long id,
            @Valid @RequestBody BookUpdateRequest request) {
        return service.update(id, request);
    }

    // 도서 삭제
    @DeleteMapping("/admin/books/{bookId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long id) {
        service.delete(id);
    }

    // 도서랑 기여자 연관관계 추가 - 병수,지나,현경의 작품!!!!
    @PostMapping("/admin/books/{bookId}/contributors/{contributorId}")
    public ResponseEntity<BookContributorResponse> linkContributor(
            @PathVariable Long bookId,
            @PathVariable Long contributorId,
            @Valid @RequestBody BookContributorRequest request
    ) {
        return ResponseEntity.ok(service.linkContributor( bookId, contributorId, request));
    }
}
