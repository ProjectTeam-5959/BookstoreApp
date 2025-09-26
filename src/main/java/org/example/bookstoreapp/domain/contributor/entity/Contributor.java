package org.example.bookstoreapp.domain.contributor.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.bookstoreapp.common.entity.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "contributors")
public class Contributor extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 작가 이름
    @Column(name = "author_name", nullable = false, length = 50)
    private String name;

    @Builder
    public Contributor(String name) {
        this.name = name;
    }
}
