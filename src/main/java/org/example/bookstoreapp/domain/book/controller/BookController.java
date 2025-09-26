package org.example.bookstoreapp.domain.book.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.domain.book.dto.BookCreateRequest;
import org.example.bookstoreapp.domain.book.dto.BookResponse;
import org.example.bookstoreapp.domain.book.dto.BookUpdateRequest;
import org.example.bookstoreapp.domain.book.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BookController {

    private final BookService service;

    // 도서 등록
    @PostMapping("/admin/books")
    public ResponseEntity<BookResponse> create(
            @Valid @RequestBody BookCreateRequest request,
            UriComponentsBuilder uriBuilder) {
        BookResponse created = service.create(request);
        return ResponseEntity.created(
                uriBuilder.path("/api/admin/books").build(created.getId()) // 하드코딩은 피하는게??
        ).body(created);
    }

    /**
     * UriComponentsBuilder 설명
     * * - 스프링이 제공하는 URI 빌더
     * * - 컨트롤러 메서드 파라미터로 선언 시 스프링이 자동 주입
     * * - URI 템플릿 변수 치환, 쿼리 파라미터 추가 등 지원
     * * - 주로 POST 요청 후 생성된 리소스의 URI를 Location 헤더에 포함할 때 사용
     *
     * */

    // 도서 단건 조회
    @GetMapping("/books/{bookid}")
    public BookResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    // 도서 검색
    @GetMapping("/books?title=String&isbn=String&category=String&publisher=String&page=0&size=0&sort=created_at,desc")
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
    @PatchMapping("/admin/books/{bookid}")
    public BookResponse update(@PathVariable Long id, @Valid @RequestBody BookUpdateRequest request) {
        return service.update(id, request);
    }

    // 도서 삭제
    @DeleteMapping("/admin/books/{bookid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
