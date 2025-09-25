package org.example.bookstoreapp.domain.book.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.bookstoreapp.common.entity.BaseEntity;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "book")
public class Book extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "publisher", nullable = false, length = 50)
    private String publisher;

    @Column(name = "isbn", nullable = false, length = 20)
    private String isbn;

    @Column(name = "category", nullable = false, length = 20)
    private String category;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "publication_date")
    private LocalDate publicationDate;

    public Book(String publisher, String isbn, String category, String title, LocalDate publicationDate) {
        this.publisher = publisher;
        this.isbn = isbn;
        this.category = category;
        this.title = title;
        this.publicationDate = publicationDate;
    }
}
