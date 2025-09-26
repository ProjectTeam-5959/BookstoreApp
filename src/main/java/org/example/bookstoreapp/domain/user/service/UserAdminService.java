package org.example.bookstoreapp.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.common.exception.BusinessException;
import org.example.bookstoreapp.domain.auth.exception.AuthErrorCode;
import org.example.bookstoreapp.domain.user.dto.request.UserRoleChangeRequest;
import org.example.bookstoreapp.domain.user.dto.response.UserResponse;
import org.example.bookstoreapp.domain.user.entity.User;
import org.example.bookstoreapp.domain.user.enums.UserRole;
import org.example.bookstoreapp.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserAdminService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponse changeUserRole(long userId, UserRoleChangeRequest request) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new BusinessException(AuthErrorCode.USER_NOT_FOUND_BY_ID)
        );
        user.updateRole(UserRole.of(String.valueOf(request.getRole())));
        return new UserResponse(user.getName(), user.getNickname(), user.getUserRole(), user.getEmail());
    }
}
