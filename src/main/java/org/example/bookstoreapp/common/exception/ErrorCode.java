package org.example.bookstoreapp.common.exception;

import org.springframework.http.HttpStatus;

// ErrorCode 인터페이스
public interface ErrorCode {
    HttpStatus getStatus();    // HTTP 상태 코드
    String getMessage();       // 에러 메시지
}