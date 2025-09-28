package org.example.bookstoreapp.domain.searchHistory.service;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.common.exception.BusinessException;
import org.example.bookstoreapp.domain.auth.dto.AuthUser;
import org.example.bookstoreapp.domain.book.entity.Book;
import org.example.bookstoreapp.domain.book.entity.BookCategory;
import org.example.bookstoreapp.domain.book.repository.BookRepository;
import org.example.bookstoreapp.domain.contributor.dto.SearchContributorResponse;
import org.example.bookstoreapp.domain.searchHistory.dto.response.SearchResponse;
import org.example.bookstoreapp.domain.searchHistory.entity.SearchHistory;
import org.example.bookstoreapp.domain.searchHistory.exception.enums.SearchErrorCode;
import org.example.bookstoreapp.domain.searchHistory.repository.SearchHistoryRepository;
import org.example.bookstoreapp.domain.user.entity.User;
import org.example.bookstoreapp.domain.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchHistoryService {

    private final BookRepository bookRepository;
    private final SearchHistoryRepository searchHistoryRepository;
    private final UserRepository userRepository;

    public Page<SearchResponse> searchKeyword(
            String title,
            String name,
            BookCategory category,
            Pageable pageable,
            AuthUser authUser
    ) {
        // 1. 키워드 검증(null 이거나 빈 문자열일 때)
        if ((title == null || title.isBlank()) &&
                (name == null || name.isBlank()) &&
                (category == null)) {
            throw new BusinessException(SearchErrorCode.KEYWORD_EMPTY);
        }

        // 2. 실제 도서 검색
        Page<Book> books = bookRepository.findBooksByKeyword(
                title,
                name,
                category,
                pageable
        );

        if (books.isEmpty()) {
            throw new BusinessException(SearchErrorCode.BOOK_NOT_FOUND);
        }

        // 3. 검색 기록 저장(비로그인 유저도 포함)
        if (authUser != null) {
            User user = userRepository.findById(authUser.getId()).orElseThrow(
                    () -> new IllegalStateException("인증된 사용자를 찾을 수 없습니다."));
            SearchHistory searchHistory = new SearchHistory(
                    title,
                    name,
                    category,
                    user
            );

            searchHistoryRepository.save(searchHistory);

        } else {
            // 로그인 안 한 경우 -> 유저 없이 저장
            SearchHistory searchHistory = new SearchHistory(
                    title,
                    name,
                    category,
                    null
            );

            searchHistoryRepository.save(searchHistory);

        }

        return books.map(book -> new SearchResponse(
                book.getId(),
                book.getTitle(),
                book.getBookContributors().stream()
                        .map(bookContributor -> SearchContributorResponse.builder()
                                .id(bookContributor.getContributor().getId())
                                .name(bookContributor.getContributor().getName())
                                .role(bookContributor.getRole())
                                .build()
                        ).toList(),
                book.getCategory(),
                book.getCreatedAt()
        ));
    }
}