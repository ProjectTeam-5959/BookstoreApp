package org.example.bookstoreapp.common.exception;

import org.example.bookstoreapp.common.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    // '?'는 응답 데이터 T가 무엇이든 상관없음을 의미 (오류 응답이므로 data는 null)
    public ResponseEntity<ApiResponse<?>> handleBusinessException(BusinessException ex) {
        // 1. 예외 객체에서 ErrorCode를 꺼냄 (도메인별 에러 정보 포함)
        ErrorCode errorCode = ex.getErrorCode();

        // 2. ErrorCode의 메시지를 기반으로 공통 응답 객체 생성 (data는 null, 메시지만 포함)
        ApiResponse<?> errorResponse = ApiResponse.error(errorCode.getMessage());

        // 3. ErrorCode의 상태 코드를 기반으로 HTTP 응답 객체 생성 후 반환
        return new ResponseEntity<>(errorResponse, errorCode.getStatus());
    }

    // 2. 예상치 못한 서버 오류 처리 로직 (500 에러)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleSystemException(Exception ex) {
        // 보안상 내부 에러 메시지를 그대로 노출하지 않도록 일반적인 메시지를 사용
        String internalErrorMsg = "예상치 못한 서버 오류가 발생했습니다.";

        ApiResponse<?> errorResponse = ApiResponse.error(internalErrorMsg);

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR); // 500
    }
}