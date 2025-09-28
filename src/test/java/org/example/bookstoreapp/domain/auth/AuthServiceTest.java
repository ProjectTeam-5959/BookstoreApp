package org.example.bookstoreapp.domain.auth;

import org.example.bookstoreapp.common.config.JwtUtil;
import org.example.bookstoreapp.common.exception.BusinessException;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
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
    private final String HASHED_PASSWORD = "hashedPassword";
    private final String NAME = "이름";
    private final String NICKNAME = "닉네임";
    private final Long USER_ID = 1L;
    private final String MOCK_JWT = "mocked.jwt.token";

    @Test
    void signup_성공() {
        //given
        SignupRequest request = new SignupRequest(EMAIL,RAW_PASSWORD,NAME,NICKNAME);
        User newUser = User.of(NICKNAME, EMAIL, UserRole.ROLE_USER, NAME, HASHED_PASSWORD);
        ReflectionTestUtils.setField(newUser,"id",USER_ID);

        given(userRepository.existsByEmail(anyString())).willReturn(false);
        given(userRepository.existsByNickname(anyString())).willReturn(false);
        given(passwordEncoder.encode(RAW_PASSWORD)).willReturn(HASHED_PASSWORD);
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
    void signup_이메일중복이면_DUPLICATE_EMAIL_예외() {
        SignupRequest request = new SignupRequest(EMAIL,RAW_PASSWORD,NAME,NICKNAME);
        given(userRepository.existsByEmail(EMAIL)).willReturn(true);

        //when&then
        assertThrows(BusinessException.class,
                () -> authService.signup(request),
                "email already exists."
        );
    }

    @Test
    void signup_닉네임중복이면_DUPLICATE_NICKNAME_예외() {
        SignupRequest request = new SignupRequest(EMAIL,RAW_PASSWORD,NAME,NICKNAME);
        given(userRepository.existsByNickname(NICKNAME)).willReturn(true);

        //when&then
        assertThrows(BusinessException.class,
                () -> authService.signup(request),
                "nickname already exists."
        );
    }

    @Test
    void login_성공한다() {
        //given
        SigninRequest request = new SigninRequest(EMAIL,RAW_PASSWORD);
        User newUser = User.of(NICKNAME, EMAIL, UserRole.ROLE_USER, NAME, HASHED_PASSWORD);
        ReflectionTestUtils.setField(newUser,"id",USER_ID);

        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(newUser));
        given(passwordEncoder.matches(RAW_PASSWORD, HASHED_PASSWORD)).willReturn(true);
        given(jwtUtil.createToken(anyLong(), anyString(), any(UserRole.class))).willReturn(MOCK_JWT);

        //when
        String token = authService.login(request);

        // then
        assertThat(token).isEqualTo(MOCK_JWT);
        then(userRepository).should(times(1)).findByEmail(EMAIL);
        then(passwordEncoder).should(times(1)).matches(RAW_PASSWORD,HASHED_PASSWORD);
        then(jwtUtil).should(times(1)).createToken(USER_ID, EMAIL, UserRole.ROLE_USER);
    }

    @Test
    void login_이메일없으면_USER_NOT_FOUND_BY_EMAIL_예외() {
        SigninRequest request = new SigninRequest(EMAIL,RAW_PASSWORD);
        given(userRepository.findByEmail(EMAIL)).willReturn(Optional.empty());

        //when then
        assertThrows(BusinessException.class,
                () -> authService.login(request),
                "nickname already exists."
        );
    }

    @Test
    void login_비밀번호틀리면_PASSWORD_MISMATCH_예외() {
        SigninRequest request = new SigninRequest(EMAIL,RAW_PASSWORD);
        User user = User.of(NICKNAME,EMAIL,UserRole.ROLE_USER,NAME,RAW_PASSWORD);

        given(userRepository.findByEmail(EMAIL)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(anyString(),anyString())).willReturn(false);

        assertThrows(BusinessException.class,
            () -> authService.login(request),
            "password doesn't match"
        );
    }

    @Test
    void withdraw_성공() {
        //given
        User user = User.of(NICKNAME, EMAIL, UserRole.ROLE_USER, NAME, HASHED_PASSWORD);
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        //when
        authService.withdraw(USER_ID);

        //then
        then(userRepository).should(times(1)).findById(USER_ID);

        assertThat(user.isDeleted()).isTrue();
        //save 메서드가 호출되지 않았는지 확인 (Dirty Checking에 의존하기 때문에)
        then(userRepository).should(never()).save(any());
    }
}
