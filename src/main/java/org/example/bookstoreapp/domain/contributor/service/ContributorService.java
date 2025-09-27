package org.example.bookstoreapp.domain.contributor.service;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.domain.contributor.dto.ContributorCreateRequest;
import org.example.bookstoreapp.domain.contributor.dto.ContributorResponse;
import org.example.bookstoreapp.domain.contributor.entity.Contributor;
import org.example.bookstoreapp.domain.contributor.repository.ContributorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContributorService {

    private final ContributorRepository repo;

    @Transactional
    public ContributorResponse create(ContributorCreateRequest req, Long userId) {
        Contributor saved = repo.save(Contributor.builder()
                .name(req.getName())
                .createdBy(userId)   // 기여자 ID 저장
                .build());

        return ContributorResponse.builder()
                .id(saved.getId())
                .name(saved.getName())
                .build();
    }
}
