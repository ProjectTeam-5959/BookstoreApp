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
@Table(name = "books")                      // TODO: 추후 인덱스/유니크 제약 추가 예정
/**
 * 유니크 키 넣는 법 (1)
 * @Table(name = "books", uniqueConstraints = {
 *     @UniqueConstraint(name = "uk_isbn", columnNames = {"isbn"})
 * */
public class Book extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 양방향 관계 설정
    // OneToMany는 기본적으로 fetch = FetchType.LAZY
    @OneToMany(mappedBy = "book")
    private final List<BookContributor> bookContributors = new ArrayList<>();

    // 출판사명
    @Column(name = "publisher", nullable = false, length = 50)
    private String publisher;

    /**
     * 유니크 키 넣는 법 (2)
     *
     * @Column(name = "isbn", nullable = false, length = 20, unique = true)
     */
    @Column(name = "isbn", nullable = false, length = 20, unique = true)
    private String isbn;                        // 하이픈(-) 포함 허용

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 20)
    private BookCategory category;              // 도서 카테고리 (예: 소설, 에세이, 컴퓨터/IT, 인문, 경제/경영, 자기계발 등)

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "publication_date")
    private LocalDate publicationDate;      // ERD는 DATETIME이지만 LocalDate로 단순화

    @Column(name = "created_by", nullable = false, updatable = false)
    private Long createdBy;                 // 도서 ID (누가 생성했는지 추적용 - 관리자가 등록 하지만 확인용)

    // 생성자: 외부 직접 접근 금지
    @Builder
    private Book(String publisher, String isbn, BookCategory category, String title, LocalDate publicationDate, Long createdBy) {
        this.publisher = publisher;
        this.isbn = isbn;
        this.category = category;
        this.title = title;
        this.publicationDate = publicationDate;
        this.createdBy = createdBy;
    }

    /// todo ErrorCode 만들고, InvalidBookException 제거
//    // 정합성 검증
//    private void validate(String publisher, String isbn, String category, String title) {
//        if (publisher == null || publisher.isBlank()) {
//            throw new InvalidBookException("출판사는 필수값입니다.");
//        }
//        if (isbn == null || isbn.isBlank()) {
//            throw new InvalidBookException("ISBN은 필수값입니다.");
//        }
//        if (category == null || category.isBlank()) {
//            throw new InvalidBookException("카테고리는 필수값입니다.");
//        }
//        if (title == null || title.isBlank()) {
//            throw new InvalidBookException("제목은 필수값입니다.");
//        }
//    }

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