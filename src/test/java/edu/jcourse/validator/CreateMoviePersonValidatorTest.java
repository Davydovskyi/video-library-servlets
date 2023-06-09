package edu.jcourse.validator;

import edu.jcourse.dto.CreateMoviePersonDto;
import edu.jcourse.entity.PersonRole;
import edu.jcourse.util.CodeUtil;
import edu.jcourse.validator.impl.CreateMoviePersonValidator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreateMoviePersonValidatorTest {

    private final CreateMoviePersonValidator createMoviePersonValidator = ValidatorProvider.getInstance().getCreateMoviePersonValidator();

    @Test
    void shouldPassValidation() {
        CreateMoviePersonDto createMoviePersonDTO = CreateMoviePersonDto.builder()
                .personRole(PersonRole.DIRECTOR.name())
                .build();

        ValidationResult actualResult = createMoviePersonValidator.validate(createMoviePersonDTO);

        assertTrue(actualResult.isValid());
    }

    @Test
    void shouldNotPassValidation() {
        CreateMoviePersonDto createMoviePersonDTO = CreateMoviePersonDto.builder()
                .personRole("invalid")
                .build();

        ValidationResult actualResult = createMoviePersonValidator.validate(createMoviePersonDTO);

        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(CodeUtil.INVALID_MOVIE_PERSONS_CODE);
    }
}