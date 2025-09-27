package org.example.bookstoreapp.domain.contributor.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.domain.auth.dto.AuthUser;
import org.example.bookstoreapp.domain.contributor.dto.ContributorCreateRequest;
import org.example.bookstoreapp.domain.contributor.dto.ContributorResponse;
import org.example.bookstoreapp.domain.contributor.service.ContributorService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class ContributorController {
    private final ContributorService service;

    @PostMapping("/admin/contributors")
    public ResponseEntity<ContributorResponse> create(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody ContributorCreateRequest req) {

        ContributorResponse created = service.create(
                req,
                authUser.getId()
        );

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/api/admin/contributors")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }
}
