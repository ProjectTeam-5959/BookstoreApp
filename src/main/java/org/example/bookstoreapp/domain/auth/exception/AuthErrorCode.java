package org.example.bookstoreapp.domain.auth.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    DUPLICATE_EMAIL(HttpStatus.CONFLICT,  "이미 사용 중인 이메일입니다."),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT,  "이미 사용 중인 닉네임입니다."),

    USER_NOT_FOUND_BY_EMAIL(HttpStatus.NOT_FOUND,  "존재하지 않는 유저 email입니다."),
    USER_NOT_FOUND_BY_ID(HttpStatus.NOT_FOUND,  "존재하지 않는 유저 id입니다."),

    INVALID_USER_ROLE(HttpStatus.BAD_REQUEST,  "유효하지 않은 UserRole입니다."),

    PASSWORD_MISMATCH(HttpStatus.UNAUTHORIZED ,  "비밀번호가 일치하지 않습니다."),
    INVALID_NEW_PASSWORD(HttpStatus.BAD_REQUEST,"새 비밀번호는 기존 비밀번호와 같을 수 없습니다."),
    INVALID_PASSWORD_FORMAT(HttpStatus.BAD_REQUEST,"비밀번호는 대소문자 불문 영문, 숫자, 특수문자를 모두 포함해야 하며, 8~30자여야 합니다."),
    ;

    private final HttpStatus status;
    private final String message;

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
