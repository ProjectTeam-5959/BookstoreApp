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
        // 1. BusinessException에 담긴 상태 코드와 메시지를 추출
        HttpStatus httpStatus = ex.getStatus();
        String errorMessage = ex.getMessage(); // 서비스에서 설정한 상세 메시지

        // 2. ApiResponse.error()를 사용해 공통 오류 응답 객체 생성
        ApiResponse<?> errorResponse = ApiResponse.error(errorMessage);

        // 3. HTTP 상태 코드와 함께 응답 반환
        return new ResponseEntity<>(errorResponse, httpStatus);
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