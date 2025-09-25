package org.example.bookstoreapp.review.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.review.service.ReviewService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ReviewController {
    private final ReviewService reviewService;
}
