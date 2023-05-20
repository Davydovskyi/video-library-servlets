package edu.jcourse.mapper.impl;

import edu.jcourse.dto.CreateMovieDTO;
import edu.jcourse.entity.Genre;
import edu.jcourse.entity.Movie;
import edu.jcourse.entity.MoviePerson;
import edu.jcourse.mapper.Mapper;
import edu.jcourse.mapper.MapperProvider;

import java.util.Set;
import java.util.stream.Collectors;

public class CreateMovieMapper implements Mapper<CreateMovieDTO, Movie> {
    @Override
    public Movie mapFrom(CreateMovieDTO createMovieDTO) {
        CreateMoviePersonMapper createMoviePersonMapper = MapperProvider.getInstance().getCreateMoviePersonMapper();

        Set<MoviePerson> moviePeople = createMovieDTO.moviePersons().stream()
                .map(createMoviePersonMapper::mapFrom)
                .collect(Collectors.toSet());

        return Movie.builder()
                .title(createMovieDTO.title())
                .releaseYear(Integer.parseInt(createMovieDTO.releaseYear()))
                .country(createMovieDTO.country())
                .genre(Genre.valueOf(createMovieDTO.genre()))
                .description(createMovieDTO.description())
                .moviePersons(moviePeople)
                .build();
    }
}