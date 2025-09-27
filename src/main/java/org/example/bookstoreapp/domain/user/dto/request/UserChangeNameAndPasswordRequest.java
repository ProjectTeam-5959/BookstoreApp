package org.example.bookstoreapp.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserChangeNameAndPasswordRequest {

    @NotBlank
    private String nickname;
    @NotBlank
    private String oldPassword;
    @NotBlank
    private String newPassword;
}
