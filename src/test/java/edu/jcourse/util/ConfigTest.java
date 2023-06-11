package edu.jcourse.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConfigTest {
    static Stream<Arguments> getPropertyArguments() {
        return Stream.of(
                Arguments.of("db.url", "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"),
                Arguments.of("db.login", "sa"),
                Arguments.of("db.password", ""),
                Arguments.of("db.driver", "org.h2.Driver"),
                Arguments.of("db.pool.size", "5"),
                Arguments.of("db.limit", "1000"),
                Arguments.of("image.base.url", "C:/DevTools/temp/")
        );
    }

    @ParameterizedTest
    @MethodSource("getPropertyArguments")
    void checkGetProperty(String key, String expectedResult) {
        String actualResult = Config.getProperty(key);

        assertEquals(expectedResult, actualResult);
    }
}