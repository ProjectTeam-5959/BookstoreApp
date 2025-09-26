package org.example.bookstoreapp.domain.library.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class LibraryResponse {

    private List<LibraryBookResponse> books;
}
