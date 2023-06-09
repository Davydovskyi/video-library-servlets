package edu.jcourse.mapper.impl;

import edu.jcourse.dto.ReceiveMoviePersonDto;
import edu.jcourse.entity.MoviePerson;
import edu.jcourse.mapper.Mapper;
import edu.jcourse.mapper.MapperProvider;

import java.util.ResourceBundle;

public class MoviePersonMapper implements Mapper<MoviePerson, ReceiveMoviePersonDto> {
    private final ResourceBundle resourceBundle = ResourceBundle.getBundle("translations_en_US");

    @Override
    public ReceiveMoviePersonDto mapFrom(MoviePerson moviePerson) {
        PersonMapper personMapper = MapperProvider.getInstance().getPersonMapper();

        return ReceiveMoviePersonDto.builder()
                .id(moviePerson.getId())
                .movieId(moviePerson.getMovie().getId())
                .person(personMapper.mapFrom(moviePerson.getPerson()))
                .personRole(resourceBundle.getString(moviePerson.getPersonRole().getCode()))
                .build();
    }
}