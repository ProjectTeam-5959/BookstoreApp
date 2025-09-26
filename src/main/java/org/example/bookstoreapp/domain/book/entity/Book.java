package org.example.bookstoreapp.domain.book.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.bookstoreapp.common.entity.BaseEntity;
import org.example.bookstoreapp.domain.book.exception.InvalidBookException;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // JPA 프록시용
@Table(name = "books")                      // TODO: 추후 인덱스/유니크 제약 추가 예정
public class Book extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "publisher", nullable = false, length = 50)
    private String publisher;

    @Column(name = "isbn", nullable = false, length = 20)
    private String isbn;                    // 하이픈(-) 포함 허용

    @Column(name = "category", nullable = false, length = 20)
    private String category;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "publication_date")
    private LocalDate publicationDate;      // ERD는 DATETIME이지만 LocalDate로 단순화

    // 생성자: 외부 직접 접근 금지
    @Builder
    private Book(String publisher, String isbn, String category, String title, LocalDate publicationDate) {
        this.publisher = publisher;
        this.isbn = isbn;
        this.category = category;
        this.title = title;
        this.publicationDate = publicationDate;
    }

    // 정합성 검증
    private void validate(String publisher, String isbn, String category, String title) {
        if (publisher == null || publisher.isBlank()) {
            throw new InvalidBookException("출판사는 필수값입니다.");
        }
        if (isbn == null || isbn.isBlank()) {
            throw new InvalidBookException("ISBN은 필수값입니다.");
        }
        if (category == null || category.isBlank()) {
            throw new InvalidBookException("카테고리는 필수값입니다.");
        }
        if (title == null || title.isBlank()) {
            throw new InvalidBookException("제목은 필수값입니다.");
        }
    }

    // 팩토리 메서드
    public static Book create(String publisher,
                              String isbn,
                              String category,
                              String title,
                              LocalDate publicationDate
    ) {
        return new Book(
                publisher,
                isbn,
                category,
                title,
                publicationDate
        );
    }

    // --- Domain 변경 메서드 ---
    public void changePublisher(String publisher) {
        this.publisher = publisher;
    }

    public void changeIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void changeCategory(String category) {
        this.category = category;
    }

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changePublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }
}