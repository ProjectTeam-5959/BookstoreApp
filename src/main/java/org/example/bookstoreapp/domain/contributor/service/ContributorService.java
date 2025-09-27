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

    // todo 점검 : DB 자체 문제 (커넥션 끊김 등) → 500
    @Transactional
    public ContributorResponse create(ContributorCreateRequest req, Long userId) {
        Contributor saved = repo.save(Contributor.builder()
                .name(req.getName())    // todo 해결할 것 : 이름 중복 체크 및 DB 저장 중 제약 조건 위반 (name unique 등) → DataIntegrityViolationException → 500
                .createdBy(userId)   // 기여자 ID 저장
                .build());

        return ContributorResponse.builder()
                .id(saved.getId())
                .name(saved.getName())
                .build();
    }
}
