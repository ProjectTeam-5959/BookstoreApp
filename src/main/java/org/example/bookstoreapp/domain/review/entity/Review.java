package org.example.bookstoreapp.domain.review.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.bookstoreapp.common.entity.BaseEntity;
import org.example.bookstoreapp.domain.book.entity.Book;
import org.example.bookstoreapp.domain.review.dto.request.ReviewRequest;
import org.example.bookstoreapp.domain.user.entity.User;

@Entity
@Getter
@Builder
@Table(name = "reviews")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 300)
    private String content;

    // 유저와 연관 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 책와 연관 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    public void updateReview(ReviewRequest reviewRequest) {
        this.content = reviewRequest.getContent();
    }
}
