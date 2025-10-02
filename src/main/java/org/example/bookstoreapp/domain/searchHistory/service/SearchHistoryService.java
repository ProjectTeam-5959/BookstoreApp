package org.example.bookstoreapp.domain.searchHistory.service;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.common.exception.BusinessException;
import org.example.bookstoreapp.domain.auth.dto.AuthUser;
import org.example.bookstoreapp.domain.book.entity.Book;
import org.example.bookstoreapp.domain.book.entity.BookCategory;
import org.example.bookstoreapp.domain.book.repository.BookRepository;
import org.example.bookstoreapp.domain.contributor.dto.SearchContributorResponse;
import org.example.bookstoreapp.domain.searchHistory.dto.response.MySearchHistoryResponse;
import org.example.bookstoreapp.domain.searchHistory.dto.response.SearchResponse;
import org.example.bookstoreapp.domain.searchHistory.entity.SearchHistory;
import org.example.bookstoreapp.domain.searchHistory.exception.enums.SearchErrorCode;
import org.example.bookstoreapp.domain.searchHistory.repository.SearchHistoryRepository;
import org.example.bookstoreapp.domain.user.entity.User;
import org.example.bookstoreapp.domain.user.repository.UserRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
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
        if ((title == null || title.isBlank()) &&
                (name == null || name.isBlank()) &&
                (category == null)) {
            throw new BusinessException(SearchErrorCode.KEYWORD_EMPTY);
        }

        Page<Book> books = bookRepository.findBooksByKeyword(
                title,
                name,
                category,
                pageable
        );

        if (books.isEmpty()) {
            throw new BusinessException(SearchErrorCode.BOOK_NOT_FOUND);
        }

        User user;

        if (authUser != null) {
            user = userRepository.findById(authUser.getId()).orElseThrow(
                    () -> new BusinessException(SearchErrorCode.UNAUTHORIZED));

        } else {
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
                        .map(bookContributor -> SearchContributorResponse.from(
                                bookContributor.getContributor(),
                                bookContributor.getRole()
                        )).toList(),
                book.getCategory(),
                book.getCreatedAt()
        ));
    }

    // 나의 검색어 기록 조회
    @Transactional(readOnly = true)
    public Page<MySearchHistoryResponse> mySearchHistory(AuthUser authUser, Pageable pageable) {
        return searchHistoryRepository.findByUserId(authUser.getId(), pageable)
                .map(searchHistory -> MySearchHistoryResponse.from(searchHistory));
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
    public List<SearchResponse> searchTop10BooksByMySearchHistoryV1(AuthUser authUser) {

        List<SearchHistory> histories = searchHistoryRepository.findAllByUserId(authUser.getId());

        if (histories.isEmpty()) {
            return new ArrayList<>();
        }

        List<Book> books = bookRepository.findTop10BySearchHistories(histories);

        if (books.isEmpty()) {
            return new ArrayList<>();
        }

        List<SearchResponse> dtos = new ArrayList<>();
        for (Book book : books) {
            dtos.add(SearchResponse.from(book));
        }

        return dtos;
    }

    // 나의 검색어 기반 도서 Top10
    // 캐싱 적용
    @Transactional(readOnly = true)
    @Cacheable(value = "top10BooksByHistory", key = "#authUser.id")
    public List<SearchResponse> searchTop10BooksByMySearchHistoryV2(AuthUser authUser) {

        List<SearchHistory> histories = searchHistoryRepository.findAllByUserId(authUser.getId());

        if (histories.isEmpty()) {
            return new ArrayList<>();
        }

        List<Book> books = bookRepository.findTop10BySearchHistories(histories);

        if (books.isEmpty()) {
            return new ArrayList<>();
        }

        List<SearchResponse> dtos = new ArrayList<>();
        for (Book book : books) {
            dtos.add(SearchResponse.from(book));
        }

        return dtos;
    }
}
