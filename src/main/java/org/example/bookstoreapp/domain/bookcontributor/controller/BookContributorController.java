package org.example.bookstoreapp.domain.bookcontributor.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.domain.bookcontributor.dto.BookContributorCreateRequest;
import org.example.bookstoreapp.domain.bookcontributor.service.BookContributorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BookContributorController {
    private final BookContributorService service;

    // 도서-기여자 등록
    @PostMapping("/admin/book-contributors")        // URL 경로에서 하이픈(-) 사용 → 가독성 향상
    public ResponseEntity<Void> create(
            @Valid @RequestBody BookContributorCreateRequest req) {
        // 등록된 도서-기여자 ID 반환
        Long id = service.create(req);

        /**
         * URI 생성
         * - ServletUriComponentsBuilder.fromCurrentRequest(): 현재 요청 URI를 기반으로
         * - .path("/{id}"): 새 경로 세그먼트 추가
         * - .buildAndExpand(id): {id}를 실제 ID 값으로 치환
         * - .toUri(): URI 객체로 변환
         *
         * */
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        // 응답 본문 없이 201 Created 상태 코드와 Location 헤더 반환
        return ResponseEntity.created(location).build();
    }
}
