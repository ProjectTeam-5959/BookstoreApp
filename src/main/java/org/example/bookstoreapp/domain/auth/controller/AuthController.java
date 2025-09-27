package org.example.bookstoreapp.domain.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.common.response.ApiResponse;
import org.example.bookstoreapp.domain.auth.dto.AuthUser;
import org.example.bookstoreapp.domain.auth.dto.request.SigninRequest;
import org.example.bookstoreapp.domain.auth.dto.request.SignupRequest;
import org.example.bookstoreapp.domain.auth.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/signup")
    public ResponseEntity<ApiResponse<String>> signup(@Valid @RequestBody SignupRequest signupRequest) {
        String bearerToken = authService.signup(signupRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success("회원가입이 완료되었습니다.", bearerToken));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponse<String>> login(
            @Valid @RequestBody SigninRequest signinRequest
    ){
        return ResponseEntity.ok(
                ApiResponse.success("로그인이 완료되었습니다.", authService.login(signinRequest))
        );
    }

    @DeleteMapping("/withdraw/me")
    public ResponseEntity<ApiResponse<Void>> withdraw(
            @AuthenticationPrincipal AuthUser authUser
    ) {
        authService.withdraw(authUser.getId());
        return ResponseEntity.ok(
                ApiResponse.success("회원탈퇴가 완료되었습니다.")
        );
    }
}