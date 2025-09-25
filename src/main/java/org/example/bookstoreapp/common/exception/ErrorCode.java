package org.example.bookstoreapp.common.exception;

// ErrorCode 인터페이스
public interface ErrorCode {
    int getStatus();         // HTTP 상태 코드
    String getMessage();     // 에러 메시지
}