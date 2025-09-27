package org.example.bookstoreapp.domain.auth;

import org.example.bookstoreapp.common.config.JwtUtil;
import org.example.bookstoreapp.domain.auth.dto.request.SigninRequest;
import org.example.bookstoreapp.domain.auth.dto.request.SignupRequest;
import org.example.bookstoreapp.domain.auth.service.AuthService;
import org.example.bookstoreapp.domain.user.entity.User;
import org.example.bookstoreapp.domain.user.enums.UserRole;
import org.example.bookstoreapp.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;
    @InjectMocks
    private AuthService authService;

    private final String EMAIL = "test123@eamil.com";
    private final String RAW_PASSWORD = "Password123!";
    private final String HASED_PASSWORD = "hashedPassword";
    private final String NAME = "이름";
    private final String NICKNAME = "닉네임";
    private final Long USER_ID = 1L;
    private final String MOCK_JWT = "mocked.jwt.token";

    @Test
    void signup_성공() {
        //given
        SignupRequest request = new SignupRequest(EMAIL,RAW_PASSWORD,NAME,NICKNAME);
        User newUser = User.of(NICKNAME, EMAIL, UserRole.ROLE_USER, NAME, HASED_PASSWORD);
        ReflectionTestUtils.setField(newUser,"id",USER_ID);

        given(userRepository.existsByEmail(anyString())).willReturn(false);
        given(userRepository.existsByNickname(anyString())).willReturn(false);
        given(passwordEncoder.encode(RAW_PASSWORD)).willReturn(HASED_PASSWORD);
        given(userRepository.save(any(User.class))).willReturn(newUser);
        given(jwtUtil.createToken(anyLong(), anyString(), any(UserRole.class))).willReturn(MOCK_JWT);

        //when
        String token = authService.signup(request);

        //then
        assertThat(token).isEqualTo(MOCK_JWT);
        then(userRepository).should(times(1)).save(any(User.class));
        then(passwordEncoder).should(times(1)).encode(RAW_PASSWORD);
    }

    @Test
    void signup_이메일중복이면_DUPLICATE_EMAIL_예외() {}

    @Test
    void signup_닉네임중복이면_DUPLICATE_NICKNAME_예외() {}

    @Test
    void login_성공한다() {
        //given
        SigninRequest request = new SigninRequest(EMAIL,RAW_PASSWORD);
        User newUser = User.of(NICKNAME, EMAIL, UserRole.ROLE_USER, NAME, HASED_PASSWORD);
        ReflectionTestUtils.setField(newUser,"id",USER_ID);

        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(newUser));
        given(passwordEncoder.matches(RAW_PASSWORD, HASED_PASSWORD)).willReturn(true);
        given(jwtUtil.createToken(anyLong(), anyString(), any(UserRole.class))).willReturn(MOCK_JWT);

        //when
        String token = authService.login(request);

        // then
        assertThat(token).isEqualTo(MOCK_JWT);
        then(userRepository).should(times(1)).findByEmail(EMAIL);
        then(passwordEncoder).should(times(1)).matches(RAW_PASSWORD,HASED_PASSWORD);
        then(jwtUtil).should(times(1)).createToken(USER_ID, EMAIL, UserRole.ROLE_USER);
    }

    @Test
    void login_이메일없으면_USER_NOT_FOUND_BY_EMAIL_예외() {}

    @Test
    void login_비밀번호틀리면_PASSWORD_MISMATCH_예외() {}

    @Test
    void withdraw_성공() {}

}
