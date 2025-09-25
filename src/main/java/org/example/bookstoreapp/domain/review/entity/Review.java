package org.example.bookstoreapp.domain.review.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.bookstoreapp.common.entity.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "reviews")
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 300)
    private String content;

    // 유저와 연관 관계
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;

    // 책와 연관 관계
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "Book_id", nullable = false)
//    private Book book;

    // 유저와 책 값 추가 예정
    @Builder
    public Review(Long id, String content) {
        this.id = id;
        this.content = content;
    }
}
