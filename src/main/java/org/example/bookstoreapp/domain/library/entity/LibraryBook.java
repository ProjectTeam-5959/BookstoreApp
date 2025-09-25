package org.example.bookstoreapp.domain.library.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.bookstoreapp.domain.book.entity.Book;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// 컬럼자체에 unique = true  할 경우, 한 서재에는 책이 단 1권 밖에 못들어감
// 그러므로 FK 컬럼 단위로 unique 걸 수 없다.
// 복합 유니크 키 사용!
// 한 서재에 같은 책이 중복 되지 않도록 막음
// (library_id, book_id) 쌍이 유일해야 함 -> 테이블 레벨에서 설정
@Table(
        name = "librarybooks",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"library_id", "book_id"})
      }
)
public class LibraryBook {

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


    // 서재에 책 등록 시간
    @Column(nullable = false)
    private LocalDateTime addedAt;

    protected LibraryBook(Library library, Book book) {
        this.library = library;
        this.book = book;
        this.addedAt = LocalDateTime.now();
    }

    public static LibraryBook of(Library library, Book book) {
        return new LibraryBook(library, book);
    }

}
