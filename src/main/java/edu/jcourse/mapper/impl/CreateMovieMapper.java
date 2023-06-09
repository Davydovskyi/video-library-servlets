package edu.jcourse.mapper.impl;

import edu.jcourse.dto.CreateMovieDto;
import edu.jcourse.entity.Genre;
import edu.jcourse.entity.Movie;
import edu.jcourse.entity.MoviePerson;
import edu.jcourse.mapper.Mapper;
import edu.jcourse.mapper.MapperProvider;

import java.util.List;

public class CreateMovieMapper implements Mapper<CreateMovieDto, Movie> {
    @Override
    public Movie mapFrom(CreateMovieDto createMovieDTO) {
        CreateMoviePersonMapper createMoviePersonMapper = MapperProvider.getInstance().getCreateMoviePersonMapper();

        List<MoviePerson> moviePeople = createMovieDTO.moviePersons().stream()
                .map(createMoviePersonMapper::mapFrom)
                .toList();

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