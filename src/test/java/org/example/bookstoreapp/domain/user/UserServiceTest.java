package org.example.bookstoreapp.domain.user;

import org.example.bookstoreapp.common.exception.BusinessException;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void User를_ID로_조회할_수_있다() {
        //given
        String nickname = "nickname";
        String email = "test123@example.com";
        String name = "name";
        long userId = 1L;

        User user = User.of(nickname,email,UserRole.ROLE_USER,name,"password");
        ReflectionTestUtils.setField(user, "id",userId);
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        //when
        UserResponse userResponse = userService.getUser(userId);

        //then
        assertThat(userResponse).isNotNull();
        assertThat(userResponse.getName()).isEqualTo(name);
        assertThat(userResponse.getNickname()).isEqualTo(nickname);
        assertThat(userResponse.getUserRole()).isEqualTo(UserRole.ROLE_USER);
        assertThat(userResponse.getEmail()).isEqualTo(email);
    }

    @Test
    void 존재하지_않는_User를_조회_시_BusinessException을_던진다() {
        //given
        long userId = 1L;
        given(userRepository.findById(anyLong())).willReturn(Optional.empty());

        //when&then
        assertThrows(BusinessException.class,
                () -> userService.getUser(userId),
                "user not found");
    }

    @Test
    void changeNicknameAndPassword_성공한다() {}

    @Test
    void changeNicknameAndPassword_새비밀번호형식이_올바르지않으면_INVALID_PASSWORD_FORMAT_예외() {}

    @Test
    void changeNicknameAndPassword_새비밀번호가_기존과_같으면_INVALID_NEW_PASSWORD_예외() {}

    @Test
    void changeNicknameAndPassword_기존비밀번호가_틀리면_PASSWORD_MISMATCH_예외() {}
}
