package org.example.bookstoreapp.review.service;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {
    private final ReviewRepository reviewRepository;
}
