package org.example.bookstoreapp.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.common.exception.BusinessException;
import org.example.bookstoreapp.domain.auth.exception.AuthErrorCode;
import org.example.bookstoreapp.domain.user.dto.request.UserChangeNameAndPasswordRequest;
import org.example.bookstoreapp.domain.user.dto.response.UserResponse;
import org.example.bookstoreapp.domain.user.entity.User;
import org.example.bookstoreapp.domain.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public UserResponse getUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new BusinessException(AuthErrorCode.USER_NOT_FOUND_BY_ID)
        );
        return new UserResponse(user.getName(), user.getNickname(), user.getUserRole(), user.getEmail());
    }

    @Transactional
    public UserResponse changeNicknameAndPassword(long userId, UserChangeNameAndPasswordRequest request) {
        validateNewPassword(request);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(AuthErrorCode.USER_NOT_FOUND_BY_ID));

        if(passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new BusinessException(AuthErrorCode.INVALID_NEW_PASSWORD);
        }
        if(!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new BusinessException(AuthErrorCode.PASSWORD_MISMATCH);
        }

        user.updateNicknameAndPassword(request.getNickname(), passwordEncoder.encode(request.getNewPassword()));

        return new UserResponse(user.getName(), user.getNickname(), user.getUserRole(), user.getEmail());
    }

    private static final String PASSWORD_REGEXP = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,30}$";

    private static void validateNewPassword(UserChangeNameAndPasswordRequest userChangeNameAndPasswordRequest) {
        String newPassword = userChangeNameAndPasswordRequest.getNewPassword();

        // 정규 표현식으로 모든 조건(길이, 영문, 숫자, 특수문자)을 한 번에 검사
        if (!newPassword.matches(PASSWORD_REGEXP)) {
            throw new BusinessException(AuthErrorCode.INVALID_PASSWORD_FORMAT);
        }
    }
}
