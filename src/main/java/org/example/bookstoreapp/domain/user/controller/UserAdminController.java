package org.example.bookstoreapp.domain.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookstoreapp.common.response.ApiResponse;
import org.example.bookstoreapp.domain.auth.dto.AuthUser;
import org.example.bookstoreapp.domain.user.dto.request.UserRoleChangeRequest;
import org.example.bookstoreapp.domain.user.dto.response.UserResponse;
import org.example.bookstoreapp.domain.user.enums.UserRole;
import org.example.bookstoreapp.domain.user.service.UserAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Secured(UserRole.Authority.ADMIN)
@RestController
@RequiredArgsConstructor
public class UserAdminController {

    private final UserAdminService userAdminService;

    @PatchMapping("/admin/users/{userId}/userRole")
    public ResponseEntity<ApiResponse<UserResponse>> changeUserRole(@PathVariable long userId, @RequestBody UserRoleChangeRequest request) {
        return ResponseEntity.ok(
                ApiResponse.success("권한을 변경했습니다.",userAdminService.changeUserRole(userId,request))
        );
    }


    @GetMapping("/admin/test")
    public void test(@AuthenticationPrincipal AuthUser authUser) {
        log.info("User ID: {}", authUser.getId());
        log.info("Email: {}", authUser.getEmail());
        log.info("Authorities: {}", authUser.getAuthorities());
    }
}
