package org.example.bookstoreapp.domain.book.repository;

import org.example.bookstoreapp.domain.book.entity.Book;
import org.example.bookstoreapp.domain.searchHistory.entity.SearchHistory;

import java.util.List;

public interface CustomBookRepository {

    List<Book> findTop10BySearchHistories(List<SearchHistory> histories);
}
