package org.example.bookstoreapp.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
public class SecurityConfigTest {

    @Autowired
    private SecurityFilterChain securityFilterChain;

    @Test
    void securityFilterChain_isNotNull() {
        assertNotNull(securityFilterChain, "SecurityFilterChain이 생성되어야 합니다.");
    }
}

/*SecurityConfig는 설정용 클래스라 순수하게 유닛테스트를 하기 어려움
현재 테스트 코드는 단순히 Spring Context에서 빈이 정상 등록되는지 확인만 함.

HTTP 보안 규칙(authorizeHttpRequest)까지 유닛테스는 거의 불가능
Spring Security는 실제 필터 체인 + MockMvc/웹 환경에서 테스트하는게 맞음.
그래서 보통은 통합테스트(Integration Test)방식  사용 -> MockMvc+실제 SecurityConfig  |  JWT,인증,권한테스트 가능.
*/