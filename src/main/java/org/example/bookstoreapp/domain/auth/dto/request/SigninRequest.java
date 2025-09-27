package org.example.bookstoreapp.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SigninRequest {

    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Email
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    private String password;
}