package org.example.bookstoreapp.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class UserChangeNameAndPasswordRequest {

    @NotBlank
    private String nickname;
    @NotBlank
    private String oldPassword;
    @NotBlank
    private String newPassword;
}
