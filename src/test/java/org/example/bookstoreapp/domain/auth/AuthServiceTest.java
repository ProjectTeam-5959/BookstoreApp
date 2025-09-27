package org.example.bookstoreapp.domain.auth;

import org.example.bookstoreapp.domain.auth.service.AuthService;
import org.example.bookstoreapp.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService userService;

    @Test
    void signup_성공한다() {}

    @Test
    void signup_이메일중복이면_DUPLICATE_EMAIL_예외() {}

    @Test
    void signup_닉네임중복이면_DUPLICATE_NICKNAME_예외() {}

    @Test
    void login_성공한다() {}

    @Test
    void login_이메일없으면_USER_NOT_FOUND_BY_EMAIL_예외() {}

    @Test
    void login_비밀번호틀리면_PASSWORD_MISMATCH_예외() {}

    @Test
    void withdraw_성공한다() {}

}
