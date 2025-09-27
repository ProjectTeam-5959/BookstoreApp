package org.example.bookstoreapp.domain.user;

import org.example.bookstoreapp.domain.user.repository.UserRepository;
import org.example.bookstoreapp.domain.user.service.UserAdminService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserAdminServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserAdminService userService;

    @Test
    void User의_Role을_수정할_수_있다() {}
}
