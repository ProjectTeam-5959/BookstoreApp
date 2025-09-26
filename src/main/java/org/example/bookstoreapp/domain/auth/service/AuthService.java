package org.example.bookstoreapp.domain.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookstoreapp.common.config.JwtUtil;
import org.example.bookstoreapp.common.exception.BusinessException;
import org.example.bookstoreapp.domain.auth.dto.request.SigninRequest;
import org.example.bookstoreapp.domain.auth.dto.request.SignupRequest;
import org.example.bookstoreapp.domain.user.entity.User;
import org.example.bookstoreapp.domain.user.enums.UserRole;
import org.example.bookstoreapp.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public String signup(SignupRequest request) {
        log.info("Signup request 확인: {}", request);
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("이미 사용 중인 이메일입니다.", 409);
        }
        if (userRepository.existsByNickname(request.getNickname())) {
            throw new BusinessException("이미 사용 중인 닉네임입니다.", 409);
        }
        User newUser = User.of(request.getNickname(), request.getEmail(), UserRole.ROLE_USER, request.getName(), request.getPassword());
        User savedUser = userRepository.save(newUser);
        log.info("Signup request 확인2: {}", request);

        return jwtUtil.createToken(savedUser.getId(), savedUser.getEmail(), savedUser.getUserRole());
    }

    @Transactional(readOnly = true)
    public String login(SigninRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException("존재하지 않는 유저 email입니다.", 403)
                );
        return jwtUtil.createToken(user.getId(), user.getEmail(), user.getUserRole());
    }

    @Transactional
    public void withdraw(Long userId) {
        userRepository.findById(userId).orElseThrow(
                () -> new BusinessException("존재하지 않는 유저 id입니다", 403)
        );
        userRepository.deleteById(userId);
    }
}