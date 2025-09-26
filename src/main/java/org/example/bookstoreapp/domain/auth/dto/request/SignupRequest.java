package org.example.bookstoreapp.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SignupRequest {

    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @Pattern(regexp = "^(?!\\.)[A-Za-z0-9._%+-]+(?<!\\.)@" +
            "[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?" +
            "(?:\\.[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?)*" +
            "\\.[A-Za-z]{2,}$")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Size(min = 8, max = 64)
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,64}$",
            message = "비밀번호는 대소문자 불문 영문, 숫자, 특수문자를 모두 포함해야 하며, 8~64자여야 합니다.")
    private String password;

    @Size(min = 2, max = 30, message = "이름은 2~30자여야 합니다.")
    @NotBlank(message = "사용자 이름은 필수 입력값입니다.")
    private String name;

    @Size(min=2, max=30, message = "닉네임은 2~30자여야 합니다.")
    @NotBlank(message = "닉네임은 필수 입력값입니다.")
    private String nickname;
}