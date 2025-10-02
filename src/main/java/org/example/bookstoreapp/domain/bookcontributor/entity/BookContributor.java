package org.example.bookstoreapp.domain.bookcontributor.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.bookstoreapp.common.entity.BaseEntity;
import org.example.bookstoreapp.domain.book.entity.Book;
import org.example.bookstoreapp.domain.contributor.entity.Contributor;
import org.example.bookstoreapp.domain.contributor.entity.ContributorRole;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "bookcontributors")
public class BookContributor extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "contributor_id", nullable = false)
    private Contributor contributor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private ContributorRole role;

    @Builder
    public BookContributor(Book book, Contributor contributor, ContributorRole role) {
        this.book = book;
        this.contributor = contributor;
        this.role = role;
    }
}
