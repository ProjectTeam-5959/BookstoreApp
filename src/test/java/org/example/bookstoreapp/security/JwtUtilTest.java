package org.example.bookstoreapp.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.example.bookstoreapp.common.security.JwtUtil;
import org.example.bookstoreapp.domain.user.enums.UserRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.ReflectionUtils;

import javax.crypto.SecretKey;
import java.lang.reflect.Field;
import java.security.Key;
import java.util.Objects;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class JwtUtilTest {

    @InjectMocks
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        Field secretKeyField = ReflectionUtils.findField(JwtUtil.class,"secretKey");
        Objects.requireNonNull(secretKeyField, "secretKey 필드를 찾을 수 없습니다");
        secretKeyField.setAccessible(true);
        ReflectionUtils.setField(secretKeyField,
                jwtUtil,
                "dGVzdC1zZWNyZXQta2V5LTEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5MA==");
        jwtUtil.init();
    }

    @Test
    @DisplayName("init() - key 초기화 성공")
    void init_success() {
        Field keyField = ReflectionUtils.findField(JwtUtil.class, "key");
        Objects.requireNonNull(keyField, "secretKey 필드를 찾을 수 없습니다");
        keyField.setAccessible(true);
        Key key = (Key) ReflectionUtils.getField(keyField, jwtUtil);
        Assertions.assertNotNull(key,"Key는 init() 호출 후에 초기화되어야 합니다.");
        jwtUtil.init();
    }

    @Test
    @DisplayName("createToken() - 성공적으로 JWT 생성")
    void createToken_success() {
        //given
        Long userId = 1L;
        String email = "test123@example.com";
        UserRole role = UserRole.ROLE_USER;

        //when
        String token = jwtUtil.createToken(userId,email,role);

        //then
        Assertions.assertTrue(token.startsWith("Bearer "), "토큰은 반드시 'Bearer '로 시작합니다.");

        //Bearer 제거 후 Claims 추출
        String jwt =token.replace("Bearer ","");
        Claims claims = Jwts.parser()
                .verifyWith((SecretKey) ReflectionTestUtils.getField(jwtUtil,"key"))
                .build()
                .parseSignedClaims(jwt)
                .getPayload();

        Assertions.assertEquals(String.valueOf(userId), claims.getSubject());
        Assertions.assertEquals(email, claims.get("email"));
        Assertions.assertEquals(role.getUserRole(),claims.get("userRole"));
    }

    @Test
    @DisplayName("substringToken() - Bearer 제거 성공")
    void subStringToken_success() {
        String tokenValue = "Bearer my.jwt.token";
        String result = jwtUtil.substringToken(tokenValue);
        Assertions.assertEquals("my.jwt.token",result);
    }

    @Test
    @DisplayName("substringToken() - Bearer 없으면 예외")
    void substringToken_fail() {
        String tokenValue = "my.jwt.token";
        Assertions.assertThrows(NullPointerException.class,
                () -> jwtUtil.substringToken(tokenValue));
    }

    @Test
    @DisplayName("extractClaims() - Claims 추출 성공")
    void extractClaims_success() {
        //given
        jwtUtil.init();
        Long userId = 1L;
        String email = "test123@example.com";

        String token = jwtUtil.createToken(userId, email, UserRole.ROLE_USER);
        String jwt = jwtUtil.substringToken(token);

        //when
        Claims claims = jwtUtil.extractClaims(jwt);

        //then
        Assertions.assertEquals(String.valueOf(userId),claims.getSubject());
        Assertions.assertEquals(email,claims.get("email"));
    }
}
