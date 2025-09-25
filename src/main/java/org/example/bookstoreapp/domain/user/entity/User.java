package org.example.bookstoreapp.domain.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.bookstoreapp.common.entity.BaseEntity;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30, unique = true, nullable = false)
    @NotBlank
    private String nickname;

    @Column(length = 50, unique = true, nullable = false)
    @NotBlank
    private String email;

    @Column(length = 10, nullable = false)
    @NotBlank
    private UserRole userRole;

    @Column(length = 30, nullable = false)
    @NotBlank
    private String name;

    @Column(length = 255, nullable = false)
    private String password;

    protected User(String nickname, String email, UserRole userRole, String name, String password) {
        this.nickname = nickname;
        this.email = email;
        this.userRole = userRole;
        this.name = name;
        this.password = password;
    }

    public static User of(String nickname, String email, UserRole userRole, String name, String password) {
        return new User(nickname, email, userRole, name, password);
    }

    public void updateNicknameAndPassword(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;
    }

    public void updateRole(UserRole userRole) {
        this.userRole = userRole;
    }
}
