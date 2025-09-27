package org.example.bookstoreapp.domain.review.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ReviewRequest {

    @NotBlank(message = "리뷰 내용을 입력해주세요.")
    @Size(max = 300, message = "리뷰는 최대 300자까지 작성 가능합니다.")
    String content;
}
