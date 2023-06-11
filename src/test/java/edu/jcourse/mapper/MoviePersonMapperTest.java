package edu.jcourse.mapper;

import edu.jcourse.dto.ReceiveMoviePersonDto;
import edu.jcourse.dto.ReceivePersonDto;
import edu.jcourse.entity.Movie;
import edu.jcourse.entity.MoviePerson;
import edu.jcourse.entity.Person;
import edu.jcourse.entity.PersonRole;
import edu.jcourse.mapper.impl.MoviePersonMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MoviePersonMapperTest {

    private final MoviePersonMapper moviePersonMapper = MapperProvider.getInstance().getMoviePersonMapper();

    @Test
    void map() {
        MoviePerson moviePerson = MoviePerson.builder()
                .id(1L)
                .movie(Movie.builder().id(2L).build())
                .person(Person.builder()
                        .id(3L)
                        .name("Person")
                        .birthDate(LocalDate.of(2011, 1, 1))
                        .build())
                .personRole(PersonRole.ACTOR)
                .build();

        ReceiveMoviePersonDto actualResult = moviePersonMapper.mapFrom(moviePerson);

        ReceiveMoviePersonDto expectedResult = ReceiveMoviePersonDto.builder()
                .id(1L)
                .movieId(2L)
                .person(ReceivePersonDto.builder()
                        .id(3L)
                        .personData("Person(2011)")
                        .build())
                .personRole("Actor")
                .build();

        assertEquals(expectedResult, actualResult);
    }
}