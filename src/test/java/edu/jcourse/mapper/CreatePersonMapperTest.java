package edu.jcourse.mapper;

import edu.jcourse.dto.CreatePersonDto;
import edu.jcourse.entity.Person;
import edu.jcourse.mapper.impl.CreatePersonMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class CreatePersonMapperTest {

    private final CreatePersonMapper createPersonMapper = MapperProvider.getInstance().getCreatePersonMapper();

    @Test
    void map() {
        CreatePersonDto createPersonDto = CreatePersonDto.builder()
                .name("Test")
                .birthDate("2010-10-01")
                .build();

        Person actualResult = createPersonMapper.mapFrom(createPersonDto);

        Person expectedResult = Person.builder()
                .name("Test")
                .birthDate(LocalDate.of(2010, 10, 1))
                .build();

        assertThat(actualResult).isEqualTo(expectedResult);
    }
}