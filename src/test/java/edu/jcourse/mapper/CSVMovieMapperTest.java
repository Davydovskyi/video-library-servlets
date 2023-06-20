package edu.jcourse.mapper;

import edu.jcourse.dto.ReceiveMovieDto;
import edu.jcourse.mapper.impl.CSVMovieMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CSVMovieMapperTest {

    private final CSVMovieMapper csvMovieMapper = MapperProvider.getInstance().getCsvMovieMapper();

    @Test
    void map() {
        ReceiveMovieDto receiveMovieDto = buildReceiveMovieDto();

        String actualResult = csvMovieMapper.mapFrom(receiveMovieDto);

        String expectedResult = "Title,2020,Action,US,Description";

        assertThat(actualResult).isEqualTo(expectedResult);
    }

    private ReceiveMovieDto buildReceiveMovieDto() {
        return ReceiveMovieDto.builder()
                .title("Title")
                .releaseYear(2020)
                .genre("Action")
                .country("US")
                .description("Description")
                .build();
    }
}