package org.example.bookstoreapp.domain.library.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.common.response.ApiResponse;
import org.example.bookstoreapp.domain.auth.dto.AuthUser;
import org.example.bookstoreapp.domain.library.dto.request.AddBookRequest;
import org.example.bookstoreapp.domain.library.dto.response.LibraryBookResponse;
import org.example.bookstoreapp.domain.library.dto.response.LibraryResponse;
import org.example.bookstoreapp.domain.library.service.LibraryService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LibraryController {

    private final LibraryService libraryService;

    // 내 서재 조회 + 내 서재 생성(1회만)//
    // 무한스크롤 적용
    @GetMapping("/v1/libraries")
    public ResponseEntity<ApiResponse<Slice<LibraryBookResponse>>> getMyLibrary(
            @AuthenticationPrincipal AuthUser authUser,
            @PageableDefault(
                    page = 0,
                    size = 8,
                    sort = "addedAt",
                    direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Slice<LibraryBookResponse> librarySlice = libraryService.getMyLibrary(authUser, pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("내 서재를 조회했습니다.", librarySlice));
    }

    // 내 서재에 책 등록 //
    @PostMapping("/v1/libraries")
    public ResponseEntity<ApiResponse<LibraryResponse>> addBookLibrary(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody AddBookRequest addBookRequest
    ){
        LibraryResponse response = libraryService.addBookLibrary(authUser, addBookRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("내 서재에 책을 추가했습니다.", response));
    }

    // 내 서재에서 책 삭제 //
    @DeleteMapping("/v1/libraries/books/{bookId}") // library -> libraries로 변경
    public ResponseEntity<ApiResponse<Void>> deleteBookMyLibrary(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long bookId
    ){
      libraryService.deleteBookLibrary(authUser, bookId);

      return ResponseEntity.status(HttpStatus.OK)
              .body(ApiResponse.success("내 서재에서 책을 삭제했습니다.", null));
    }
}