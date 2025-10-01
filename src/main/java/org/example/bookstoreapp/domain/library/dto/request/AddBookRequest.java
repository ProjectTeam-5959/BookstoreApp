package org.example.bookstoreapp.domain.library.dto.request;

// 레코드 문법 사용
// 레코드 문법 : 필드 + 생성자 + getter + equals/hashCode + toString 모두 자동 생성
public record AddBookRequest(Long bookId) {}
