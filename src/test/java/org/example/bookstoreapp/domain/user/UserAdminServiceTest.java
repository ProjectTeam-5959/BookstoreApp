package org.example.bookstoreapp.domain.user;

import org.example.bookstoreapp.domain.user.dto.request.UserRoleChangeRequest;
import org.example.bookstoreapp.domain.user.dto.response.UserResponse;
import org.example.bookstoreapp.domain.user.entity.User;
import org.example.bookstoreapp.domain.user.enums.UserRole;
import org.example.bookstoreapp.domain.user.repository.UserRepository;
import org.example.bookstoreapp.domain.user.service.UserAdminService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class UserAdminServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserAdminService userAdminService;

    @Test
    void User의_Role을_수정할_수_있다() {
        Long userId = 1L;
        User user = User.of("닉네임","email1@example.com",UserRole.ROLE_USER,"이름","Password1!");
        UserRoleChangeRequest request = new UserRoleChangeRequest(UserRole.ROLE_ADMIN);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        //when
        UserResponse userResponse = userAdminService.changeUserRole(userId,request);

        //then
        assertThat(userResponse).isNotNull();
        assertThat(userResponse.getName()).isEqualTo("이름");
        assertThat(userResponse.getNickname()).isEqualTo("닉네임");
        assertThat(userResponse.getUserRole()).isEqualTo(request.getRole());
        assertThat(userResponse.getEmail()).isEqualTo("email1@example.com");
        then(userRepository).should(times(1)).findById(userId);
        then(userRepository).shouldHaveNoMoreInteractions();
    }
}
