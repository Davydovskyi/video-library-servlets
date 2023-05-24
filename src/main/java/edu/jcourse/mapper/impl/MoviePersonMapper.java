package edu.jcourse.mapper.impl;

import edu.jcourse.dto.ReceiveMoviePersonDTO;
import edu.jcourse.entity.MoviePerson;
import edu.jcourse.mapper.Mapper;
import edu.jcourse.mapper.MapperProvider;

import java.util.ResourceBundle;

public class MoviePersonMapper implements Mapper<MoviePerson, ReceiveMoviePersonDTO> {
    private final ResourceBundle resourceBundle = ResourceBundle.getBundle("translations_en_US");

    @Override
    public ReceiveMoviePersonDTO mapFrom(MoviePerson moviePerson) {
        PersonMapper personMapper = MapperProvider.getInstance().getPersonMapper();

        return ReceiveMoviePersonDTO.builder()
                .id(moviePerson.getId())
                .movieId(moviePerson.getMovie().getId())
                .person(personMapper.mapFrom(moviePerson.getPerson()))
                .personRole(resourceBundle.getString(moviePerson.getPersonRole().getCode()))
                .build();
    }
}