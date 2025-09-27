package org.example.bookstoreapp.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignupRequest {
    public static final String EMAIL_REGEXP = "^(?!\\.)[A-Za-z0-9._%+-]+(?<!\\.)@" +
            "[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?" +
            "(?:\\.[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?)*" +
            "\\.[A-Za-z]{2,}$";
    public static final String PASSWORD_REGEXP = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,30}$";

    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Email(message = "올바른 형식의 이메일 주소여야 합니다.")
    @Pattern(regexp = EMAIL_REGEXP, message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Size(min = 8, max = 30, message = "비밀번호는 8-30자 입니다.")
    @Pattern(
            regexp = PASSWORD_REGEXP,
            message = "비밀번호 형식이 올바르지 않습니다.")
    private String password;

    @NotBlank(message = "사용자 이름은 필수 입력값입니다.")
    @Size(min = 2, max = 30, message = "이름은 2~30자여야 합니다.")
    private String name;

    @NotBlank(message = "닉네임은 필수 입력값입니다.")
    @Size(min=2, max=30, message = "닉네임은 2~30자여야 합니다.")
    private String nickname;
}