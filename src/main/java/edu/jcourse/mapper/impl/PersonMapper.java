package edu.jcourse.mapper.impl;

import edu.jcourse.dto.ReceivePersonDto;
import edu.jcourse.entity.Person;
import edu.jcourse.mapper.Mapper;

public class PersonMapper implements Mapper<Person, ReceivePersonDto> {
    @Override
    public ReceivePersonDto mapFrom(Person person) {
        return ReceivePersonDto.builder()
                .id(person.getId())
                .personData(String.format("%s(%d)", person.getName(), person.getBirthDate().getYear()))
                .build();
    }
}