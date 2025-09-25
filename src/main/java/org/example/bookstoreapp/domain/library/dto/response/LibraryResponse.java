package org.example.bookstoreapp.domain.library.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class LibraryResponse {

    private String nickname;
    private List<LibraryBookResponse> books;
}
