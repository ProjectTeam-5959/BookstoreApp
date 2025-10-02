package org.example.bookstoreapp.domain.book.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.bookstoreapp.common.entity.BaseEntity;
import org.example.bookstoreapp.domain.bookcontributor.entity.BookContributor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // JPA 프록시용
@Table(name = "books")
public class Book extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 양방향 관계 설정
    @OneToMany(mappedBy = "book")
    private final List<BookContributor> bookContributors = new ArrayList<>();

    // 출판사명
    @Column(name = "publisher", nullable = false, length = 50)
    private String publisher;

    @Column(name = "isbn", nullable = false, length = 20, unique = true)
    private String isbn;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 20)
    private BookCategory category;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "publication_date")
    private LocalDate publicationDate;

    @Column(name = "created_by", nullable = false, updatable = false)
    private Long createdBy;

    @Builder
    private Book(String publisher, String isbn, BookCategory category, String title, LocalDate publicationDate, Long createdBy) {
        this.publisher = publisher;
        this.isbn = isbn;
        this.category = category;
        this.title = title;
        this.publicationDate = publicationDate;
        this.createdBy = createdBy;
    }

    // 팩토리 메서드
    public static Book create(String publisher,
                              String isbn,
                              BookCategory category,
                              String title,
                              LocalDate publicationDate,
                              Long createdBy
    ) {
        return new Book(
                publisher,
                isbn,
                category,
                title,
                publicationDate,
                createdBy
        );
    }

    // --- Domain 변경 메서드 ---
    public void changePublisher(String publisher) {
        this.publisher = publisher;
    }

    public void changeIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void changeCategory(BookCategory category) {
        this.category = category;
    }

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changePublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }
}