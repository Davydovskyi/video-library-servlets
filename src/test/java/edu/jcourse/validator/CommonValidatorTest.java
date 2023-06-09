package edu.jcourse.validator;

import edu.jcourse.entity.Genre;
import edu.jcourse.util.CodeUtil;
import edu.jcourse.validator.impl.CommonValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class CommonValidatorTest {

    @Spy
    private ValidationResult validationResult;

    static Stream<Arguments> getNullOrEmptyArguments() {
        return Stream.of(
                Arguments.of(null, true),
                Arguments.of("", true),
                Arguments.of("  ", true),
                Arguments.of(" dummy", false),
                Arguments.of("dummy", false)
        );
    }

    static Stream<Arguments> getEmailValidationArguments() {
        return Stream.of(
                Arguments.of(" "),
                Arguments.of("invalid"),
                Arguments.of("@example.com"),
                Arguments.of("e.example.com")
        );
    }

    static Stream<Arguments> getPasswordValidationArguments() {
        return Stream.of(
                Arguments.of(" "),
                Arguments.of("invalid"),
                Arguments.of("12344")
        );
    }

    static Stream<Arguments> getReleaseYearValidationArguments() {
        return Stream.of(
                Arguments.of(" "),
                Arguments.of("invalid"),
                Arguments.of("1890"),
                Arguments.of(String.valueOf(LocalDate.now().plusYears(1).getYear()))
        );
    }

    @ParameterizedTest
    @MethodSource("getNullOrEmptyArguments")
    void isNullOrEmpty(String object, boolean expectedResult) {
        boolean actualResult = CommonValidator.isNullOrEmpty(object);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void genreValidationSuccess() {
        CommonValidator.genreValidation(validationResult, Genre.ADVENTURE.name());

        assertTrue(validationResult.isValid());
    }

    @Test
    void genreValidationFailure() {
        CommonValidator.genreValidation(validationResult, "invalid");

        assertThat(validationResult.getErrors()).hasSize(1);
        assertThat(validationResult.getErrors().get(0).getCode()).isEqualTo(CodeUtil.INVALID_GENRE_CODE);
    }

    @Test
    void nameValidationSuccess() {
        CommonValidator.nameValidation(validationResult, "name");

        assertTrue(validationResult.isValid());
    }

    @Test
    void nameValidationFailure() {
        CommonValidator.nameValidation(validationResult, null);

        assertThat(validationResult.getErrors()).hasSize(1);
        assertThat(validationResult.getErrors().get(0).getCode()).isEqualTo(CodeUtil.INVALID_NAME_CODE);
    }

    @Test
    void birthdayValidationSuccess() {
        CommonValidator.birthdayValidation(validationResult, "1990-01-01");

        assertTrue(validationResult.isValid());
    }

    @Test
    void birthdayValidationFailureIfBirthdayIsInvalid() {
        CommonValidator.birthdayValidation(validationResult, "2000-01-01 54");

        assertThat(validationResult.getErrors()).hasSize(1);
        assertThat(validationResult.getErrors().get(0).getCode()).isEqualTo(CodeUtil.INVALID_BIRTHDAY_CODE);
    }

    @Test
    void birthdayValidationFailureIfBirthdayIsImpossible() {
        String date = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        CommonValidator.birthdayValidation(validationResult, date);

        assertThat(validationResult.getErrors()).hasSize(1);
        assertThat(validationResult.getErrors().get(0).getCode()).isEqualTo(CodeUtil.INVALID_BIRTHDAY_CODE);
    }

    @Test
    void emailValidationSuccess() {
        CommonValidator.emailValidation(validationResult, "ychag@example.com");

        assertTrue(validationResult.isValid());
    }

    @ParameterizedTest
    @MethodSource("getEmailValidationArguments")
    void emailValidationFailureIfEmailIsInvalid(String email) {
        CommonValidator.emailValidation(validationResult, email);

        assertThat(validationResult.getErrors()).hasSize(1);
        assertThat(validationResult.getErrors().get(0).getCode()).isEqualTo(CodeUtil.INVALID_EMAIL_CODE);
    }

    @Test
    void passwordValidationSuccess() {
        CommonValidator.passwordValidation(validationResult, "password");

        assertTrue(validationResult.isValid());
    }

    @ParameterizedTest
    @MethodSource("getPasswordValidationArguments")
    void passwordValidationFailureIfPasswordIsInvalid(String password) {
        CommonValidator.passwordValidation(validationResult, password);

        assertThat(validationResult.getErrors()).hasSize(1);
        assertThat(validationResult.getErrors().get(0).getCode()).isEqualTo(CodeUtil.INVALID_PASSWORD_CODE);
    }

    @Test
    void releaseYearValidationSuccess() {
        CommonValidator.releaseYearValidation(validationResult, "2020");

        assertTrue(validationResult.isValid());
    }

    @ParameterizedTest
    @MethodSource("getReleaseYearValidationArguments")
    void releaseYearValidationFailureIfReleaseYearIsInvalid(String releaseYear) {
        CommonValidator.releaseYearValidation(validationResult, releaseYear);

        assertThat(validationResult.getErrors()).hasSize(1);
        assertThat(validationResult.getErrors().get(0).getCode()).isEqualTo(CodeUtil.INVALID_RELEASE_YEAR_CODE);
    }
}