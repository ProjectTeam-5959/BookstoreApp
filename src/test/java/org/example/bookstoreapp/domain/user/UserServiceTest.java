package org.example.bookstoreapp.domain.user;

import org.example.bookstoreapp.common.exception.BusinessException;
import org.example.bookstoreapp.domain.auth.exception.AuthErrorCode;
import org.example.bookstoreapp.domain.user.dto.request.UserChangeNameAndPasswordRequest;
import org.example.bookstoreapp.domain.user.dto.response.UserResponse;
import org.example.bookstoreapp.domain.user.entity.User;
import org.example.bookstoreapp.domain.user.enums.UserRole;
import org.example.bookstoreapp.domain.user.repository.UserRepository;
import org.example.bookstoreapp.domain.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private final String EMAIL = "test123@eamil.com";
    private final String RAW_PASSWORD = "Password123!";
    private final String HASHED_PASSWORD = "hashedPassword";
    private final String NAME = "이름";
    private final String NICKNAME = "닉네임";
    private final Long USER_ID = 1L;

    @Test
    void User를_ID로_조회할_수_있다() {
        //given
        User user = User.of(NICKNAME,EMAIL,UserRole.ROLE_USER,NAME,RAW_PASSWORD);
        ReflectionTestUtils.setField(user, "id",USER_ID);
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        //when
        UserResponse userResponse = userService.getUser(USER_ID);

        //then
        assertThat(userResponse).isNotNull();
        assertThat(userResponse.getName()).isEqualTo(NAME);
        assertThat(userResponse.getNickname()).isEqualTo(NICKNAME);
        assertThat(userResponse.getUserRole()).isEqualTo(UserRole.ROLE_USER);
        assertThat(userResponse.getEmail()).isEqualTo(EMAIL);
        then(userRepository).should(times(1)).findById(anyLong());
    }

    @Test
    void 존재하지_않는_User를_조회_시_BusinessException을_던진다() {
        //given
        given(userRepository.findById(anyLong())).willReturn(Optional.empty());

        //when&then
        assertThrows(BusinessException.class,
                () -> userService.getUser(USER_ID),
                "user not found");
    }

    @Test
    void changeNicknameAndPassword_성공한다() {
        String oldPassword = "oldPassword1!";
        String newPassword = "newPassword1!";
        String newNickname = "newNickname";

        UserChangeNameAndPasswordRequest request = new UserChangeNameAndPasswordRequest(newNickname,oldPassword,newPassword);
        User user = User.of(NICKNAME,EMAIL,UserRole.ROLE_USER,NAME,HASHED_PASSWORD);

        given(userRepository.findById(USER_ID)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(eq(newPassword),eq(HASHED_PASSWORD))).willReturn(false);
        given(passwordEncoder.matches(eq(oldPassword), eq(HASHED_PASSWORD))).willReturn(true);
        given(passwordEncoder.encode(eq(newPassword))).willReturn("hashed_new_password");

        //when
        UserResponse userResponse = userService.changeNicknameAndPassword(USER_ID,request);

        //then
        assertThat(userResponse).isNotNull();
        assertThat(userResponse.getNickname()).isEqualTo(newNickname);
        assertThat(userResponse.getName()).isEqualTo(NAME);
        assertThat(userResponse.getUserRole()).isEqualTo(UserRole.ROLE_USER);
        assertThat(userResponse.getEmail()).isEqualTo(EMAIL);
        // 상태 검증: user 객체의 닉네임과 비밀번호가 실제로 업데이트되었는지 확인
        assertThat(user.getNickname()).isEqualTo(newNickname);
        assertThat(user.getPassword()).isEqualTo("hashed_new_password");

        then(userRepository).should(times(1)).findById(USER_ID);
        then(passwordEncoder).should(times(1)).encode(eq(newPassword));
    }

    @Test
    void validateNewPassword_유효_검사_성공() {
        //given
        String validPassword = "validPassword1!";
        UserChangeNameAndPasswordRequest request = new UserChangeNameAndPasswordRequest("닉네임", "old", validPassword);

        //when then
        assertThatCode(
                () -> UserService.validateNewPassword(request))
                        .doesNotThrowAnyException();
    }

    @Test
    void changeNicknameAndPassword_길이가_짧아_INVALID_PASSWORD_FORMAT_예외() {
        //given
        String invalidPassword = "aB1!";
        UserChangeNameAndPasswordRequest request = new UserChangeNameAndPasswordRequest("닉네임","old",invalidPassword);

        //when then
        assertThatThrownBy(()-> UserService.validateNewPassword(request))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", AuthErrorCode.INVALID_PASSWORD_FORMAT);
    }


    @Test
    void changeNicknameAndPassword_숫자_누락으로_실패() {
        //given
        String invalidPassword = "newPassword!";
        UserChangeNameAndPasswordRequest request = new UserChangeNameAndPasswordRequest("닉네임", "old", invalidPassword);

        //when then
        assertThatThrownBy(() -> UserService.validateNewPassword(request))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode",AuthErrorCode.INVALID_PASSWORD_FORMAT);
    }

    @Test
    void changeNicknameAndPassword_특수_문자_누락으로_실패() {
        //given
        String invalidPassword = "newPassword1";
        UserChangeNameAndPasswordRequest request = new UserChangeNameAndPasswordRequest("닉네임","old",invalidPassword);

        //when then
        assertThatThrownBy(() -> UserService.validateNewPassword(request))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode",AuthErrorCode.INVALID_PASSWORD_FORMAT);
    }

    @Test
    void changeNicknameAndPassword_새비밀번호가_기존과_같으면_INVALID_NEW_PASSWORD_예외() {
        //given
        String rawNewPassword = "newPassword1!";
        String dummyHashedPassword = "dummy_hashed_password1";

        User user = User.of("닉네임", EMAIL, UserRole.ROLE_USER, NAME, dummyHashedPassword);
        UserChangeNameAndPasswordRequest request = new UserChangeNameAndPasswordRequest("닉네임",HASHED_PASSWORD,rawNewPassword);

        given(userRepository.findById(USER_ID)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(eq(rawNewPassword), eq(dummyHashedPassword))).willReturn(true);
        //when then
        assertThatThrownBy(() -> userService.changeNicknameAndPassword(USER_ID,request))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode",AuthErrorCode.INVALID_NEW_PASSWORD);

        then(userRepository).should(times(1)).findById(USER_ID);
        then(passwordEncoder).should(times(1)).matches(eq(rawNewPassword),eq(user.getPassword()));
    }

    @Test
    void changeNicknameAndPassword_기존비밀번호가_틀리면_PASSWORD_MISMATCH_예외() {
        String oldPassword = "oldPassword1!";

        User user = User.of(NICKNAME,EMAIL,UserRole.ROLE_USER,NAME,HASHED_PASSWORD);
        UserChangeNameAndPasswordRequest request = new UserChangeNameAndPasswordRequest("닉네임",oldPassword,"newPassword1!");

        given(userRepository.findById(USER_ID)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(eq("newPassword1!"),eq(HASHED_PASSWORD))).willReturn(false);
        given(passwordEncoder.matches(eq(oldPassword),eq(HASHED_PASSWORD))).willReturn(false);

        //when then
        assertThatThrownBy(() -> userService.changeNicknameAndPassword(USER_ID,request))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode",AuthErrorCode.PASSWORD_MISMATCH);

        then(userRepository).should(times(1)).findById(USER_ID);
        then(passwordEncoder).should(times(1)).matches(eq("newPassword1!"),eq(HASHED_PASSWORD));
        then(passwordEncoder).should(times(1)).matches(eq(oldPassword),eq(HASHED_PASSWORD));
    }
}
