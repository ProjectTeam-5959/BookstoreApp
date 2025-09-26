package org.example.bookstoreapp.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.common.response.ApiResponse;
import org.example.bookstoreapp.domain.auth.dto.AuthUser;
import org.example.bookstoreapp.domain.user.dto.request.UserChangeNameAndPasswordRequest;
import org.example.bookstoreapp.domain.user.dto.response.UserResponse;
import org.example.bookstoreapp.domain.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/usrs/me")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(
            @AuthenticationPrincipal AuthUser authUser
    ) {
        return ResponseEntity.ok(
                ApiResponse.success("내 정보 조회에 성공했습니다", userService.getUser(authUser.getId()))
        );
    }

    @PutMapping("/users/me")
    public ResponseEntity<ApiResponse<UserResponse>> changeNicknameAndPassword(
            @AuthenticationPrincipal AuthUser authuser,
            @RequestBody UserChangeNameAndPasswordRequest request
    ) {
        return ResponseEntity.ok(
                ApiResponse.success("내 정보를 수정했습니다.", userService.changeNicknameAndPassword(authuser.getId(),request))
        );
    }

}
