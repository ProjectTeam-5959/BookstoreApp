package org.example.bookstoreapp.domain.library.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.bookstoreapp.domain.user.entity.User;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "libraries")
public class Library {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 유저 1명당 서재 1개
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    // 서재에 담긴 책들 (중간 테이블)
    @OneToMany(mappedBy = "library", cascade = CascadeType.ALL)
    // mappedBy = "library" : 양방향 매핑에서 외래키(FK) 관리하는 주인이 누구인지 지정
    // cascade = CascadeType.ALL : 라이브러리 저장/삭제 시, 그 안에 속한 LibraryBook도 같이 저장/삭제됨
    private List<LibraryBook> libraryBooks = new ArrayList<>();

    public Library(User user) {
        this.user = user;
    }

    // 정적 팩토리 메서드(엔티티 생성용)
    public static Library of(User user) {
        return new Library(user);
    }

    // 편의 메서드 : 내 서재에 책 추가
    public void addBook(LibraryBook libraryBook) {
        // 부모(library) 컬렉션에 추가
        libraryBooks.add(libraryBook);
    }
}

