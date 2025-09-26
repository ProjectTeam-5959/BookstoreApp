package org.example.bookstoreapp.domain.contributor.service;

import org.example.bookstoreapp.domain.contributor.dto.ContributorCreateRequest;
import org.example.bookstoreapp.domain.contributor.dto.ContributorResponse;
import org.example.bookstoreapp.domain.contributor.entity.Contributor;
import org.example.bookstoreapp.domain.contributor.repository.ContributorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContributorService {
    private final ContributorRepository repo;
    public ContributorService(ContributorRepository repo) { this.repo = repo; }


    @Transactional
    public ContributorResponse create(ContributorCreateRequest req) {
        Contributor saved = repo.save(Contributor.builder()
                .name(req.getName())
                .build());
        return ContributorResponse.builder()
                .id(saved.getId())
                .name(saved.getName())
                .build();
    }
}
