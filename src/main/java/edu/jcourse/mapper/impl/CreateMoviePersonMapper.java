package edu.jcourse.mapper.impl;

import edu.jcourse.dto.CreateMoviePersonDTO;
import edu.jcourse.entity.MoviePerson;
import edu.jcourse.entity.Person;
import edu.jcourse.entity.PersonRole;
import edu.jcourse.mapper.Mapper;

public class CreateMoviePersonMapper implements Mapper<CreateMoviePersonDTO, MoviePerson> {
    @Override
    public MoviePerson mapFrom(CreateMoviePersonDTO createMoviePersonDTO) {
        return MoviePerson.builder()
                .person(Person.builder()
                        .id(Long.parseLong(createMoviePersonDTO.personId()))
                        .build()
                )
                .personRole(PersonRole.valueOf(createMoviePersonDTO.personRole()))
                .build();
    }
}
