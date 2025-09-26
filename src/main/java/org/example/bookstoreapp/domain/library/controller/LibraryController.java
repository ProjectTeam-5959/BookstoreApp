package org.example.bookstoreapp.domain.library.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.common.response.ApiResponse;
import org.example.bookstoreapp.domain.auth.dto.AuthUser;
import org.example.bookstoreapp.domain.library.dto.response.LibraryResponse;
import org.example.bookstoreapp.domain.library.service.LibraryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LibraryController {

    private final LibraryService libraryService;

    // 내 서재 조회 + 내 서재 생성(1회만)//
    @GetMapping("/v1/libraries")
    public ResponseEntity<ApiResponse<LibraryResponse>> getMyLibrary(
            @AuthenticationPrincipal AuthUser authUser
    ) {
        LibraryResponse response = libraryService.getMyLibrary(authUser);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("내 서재를 조회했습니다.", response));
    }
}
