package org.example.bookstoreapp.domain.review;

import org.example.bookstoreapp.common.exception.BusinessException;
import org.example.bookstoreapp.domain.auth.dto.AuthUser;
import org.example.bookstoreapp.domain.book.entity.Book;
import org.example.bookstoreapp.domain.book.repository.BookRepository;
import org.example.bookstoreapp.domain.review.dto.request.ReviewRequest;
import org.example.bookstoreapp.domain.review.dto.response.ReviewResponse;
import org.example.bookstoreapp.domain.review.entity.Review;
import org.example.bookstoreapp.domain.review.exception.ReviewErrorCode;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.example.bookstoreapp.domain.book.entity.BookCategory.NOVEL;
import static org.example.bookstoreapp.domain.user.enums.UserRole.ROLE_ADMIN;
import static org.junit.jupiter.api.Assertions.*;
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

    @Test
    @DisplayName("리뷰 등록 실패 테스트 - 도서가 존재하지 않을 때")
    void createReview_fail_bookNotFound() {
        // given
        ReviewRequest reviewRequest = new ReviewRequest();
        ReflectionTestUtils.setField(reviewRequest, "content", "test-content");

        when(userRepository.findById(authUser.getId())).thenReturn(Optional.of(user)); // 유저는 존재
        when(bookRepository.findById(book.getId())).thenReturn(Optional.empty()); // 도서는 존재하지 않음.

        // when & then
        BusinessException exception = assertThrows(BusinessException.class, () ->
                reviewService.createReview(authUser, book.getId(), reviewRequest)
        );

        assertEquals(ReviewErrorCode.NOT_FOUND_BOOK, exception.getErrorCode());
    }

    @Test
    @DisplayName("리뷰 조회 성공 테스트 - 무한스크롤 페이징")
    void getReviews_success_infiniteScroll() {
        // given
        org.springframework.data.domain.Pageable pageable = Pageable.ofSize(10); // 하나의 페이지에 10개 씩 출력할 수 있다.
        List<Review> reviews = new ArrayList<>(); // 테스트용 리뷰 데이터를 담을 리스트

        // 데이터 10개 생성
        for (int i = 0; i < 11; i++) {
            Review review = Review.builder()
                    .id((long) i)
                    .content((i + 1) + "번 째 리뷰")
                    .user(user)
                    .book(book)
                    .build();
            reviews.add(review); // 생성할 때마다 리스트에 추가
        }

        /**
         참고! Slice는 내부적으로 limit(pageSize + 1) 로 쿼리를 날린다.
         예를 들어 pageSize=10이면 실제 SQL은 limit 11 로 조회
         */
        // SliceImpl의 첫 번째 인자는 실제 데이터 리스트, 두 번째 인자는 페이징 정보, 세 번째 인자는 다음 페이지가 존재하는 지 여부 확인 true면 다음 페이지가 존재한다는 것을 의미함.
        Slice<Review> reviewSlice = new SliceImpl<>(
                reviews.subList(0, 10), // 반환 값 10개로 설정
                pageable,
                reviews.size() > 10 // 데이터가 11개 이상이라면 hasNext가 true -> 다음 페이지가 존재한다.
        );

        // reviewRepository가 호출되면 만들어 둔 reviewSlice를 반환
        when(reviewRepository.findByUserId(authUser.getId(), pageable)).thenReturn(reviewSlice);

        // when
        // 실제 서비스 메서드를 호출, Review 에서 ReviewResponse로 변환된 결과를 반환
        Slice<ReviewResponse> result = reviewService.getReviews(authUser, pageable);

        // then
        //조회된 리뷰가 10개 인가?
        assertEquals(10, result.getContent().size());

        // 다음 페이지가 존재함을 확인
        assertTrue(result.hasNext());

        // 첫 번째 리뷰의 내용이 "1번 째 리뷰"라고 잘 전달 되었는 지 확인!
        assertEquals("1번 째 리뷰", result.getContent().get(0).getContent());
    }

    @Test
    @DisplayName("리뷰 조회 테스트 2 - 리뷰가 없는 경우")
    void getReviews_returnEmptyList() {
        // given
        Slice<Review> emptySlice = new SliceImpl<>(List.of()); // 빈 리스트
        when(reviewRepository.findByUserId(authUser.getId(), Pageable.unpaged())).thenReturn(emptySlice);

        // when
        Slice<ReviewResponse> result = reviewService.getReviews(authUser, Pageable.unpaged());

        // then
        // 리뷰가 없을 때 빈 리스트가 잘 반환되는지 검증
        assertEquals(0, result.getContent().size());
    }

    @Test
    @DisplayName("리뷰 수정 성공 테스트")
    void updateReview_success() {
        // given
        ReviewRequest reviewRequest = new ReviewRequest();
        ReflectionTestUtils.setField(reviewRequest, "content", "test-newContent");

        review = Review.builder()
                .id(1L)
                .content("oldContent")
                .user(user)
                .book(book)
                .build();

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        // when
        ReviewResponse reviewResponse = reviewService.updateReview(authUser, review.getId(), reviewRequest);

        // then
        assertEquals("test-newContent", reviewResponse.getContent());
    }

    @Test
    @DisplayName("리뷰 삭제 성공 테스트 - soft delete")
    void deleteReview_success() {
        // given
        review = Review.builder()
                .id(1L)
                .content("test-content")
                .user(user)
                .book(book)
                .build();

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        // when
        reviewService.deleteReview(authUser, review.getId());

        // then
        assertTrue(review.isDeleted()); // softDelete를 사용했기 때문에 삭제 되었다면 true
    }
}
