package org.example.bookstoreapp.domain.review;

import org.example.bookstoreapp.domain.auth.dto.AuthUser;
import org.example.bookstoreapp.domain.book.entity.Book;
import org.example.bookstoreapp.domain.book.repository.BookRepository;
import org.example.bookstoreapp.domain.review.dto.request.ReviewRequest;
import org.example.bookstoreapp.domain.review.dto.response.ReviewResponse;
import org.example.bookstoreapp.domain.review.entity.Review;
import org.example.bookstoreapp.domain.review.repository.ReviewRepository;
import org.example.bookstoreapp.domain.review.service.ReviewService;
import org.example.bookstoreapp.domain.user.entity.User;
import org.example.bookstoreapp.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Optional;

import static org.example.bookstoreapp.domain.book.entity.BookCategory.NOVEL;
import static org.example.bookstoreapp.domain.user.enums.UserRole.ROLE_ADMIN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // 단위 테스트
public class ReviewServiceTest {

    @Mock // 의존 객체를 가짜 객체로 생성
    private ReviewRepository reviewRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookRepository bookRepository;

    @InjectMocks // 해당 클래스에 @Mock 객체를 주입
    private ReviewService reviewService;

    private AuthUser authUser;
    private User user;
    private Book book;
    private Review review;

    @BeforeEach
    void setUp() {
        authUser = new AuthUser(1L, "admin@example.com", ROLE_ADMIN);

        user = User.of("관리자", "admin@example.com", ROLE_ADMIN, "관리자", "Admin1234!!");
        // authUser와 user의 id를 같은 값으로 설정!
        ReflectionTestUtils.setField(user, "id", 1L);

        book = Book.create("교보", "111111111111", NOVEL, "test", LocalDate.parse("2025-09-01"), 1L);
    }

    @Test
    @DisplayName("리뷰 등록 성공 테스트")
    void createReview_success() {
        // given
        ReviewRequest reviewRequest = new ReviewRequest();
        ReflectionTestUtils.setField(reviewRequest, "content", "test-content");

        Review savedReview = Review.builder()
                .id(1L)
                .content(reviewRequest.getContent())
                .user(user)
                .book(book)
                .build();

        when(userRepository.findById(authUser.getId())).thenReturn(Optional.of(user));
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(reviewRepository.save(any(Review.class))).thenReturn(savedReview);

        // when
        ReviewResponse reviewResponse = reviewService.createReview(authUser, book.getId(), reviewRequest);
        // then
        ArgumentCaptor<Review> captor = ArgumentCaptor.forClass(Review.class);
        verify(reviewRepository).save(captor.capture());
        assertEquals("test-content", reviewResponse.getContent()); // 리뷰 내용만 검증 할 수 있다...
        assertEquals(user, captor.getValue().getUser());
        assertEquals(book, captor.getValue().getBook());
    }


}
