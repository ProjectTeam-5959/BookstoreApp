package org.example.bookstoreapp.domain.contributor.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.common.response.ApiResponse;
import org.example.bookstoreapp.domain.auth.dto.AuthUser;
import org.example.bookstoreapp.domain.contributor.dto.ContributorCreateRequest;
import org.example.bookstoreapp.domain.contributor.dto.ContributorResponse;
import org.example.bookstoreapp.domain.contributor.service.ContributorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ContributorController {
    private final ContributorService service;

    @PostMapping("/admin/contributors")
    public ResponseEntity<ApiResponse<ContributorResponse>> create(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody ContributorCreateRequest req) {

        ContributorResponse created = service.create(
                req,
                authUser.getId()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("기여자 생성되었습니다.",created));
    }
}
