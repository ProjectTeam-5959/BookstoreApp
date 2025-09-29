package org.example.bookstoreapp.domain.searchHistory.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.bookstoreapp.common.entity.BaseEntity;
import org.example.bookstoreapp.domain.book.entity.BookCategory;
import org.example.bookstoreapp.domain.user.entity.User;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "searchhistories")
public class SearchHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String title;

    @Column(length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private BookCategory category;

    // 유저와 연관관계 (추후 주석 해제)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")    // 로그인 안 한 경우 null 허용
    private User user;

    public SearchHistory(
            String title,
            String name,
            BookCategory category,
            User user
    ) {
        this.title = title;
        this.name = name;
        this.category = category;
        this.user = user;
    }

    // 라이프사이클 콜백
    // 신규 저장 시
    @PrePersist
    public void convertEmptyStringToNull() {
        if (title != null && title.isBlank()) {
            title = null;
        }
        if (name != null && name.isBlank()) {
            name = null;
        }
    }
}
