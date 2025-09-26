package org.example.bookstoreapp.domain.bookcontributor.service;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.domain.book.entity.Book;
import org.example.bookstoreapp.domain.book.repository.BookRepository;
import org.example.bookstoreapp.domain.bookcontributor.dto.BookContributorCreateRequest;
import org.example.bookstoreapp.domain.bookcontributor.entity.BookContributor;
import org.example.bookstoreapp.domain.bookcontributor.repository.BookContributorRepository;
import org.example.bookstoreapp.domain.contributor.entity.Contributor;
import org.example.bookstoreapp.domain.contributor.repository.ContributorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookContributorService {

    private final BookRepository bookRepository;                        // 도서 레포지토리
    private final ContributorRepository contributorRepository;          // 기여자 레포지토리
    private final BookContributorRepository bookContributorRepository;  // 도서-기여자 레포지토리

    @Transactional
    public Long create(BookContributorCreateRequest req) {
        Book book = bookRepository.findById(req.getBookId())
                .orElseThrow(() -> new IllegalArgumentException("도서를 찾을 수 없습니다."));
        Contributor contributor = contributorRepository.findById(req.getAuthorId())
                .orElseThrow(() -> new IllegalArgumentException("기여자를 찾을 수 없습니다."));

        BookContributor bookContributor = BookContributor.builder()
                .book(book)
                .contributor(contributor)
                .role(req.getRole())
                .build();
        return bookContributorRepository.save(bookContributor).getId();
    }
}
