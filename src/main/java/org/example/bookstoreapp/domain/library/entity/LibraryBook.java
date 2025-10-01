package org.example.bookstoreapp.domain.library.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.bookstoreapp.common.SoftDelete;
import org.example.bookstoreapp.domain.book.entity.Book;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "librarybooks",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"library_id", "book_id"})
      }
)
// delete = false 만 조회!
// @Where(clause = "deleted = false")는 지원 중단으로 사용 불가
@SQLRestriction("deleted = false")
public class LibraryBook extends SoftDelete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 서재 (FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "library_id", nullable = false)
    private Library library;

    // 책 (FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    // 서재에 책 등록된 날짜(시간)
    @Column(nullable = false)
    private LocalDateTime addedAt;

    protected LibraryBook(Library library, Book book) {
        this.library = library;
        this.book = book;
        this.addedAt = LocalDateTime.now();
    }

    // 정적 팩토리 메서드 (엔티티 생성용)
    public static LibraryBook of(Library library, Book book) {
        return new LibraryBook(library, book);
    }

    // restore 시점에 addedAt 갱신
    @Override
    public void restore() {
        super.restore();
        this.addedAt = LocalDateTime.now();
    }
}
