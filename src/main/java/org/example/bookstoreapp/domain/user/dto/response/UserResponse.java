package org.example.bookstoreapp.domain.user.dto.response;

import lombok.Getter;
import org.example.bookstoreapp.domain.user.enums.UserRole;

@Getter
public class UserResponse {

    private final String name;
    private final String nickname;
    private final UserRole userRole;
    private final String email;

    public UserResponse(String name, String nickname, UserRole userRole, String email) {
        this.name = name;
        this.nickname = nickname;
        this.userRole = userRole;
        this.email = email;
    }
}
