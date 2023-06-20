package edu.jcourse.validator;

import edu.jcourse.dao.ReviewDao;
import edu.jcourse.dto.CreateReviewDto;
import edu.jcourse.entity.Review;
import edu.jcourse.exception.DAOException;
import edu.jcourse.exception.ServiceException;
import edu.jcourse.util.CodeUtil;
import edu.jcourse.validator.impl.CreateReviewValidator;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateReviewValidatorTest {
    @Mock
    private ReviewDao reviewDAO;
    @InjectMocks
    private CreateReviewValidator createReviewValidator;

    static Stream<Arguments> getRateArguments() {
        return Stream.of(
                Arguments.of("0"),
                Arguments.of(" "),
                Arguments.of("11")
        );
    }

    @SneakyThrows
    @Test
    void shouldPassValidation() {
        CreateReviewDto createReviewDTO = CreateReviewDto.builder()
                .userId(1L)
                .moveId("1")
                .reviewText("test")
                .rate("4")
                .build();

        doReturn(Optional.empty()).when(reviewDAO)
                .findByUserIdAndMovieId(any(), any());

        ValidationResult actualResult = createReviewValidator.validate(createReviewDTO);

        assertTrue(actualResult.isValid());
        verify(reviewDAO).findByUserIdAndMovieId(createReviewDTO.userId(), Long.parseLong(createReviewDTO.moveId()));
    }

    @SneakyThrows
    @Test
    void shouldNotPassValidationIfReviewAlreadyExists() {
        CreateReviewDto createReviewDTO = CreateReviewDto.builder()
                .userId(1L)
                .moveId("1")
                .reviewText("test")
                .rate("4")
                .build();

        doReturn(Optional.of(Review.builder().build())).when(reviewDAO)
                .findByUserIdAndMovieId(any(), any());

        ValidationResult actualResult = createReviewValidator.validate(createReviewDTO);

        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(CodeUtil.EXIST_REVIEW_CODE);
        verify(reviewDAO).findByUserIdAndMovieId(createReviewDTO.userId(), Long.parseLong(createReviewDTO.moveId()));
    }

    @SneakyThrows
    @Test
    void shouldThrowServiceExceptionIfDaoThrowsException() {
        CreateReviewDto createReviewDTO = CreateReviewDto.builder()
                .userId(1L)
                .moveId("1")
                .reviewText("test")
                .rate("4")
                .build();

        doThrow(DAOException.class).when(reviewDAO).findByUserIdAndMovieId(any(), any());

        assertThrowsExactly(ServiceException.class, () -> createReviewValidator.validate(createReviewDTO));
        verify(reviewDAO).findByUserIdAndMovieId(createReviewDTO.userId(), Long.parseLong(createReviewDTO.moveId()));
    }

    @SneakyThrows
    @Test
    void invalidReviewText() {
        CreateReviewDto createReviewDTO = CreateReviewDto.builder()
                .userId(1L)
                .moveId("1")
                .reviewText("")
                .rate("4")
                .build();

        ValidationResult actualResult = createReviewValidator.validate(createReviewDTO);

        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(CodeUtil.INVALID_REVIEW_CODE);
        verifyNoInteractions(reviewDAO);
    }

    @SneakyThrows
    @Test
    void invalidReviewTextSize() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(257);
        CreateReviewDto createReviewDTO = CreateReviewDto.builder()
                .userId(1L)
                .moveId("1")
                .reviewText(stringBuilder.toString())
                .rate("4")
                .build();

        ValidationResult actualResult = createReviewValidator.validate(createReviewDTO);

        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(CodeUtil.INVALID_REVIEW_SIZE_CODE);
        verifyNoInteractions(reviewDAO);
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("getRateArguments")
    void invalidRate(String rate) {
        CreateReviewDto createReviewDTO = CreateReviewDto.builder()
                .userId(1L)
                .moveId("1")
                .reviewText("test")
                .rate(rate)
                .build();

        ValidationResult actualResult = createReviewValidator.validate(createReviewDTO);

        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(CodeUtil.INVALID_RATE_CODE);
        verifyNoInteractions(reviewDAO);
    }
}