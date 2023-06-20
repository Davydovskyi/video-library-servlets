package edu.jcourse.service;

import edu.jcourse.dao.ReviewDao;
import edu.jcourse.dto.CreateReviewDto;
import edu.jcourse.dto.ReceiveMovieReviewDto;
import edu.jcourse.dto.ReceiveReviewDto;
import edu.jcourse.dto.ReceiveUserDto;
import edu.jcourse.entity.Movie;
import edu.jcourse.entity.Review;
import edu.jcourse.entity.User;
import edu.jcourse.exception.DAOException;
import edu.jcourse.exception.ServiceException;
import edu.jcourse.exception.ValidationException;
import edu.jcourse.mapper.impl.CreateReviewMapper;
import edu.jcourse.mapper.impl.ReviewMapper;
import edu.jcourse.service.impl.ReviewServiceImpl;
import edu.jcourse.util.CodeUtil;
import edu.jcourse.util.MessageUtil;
import edu.jcourse.validator.Error;
import edu.jcourse.validator.ValidationResult;
import edu.jcourse.validator.impl.CreateReviewValidator;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {
    @Mock
    private ReviewDao reviewDao;
    @Mock
    private ReviewMapper reviewMapper;
    @Mock
    private CreateReviewValidator createReviewValidator;
    @Mock
    private CreateReviewMapper createReviewMapper;
    @InjectMocks
    private ReviewServiceImpl reviewService;
    @Captor
    private ArgumentCaptor<Review> reviewCaptor;
    @Captor
    private ArgumentCaptor<CreateReviewDto> createReviewDtoCaptor;

    @SneakyThrows
    @Test
    void create() {
        doReturn(new ValidationResult()).when(createReviewValidator).validate(any());
        CreateReviewDto createReviewDto = buildCreateReviewDto(1L, "1L");

        Review review = buildReview(1L, 1L);
        doReturn(review).when(createReviewMapper).mapFrom(any());
        doReturn(review).when(reviewDao).save(any());

        Long actualResult = reviewService.create(createReviewDto);
        assertThat(actualResult).isEqualTo(review.getId());
        verify(createReviewValidator).validate(createReviewDtoCaptor.capture());
        assertThat(createReviewDtoCaptor.getValue()).isEqualTo(createReviewDto);
        verify(reviewDao).save(reviewCaptor.capture());
        assertThat(reviewCaptor.getValue()).isEqualTo(review);
        verify(createReviewMapper).mapFrom(createReviewDtoCaptor.capture());
        assertThat(createReviewDtoCaptor.getValue()).isEqualTo(createReviewDto);
    }

    @SneakyThrows
    @Test
    void createShouldThrowValidationExceptionIfReviewDtoIsInValid() {
        ValidationResult validationResult = new ValidationResult();
        validationResult.add(Error.of(CodeUtil.INVALID_REVIEW_SIZE_CODE, MessageUtil.REVIEW_INVALID_SIZE_MESSAGE));
        doReturn(validationResult).when(createReviewValidator).validate(any());

        assertThrowsExactly(ValidationException.class, () -> reviewService.create(any()));

        verify(createReviewValidator).validate(any());
        verifyNoInteractions(reviewDao, createReviewMapper);
    }

    @SneakyThrows
    @Test
    void createShouldThrowServiceExceptionIfDaoThrowsException() {
        doReturn(new ValidationResult()).when(createReviewValidator).validate(any());

        CreateReviewDto createReviewDto = buildCreateReviewDto(1L, "1L");
        Review review = buildReview(1L, 1L);
        doReturn(review).when(createReviewMapper).mapFrom(any());

        doThrow(DAOException.class).when(reviewDao).save(any());

        assertThrowsExactly(ServiceException.class, () -> reviewService.create(createReviewDto));
        verify(createReviewValidator).validate(createReviewDtoCaptor.capture());
        assertThat(createReviewDtoCaptor.getValue()).isEqualTo(createReviewDto);
        verify(reviewDao).save(reviewCaptor.capture());
        assertThat(reviewCaptor.getValue()).isEqualTo(review);
        verify(createReviewMapper).mapFrom(createReviewDtoCaptor.capture());
        assertThat(createReviewDtoCaptor.getValue()).isEqualTo(createReviewDto);
    }

    @SneakyThrows
    @Test
    void findAllByUserId() {
        List<Review> reviews = List.of(
                buildReview(1L, 1L),
                buildReview(2L, 1L)
        );

        doReturn(reviews).when(reviewDao).findAllByUserId(any());

        List<ReceiveReviewDto> expectedResult = List.of(
                buildReceiveReviewDto(1L, 1L),
                buildReceiveReviewDto(2L, 1L)
        );
        when(reviewMapper.mapFrom(any())).thenReturn(expectedResult.get(0))
                .thenReturn(expectedResult.get(1));

        List<ReceiveReviewDto> actualResult = reviewService.findAllByUserId(1L);

        assertThat(actualResult).isEqualTo(expectedResult);
        verify(reviewDao).findAllByUserId(1L);
        verify(reviewMapper, times(2)).mapFrom(any());
        verify(reviewMapper).mapFrom(reviews.get(0));
        verify(reviewMapper).mapFrom(reviews.get(1));
    }

    @SneakyThrows
    @Test
    void findAllByUserIdShouldThrowServiceExceptionIfDaoThrowsException() {
        doThrow(DAOException.class).when(reviewDao).findAllByUserId(any());

        assertThrowsExactly(ServiceException.class, () -> reviewService.findAllByUserId(1L));
        verify(reviewDao).findAllByUserId(1L);
        verifyNoInteractions(reviewMapper);
    }

    private CreateReviewDto buildCreateReviewDto(Long userId, String movieId) {
        return CreateReviewDto.builder()
                .userId(userId)
                .moveId(movieId)
                .reviewText("test")
                .rate("5")
                .build();
    }

    private Review buildReview(Long userId, Long movieId) {
        return Review.builder()
                .user(User.builder().id(userId).build())
                .movie(Movie.builder().id(movieId).build())
                .text("test")
                .rate((short) 5)
                .build();
    }

    private ReceiveReviewDto buildReceiveReviewDto(Long userId, Long movieId) {
        return ReceiveReviewDto.builder()
                .id(1L)
                .user(ReceiveUserDto.builder().id(userId).build())
                .movie(ReceiveMovieReviewDto.builder().movieId(movieId).build())
                .reviewText("test")
                .rate((short) 5)
                .build();
    }
}