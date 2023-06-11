package edu.jcourse.mapper;

import edu.jcourse.dto.ReceivePersonDto;
import edu.jcourse.entity.Person;
import edu.jcourse.mapper.impl.PersonMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PersonMapperTest {

    private final PersonMapper personMapper = MapperProvider.getInstance().getPersonMapper();

    @Test
    void map() {
        Person person = Person.builder()
                .id(1L)
                .name("name")
                .birthDate(LocalDate.of(2000, 1, 1))
                .build();

        ReceivePersonDto actualResult = personMapper.mapFrom(person);

        ReceivePersonDto expectedResult = ReceivePersonDto.builder()
                .id(1L)
                .personData("name(2000)")
                .build();

        assertEquals(expectedResult, actualResult);
    }
}