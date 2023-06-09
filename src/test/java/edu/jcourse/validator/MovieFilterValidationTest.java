package edu.jcourse.validator;

import edu.jcourse.dto.MovieFilterDto;
import edu.jcourse.util.CodeUtil;
import edu.jcourse.validator.impl.MovieFilterValidation;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MovieFilterValidationTest {

    private final MovieFilterValidation movieFilterValidation = ValidatorProvider.getInstance().getMovieFilterValidation();

    @Test
    void shouldPassValidation() {
        MovieFilterDto movieFilterDTO = MovieFilterDto.builder().build();

        ValidationResult actualResult = movieFilterValidation.validate(movieFilterDTO);

        assertTrue(actualResult.isValid());
    }

    @Test
    void invalidReleaseYear() {
        MovieFilterDto movieFilterDTO = MovieFilterDto.builder()
                .releaseYear("2015a")
                .build();

        ValidationResult actualResult = movieFilterValidation.validate(movieFilterDTO);

        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(CodeUtil.INVALID_RELEASE_YEAR_CODE);
    }

    @Test
    void invalidGenre() {
        MovieFilterDto movieFilterDTO = MovieFilterDto.builder()
                .genre("test")
                .build();

        ValidationResult actualResult = movieFilterValidation.validate(movieFilterDTO);

        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(CodeUtil.INVALID_GENRE_CODE);
    }

    @Test
    void invalidReleaseYearAndGenre() {
        MovieFilterDto movieFilterDTO = MovieFilterDto.builder()
                .releaseYear("2015a")
                .genre("test")
                .build();

        ValidationResult actualResult = movieFilterValidation.validate(movieFilterDTO);

        assertThat(actualResult.getErrors()).hasSize(2);
        List<String> errorCodes = actualResult.getErrors().stream()
                .map(Error::getCode)
                .toList();
        assertThat(errorCodes).containsExactlyInAnyOrder(CodeUtil.INVALID_RELEASE_YEAR_CODE, CodeUtil.INVALID_GENRE_CODE);
    }

}