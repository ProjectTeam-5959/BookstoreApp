package org.example.bookstoreapp.domain.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.bookstoreapp.domain.auth.dto.AuthUser;
import org.example.bookstoreapp.domain.user.enums.UserRole;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Secured(UserRole.Authority.ADMIN)
@RestController
public class TestController {
    @GetMapping("/admin/test")
    public void test(@AuthenticationPrincipal AuthUser authUser) {
        log.info("User ID: {}", authUser.getId());
        log.info("Email: {}", authUser.getEmail());
        log.info("Authorities: {}", authUser.getAuthorities());
    }
}

