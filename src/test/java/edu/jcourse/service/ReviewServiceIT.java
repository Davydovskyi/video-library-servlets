package edu.jcourse.service;

import edu.jcourse.dto.CreateReviewDto;
import edu.jcourse.dto.ReceiveReviewDto;
import edu.jcourse.exception.ValidationException;
import edu.jcourse.integration.IntegrationTestBase;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

class ReviewServiceIT extends IntegrationTestBase {

    private final ReviewService reviewService = ServiceProvider.getInstance().getReviewService();

    @SneakyThrows
    @Test
    void create() {
        CreateReviewDto createReviewDto = buildCreateReviewDto("test");

        Long actualResult = reviewService.create(createReviewDto);

        assertThat(actualResult).isNotNull();
    }

    @SneakyThrows
    @Test
    void shouldThrowValidationExceptionIfReviewDtoIsInvalid() {
        CreateReviewDto createReviewDto = buildCreateReviewDto("");

        ValidationException actualResult = assertThrowsExactly(ValidationException.class, () -> reviewService.create(createReviewDto));
        assertThat(actualResult.getErrors()).hasSize(1);
    }

    @SneakyThrows
    @Test
    void findAllByUserId() {
        CreateReviewDto createReviewDto = buildCreateReviewDto("test");
        Long reviewId = reviewService.create(createReviewDto);

        List<ReceiveReviewDto> actualResult = reviewService.findAllByUserId(createReviewDto.userId());

        assertThat(actualResult).hasSize(1);
        assertThat(actualResult.get(0).id()).isEqualTo(reviewId);
    }

    @SneakyThrows
    @Test
    void shouldNotFindAllByUserIdIfReviewDoesNotExist() {
        reviewService.create(buildCreateReviewDto("test"));

        List<ReceiveReviewDto> actualResult = reviewService.findAllByUserId(34L);

        assertThat(actualResult).isEmpty();
    }

    private CreateReviewDto buildCreateReviewDto(String reviewText) {
        return CreateReviewDto.builder()
                .moveId("1")
                .userId(1L)
                .reviewText(reviewText)
                .rate("10")
                .build();
    }

}