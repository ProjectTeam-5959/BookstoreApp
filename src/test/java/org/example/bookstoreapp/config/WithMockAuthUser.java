package org.example.bookstoreapp.config;

import org.example.bookstoreapp.domain.user.enums.UserRole;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockUserSecurityContextFactory.class)
public @interface WithMockAuthUser {
    long userId() default 1L;
    String email() default "user123@example.com";
    UserRole role() default UserRole.ROLE_USER;
}
