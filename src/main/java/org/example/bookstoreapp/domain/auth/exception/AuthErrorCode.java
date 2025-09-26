package org.example.bookstoreapp.domain.auth.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.common.exception.ErrorCode;
@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    NOT_FOUND_TOKEN(401,"인증토큰을 찾을 수 없습니다."),
    INVALID_TOKEN(400,"유효하지 않은 토큰 형식입니다."),
    EXPIRED_TOKEN(401,"토큰이 만료되었습니다."),
    DUPLICATE_EMAIL(409,  "이미 사용 중인 이메일입니다."),
    DUPLICATE_NICKNAME(409,  "이미 사용 중인 닉네임입니다."),

    USER_NOT_FOUND_BY_EMAIL(403,  "존재하지 않는 유저 email입니다."),
    USER_NOT_FOUND_BY_ID(403,  "존재하지 않는 유저 id입니다."),

    // 비밀번호 불일치 오류도 추가해 두면 좋습니다.
    PASSWORD_MISMATCH(403,  "비밀번호가 일치하지 않습니다."),
    INVALID_USER_ROLE(400,  "유효하지 않은 UserRole입니다."),
    ;
    private final int status;
    private final String message;

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
