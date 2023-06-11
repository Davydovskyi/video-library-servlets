package edu.jcourse.mapper;

import edu.jcourse.dto.CreateMoviePersonDto;
import edu.jcourse.entity.MoviePerson;
import edu.jcourse.entity.Person;
import edu.jcourse.entity.PersonRole;
import edu.jcourse.mapper.impl.CreateMoviePersonMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CreateMoviePersonMapperTest {

    private final CreateMoviePersonMapper createMoviePersonMapper = MapperProvider.getInstance().getCreateMoviePersonMapper();

    @Test
    void map() {
        CreateMoviePersonDto createMoviePersonDto = CreateMoviePersonDto.builder()
                .personId("12")
                .personRole(PersonRole.COMPOSER.name())
                .build();

        MoviePerson actualResult = createMoviePersonMapper.mapFrom(createMoviePersonDto);

        MoviePerson expectedResult = MoviePerson.builder()
                .person(Person.builder()
                        .id(12L)
                        .build())
                .personRole(PersonRole.COMPOSER)
                .build();

        assertThat(actualResult).isEqualTo(expectedResult);
    }
}