package org.example.bookstoreapp.domain.searchHistory.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.bookstoreapp.domain.book.entity.BookCategory;
import org.example.bookstoreapp.domain.user.entity.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "searchhistories")
public class SearchHistory {

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
    @JoinColumn(name = "user_id")
    private User user;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

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
