package edu.jcourse.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class LocalDateFormatterTest {
    static Stream<Arguments> getValidationArguments() {
        return Stream.of(
                Arguments.of("2020-01-01", true),
                Arguments.of("2020-01-01 00:00:00", false),
                Arguments.of("01-01-2020", false),
                Arguments.of(" ", false),
                Arguments.of(null, false)
        );
    }

    @ParameterizedTest
    @MethodSource("getValidationArguments")
    void isValid(String date, boolean expectedResult) {
        boolean actualResult = LocalDateFormatter.isValid(date);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void parse() {
        String date = "2020-01-01";

        LocalDate actualResult = LocalDateFormatter.parse(date);

        assertThat(actualResult).isEqualTo(LocalDate.of(2020, 1, 1));
    }

    @Test
    void shouldThrowExceptionIfDateIsInvalid() {
        String date = "2020-01-01 00:00:00";

        assertThrowsExactly(DateTimeParseException.class, () -> LocalDateFormatter.parse(date));
    }
}