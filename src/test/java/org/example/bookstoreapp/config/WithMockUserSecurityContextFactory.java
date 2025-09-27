package org.example.bookstoreapp.config;

import org.example.bookstoreapp.common.config.JwtAuthenticationToken;
import org.example.bookstoreapp.domain.auth.dto.AuthUser;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockUserSecurityContextFactory implements WithSecurityContextFactory<WithMockAuthUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockAuthUser annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        AuthUser authUser = new AuthUser(
                annotation.userId(),
                annotation.email(),
                annotation.role()
        );
        JwtAuthenticationToken authentication = new JwtAuthenticationToken(authUser);

        context.setAuthentication(authentication);
        return context;
    }
}
