package edu.jcourse.mapper.impl;

import edu.jcourse.dto.ReceivePersonDTO;
import edu.jcourse.entity.Person;
import edu.jcourse.mapper.Mapper;

public class PersonMapper implements Mapper<Person, ReceivePersonDTO> {
    @Override
    public ReceivePersonDTO mapFrom(Person person) {
        return ReceivePersonDTO.builder()
                .id(person.getId())
                .personData(String.format("%s(%d)", person.getName(), person.getBirthDate().getYear()))
                .build();
    }
}