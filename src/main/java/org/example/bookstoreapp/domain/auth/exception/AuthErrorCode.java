package org.example.bookstoreapp.domain.auth.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    NOT_FOUND_TOKEN(HttpStatus.UNAUTHORIZED,"인증토큰을 찾을 수 없습니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST,"유효하지 않은 토큰 형식입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED,"토큰이 만료되었습니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT,  "이미 사용 중인 이메일입니다."),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT,  "이미 사용 중인 닉네임입니다."),

    USER_NOT_FOUND_BY_EMAIL(HttpStatus.FORBIDDEN,  "존재하지 않는 유저 email입니다."),
    USER_NOT_FOUND_BY_ID(HttpStatus.FORBIDDEN,  "존재하지 않는 유저 id입니다."),

    // 비밀번호 불일치 오류도 추가해 두면 좋습니다.
    PASSWORD_MISMATCH(HttpStatus.FORBIDDEN  ,  "비밀번호가 일치하지 않습니다."),
    INVALID_USER_ROLE(HttpStatus.BAD_REQUEST,  "유효하지 않은 UserRole입니다."),
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
