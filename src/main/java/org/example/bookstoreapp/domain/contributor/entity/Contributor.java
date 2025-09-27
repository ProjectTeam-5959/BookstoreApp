package org.example.bookstoreapp.domain.contributor.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.bookstoreapp.common.entity.BaseEntity;
import org.example.bookstoreapp.domain.bookcontributor.entity.BookContributor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "contributors")
public class Contributor extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 양방향 관계 설정
    // OneToMany는 기본적으로 fetch = FetchType.LAZY
    @OneToMany(mappedBy = "contributors")
    private final List<BookContributor> bookContributors = new ArrayList<>();

    // 기여자 이름
    @Column(name = "contributor_name", nullable = false, length = 50)
    private String name;

    // 도서 기여자 ID
    @Column(name = "created_by", nullable = false, updatable = false)
    private Long createdBy;

    @Builder
    public Contributor(String name, Long createdBy) {
        this.name = name;
        this.createdBy = createdBy;
    }
}
