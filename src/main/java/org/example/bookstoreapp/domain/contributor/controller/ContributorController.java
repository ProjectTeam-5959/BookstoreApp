package org.example.bookstoreapp.domain.contributor.controller;

import jakarta.validation.Valid;
import org.example.bookstoreapp.domain.contributor.dto.ContributorCreateRequest;
import org.example.bookstoreapp.domain.contributor.dto.ContributorResponse;
import org.example.bookstoreapp.domain.contributor.service.ContributorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api")
public class ContributorController {
    private final ContributorService service;
    public ContributorController(ContributorService service) { this.service = service; }


    @PostMapping("/admin/contributors")
    public ResponseEntity<ContributorResponse> create(@Valid @RequestBody ContributorCreateRequest req) {
        ContributorResponse created = service.create(req);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/api/admin/contributors").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(created);
    }
}
