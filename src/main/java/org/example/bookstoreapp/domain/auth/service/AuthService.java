package org.example.bookstoreapp.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.common.security.JwtUtil;
import org.example.bookstoreapp.common.exception.BusinessException;
import org.example.bookstoreapp.domain.auth.dto.request.SigninRequest;
import org.example.bookstoreapp.domain.auth.dto.request.SignupRequest;
import org.example.bookstoreapp.domain.auth.exception.AuthErrorCode;
import org.example.bookstoreapp.domain.user.entity.User;
import org.example.bookstoreapp.domain.user.enums.UserRole;
import org.example.bookstoreapp.domain.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public String signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(AuthErrorCode.DUPLICATE_EMAIL);
        }
        if (userRepository.existsByNickname(request.getNickname())) {
            throw new BusinessException(AuthErrorCode.DUPLICATE_NICKNAME);
        }
        User newUser = User.of(request.getNickname(), request.getEmail(), UserRole.ROLE_USER, request.getName(), passwordEncoder.encode(request.getPassword()));
        User savedUser = userRepository.save(newUser);

        return jwtUtil.createToken(savedUser.getId(), savedUser.getEmail(), savedUser.getUserRole());
    }

    @Transactional(readOnly = true)
    public String login(SigninRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException(AuthErrorCode.USER_NOT_FOUND_BY_EMAIL)
                );
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(AuthErrorCode.PASSWORD_MISMATCH);
        }
        return jwtUtil.createToken(user.getId(), user.getEmail(), user.getUserRole());
    }

    @Transactional
    public void withdraw(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new BusinessException(AuthErrorCode.USER_NOT_FOUND_BY_ID)
        );
        user.softDelete();
    }
}