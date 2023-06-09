package edu.jcourse.mapper.impl;

import edu.jcourse.dto.CreatePersonDto;
import edu.jcourse.entity.Person;
import edu.jcourse.mapper.Mapper;
import edu.jcourse.util.LocalDateFormatter;

public class CreatePersonMapper implements Mapper<CreatePersonDto, Person> {
    @Override
    public Person mapFrom(CreatePersonDto createPersonDTO) {
        return Person.builder()
                .name(createPersonDTO.name())
                .birthDate(LocalDateFormatter.parse(createPersonDTO.birthDate()))
                .build();
    }
}
