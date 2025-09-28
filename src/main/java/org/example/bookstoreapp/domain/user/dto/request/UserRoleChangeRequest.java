package org.example.bookstoreapp.domain.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.bookstoreapp.domain.user.enums.UserRole;

@Getter
@AllArgsConstructor
public class UserRoleChangeRequest {
    private UserRole role;
}