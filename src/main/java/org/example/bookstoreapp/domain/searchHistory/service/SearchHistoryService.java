package org.example.bookstoreapp.domain.searchHistory.service;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.common.exception.BusinessException;
import org.example.bookstoreapp.domain.auth.dto.AuthUser;
import org.example.bookstoreapp.domain.book.entity.Book;
import org.example.bookstoreapp.domain.book.entity.BookCategory;
import org.example.bookstoreapp.domain.book.repository.BookRepository;
import org.example.bookstoreapp.domain.bookcontributor.entity.BookContributor;
import org.example.bookstoreapp.domain.contributor.dto.SearchContributorResponse;
import org.example.bookstoreapp.domain.searchHistory.dto.response.MySearchHistoryResponse;
import org.example.bookstoreapp.domain.searchHistory.dto.response.SearchResponse;
import org.example.bookstoreapp.domain.searchHistory.entity.SearchHistory;
import org.example.bookstoreapp.domain.searchHistory.exception.enums.SearchErrorCode;
import org.example.bookstoreapp.domain.searchHistory.repository.SearchHistoryRepository;
import org.example.bookstoreapp.domain.user.entity.User;
import org.example.bookstoreapp.domain.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchHistoryService {

    private final BookRepository bookRepository;
    private final SearchHistoryRepository searchHistoryRepository;
    private final UserRepository userRepository;

    // 키워드 검색
    @Transactional
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
        User user;

        if (authUser != null) {
            user = userRepository.findById(authUser.getId()).orElseThrow(
                    () -> new IllegalStateException("인증된 사용자를 찾을 수 없습니다."));

        } else {
            // 로그인 안 한 경우 -> 유저 없이 저장
            user = null;

        }
        SearchHistory searchHistory = new SearchHistory(
                title,
                name,
                category,
                user
        );
        searchHistoryRepository.save(searchHistory);

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

    // 나의 검색어 기록 조회
    @Transactional(readOnly = true)
    public Page<MySearchHistoryResponse> mySearchHistory(Long userId, Pageable pageable) {
        return searchHistoryRepository.findByUserId(userId, pageable)
                .map(MySearchHistoryResponse::from
                );
    }

    // 인기 키워드 title별 조회
    @Transactional(readOnly = true)
    public Page<SearchHistoryRepository.PopularKeywordCount> searchPopularTitles(Pageable pageable) {
        return searchHistoryRepository.findPopularTitles(pageable);
    }

    // 인기 키워드 name별 조회
    @Transactional(readOnly = true)
    public Page<SearchHistoryRepository.PopularKeywordCount> searchPopularNames(Pageable pageable) {
        return searchHistoryRepository.findPopularNames(pageable);
    }

    // 인기 키워드 category별 조회
    @Transactional(readOnly = true)
    public Page<SearchHistoryRepository.PopularKeywordCount> searchPopularCategories(Pageable pageable) {
        return searchHistoryRepository.findPopularCategories(pageable);
    }

    // 나의 검색어 기반 도서 Top10
    @Transactional(readOnly = true)
    public List<SearchResponse> searchTop10BooksByMySearchHistory(AuthUser authUser) {
        List<SearchHistory> histories = searchHistoryRepository.findAllByUserId(authUser.getId());
        List<SearchResponse> dtos = new ArrayList<>();

        for (SearchHistory searchHistory : histories) {
            String title = searchHistory.getTitle();
            String name = searchHistory.getName();
            BookCategory category = searchHistory.getCategory();

            List<Book> books = bookRepository.findTop10BooksByMySearchHistory(
                    title,
                    name,
                    category,
                    PageRequest.of(0, 10)
            );    // Top10 고정

            for (Book book : books) {
                List<SearchContributorResponse> searchContributors = new ArrayList<>();
                for (BookContributor bookContributor : book.getBookContributors()) {
                    searchContributors.add(SearchContributorResponse.from(bookContributor.getContributor(), bookContributor.getRole()));
                }

                dtos.add(new SearchResponse(
                        book.getId(),
                        book.getTitle(),
                        searchContributors,
                        book.getCategory(),
                        book.getCreatedAt()
                ));
            }
        }

        return dtos;

    }

    // 인기 검색어 기반 도서 Top10
//    @Transactional(readOnly = true)
}