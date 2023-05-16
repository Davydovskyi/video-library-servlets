package edu.jcourse.mapper.impl;

import edu.jcourse.dto.CreatePersonDTO;
import edu.jcourse.entity.Person;
import edu.jcourse.mapper.Mapper;
import edu.jcourse.util.LocalDateFormatter;

public class CreatePersonMapper implements Mapper<CreatePersonDTO, Person> {
    @Override
    public Person mapFrom(CreatePersonDTO createPersonDTO) {
        return Person.builder()
                .name(createPersonDTO.name())
                .birthDate(LocalDateFormatter.parse(createPersonDTO.birthDate()))
                .build();
    }
}
