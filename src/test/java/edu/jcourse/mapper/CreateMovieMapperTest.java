package edu.jcourse.mapper;

import edu.jcourse.dto.CreateMovieDto;
import edu.jcourse.dto.CreateMoviePersonDto;
import edu.jcourse.entity.*;
import edu.jcourse.mapper.impl.CreateMovieMapper;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class CreateMovieMapperTest {

    private final CreateMovieMapper createMovieMapper = MapperProvider.getInstance().getCreateMovieMapper();

    @Test
    void map() {
        CreateMovieDto createMovieDto = buildCreateMovieDto();

        Movie actualResult = createMovieMapper.mapFrom(createMovieDto);

        Movie expectedResult = buildMovie();

        assertThat(actualResult).isEqualTo(expectedResult);
    }

    private CreateMovieDto buildCreateMovieDto() {
        return CreateMovieDto.builder()
                .title("Title")
                .releaseYear("2020")
                .country("US")
                .genre(Genre.ACTION.name())
                .description("Description")
                .moviePersons(Set.of(buildCreateMoviePersonDto()))
                .build();
    }

    private CreateMoviePersonDto buildCreateMoviePersonDto() {
        return CreateMoviePersonDto.builder()
                .personId("12")
                .personRole(PersonRole.ACTOR.name())
                .build();
    }

    private Movie buildMovie() {
        return Movie.builder()
                .title("Title")
                .releaseYear(2020)
                .country("US")
                .genre(Genre.ACTION)
                .description("Description")
                .moviePersons(List.of(buildMoviePerson()))
                .build();
    }

    private MoviePerson buildMoviePerson() {
        return MoviePerson.builder()
                .person(Person.builder()
                        .id(12L)
                        .build())
                .personRole(PersonRole.ACTOR)
                .build();
    }
}