package edu.jcourse.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JSPHelperTest {

    static Stream<Arguments> getPathArguments() {
        return Stream.of(
                Arguments.of("test", "/WEB-INF/jsp/test.jsp"),
                Arguments.of("user", "/WEB-INF/jsp/user.jsp")
        );
    }


    @ParameterizedTest
    @MethodSource("getPathArguments")
    void checkGetPath(String name, String expectedResult) {
        String actualResult = JSPHelper.getPath(name);

        assertEquals(expectedResult, actualResult);
    }
}