package org.example.bookstoreapp.domain.auth.config;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.domain.user.entity.User;
import org.example.bookstoreapp.domain.user.enums.UserRole;
import org.example.bookstoreapp.domain.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner createAdmin() {
        return args -> {
            if(userRepository.findByEmail("admin@example.com").isEmpty()) {
                String encodedPassword = passwordEncoder.encode("Admin1234!!");
                User admin = User.of("관리자","admin@example.com", UserRole.ROLE_ADMIN, "관리자", encodedPassword);
                userRepository.save(admin);
                System.out.println("초기 관리자 계정 생성완료");
            }
        };
    }
}
