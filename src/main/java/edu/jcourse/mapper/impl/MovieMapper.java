package edu.jcourse.mapper.impl;

import edu.jcourse.dto.ReceiveMovieDto;
import edu.jcourse.dto.ReceiveMoviePersonDto;
import edu.jcourse.dto.ReceiveReviewDto;
import edu.jcourse.entity.Movie;
import edu.jcourse.mapper.Mapper;
import edu.jcourse.mapper.MapperProvider;

import java.util.List;
import java.util.ResourceBundle;

public class MovieMapper implements Mapper<Movie, ReceiveMovieDto> {
    private final ResourceBundle resourceBundle = ResourceBundle.getBundle("translations_en_US");

    @Override
    public ReceiveMovieDto mapFrom(Movie movie) {
        MoviePersonMapper moviePersonMapper = MapperProvider.getInstance().getMoviePersonMapper();
        ReviewMapper reviewMapper = MapperProvider.getInstance().getReviewMapper();

        List<ReceiveMoviePersonDto> moviePersonDTOS = movie.getMoviePersons().stream()
                .map(moviePersonMapper::mapFrom)
                .toList();
        List<ReceiveReviewDto> reviewDTOS = movie.getReviews().stream()
                .map(reviewMapper::mapFrom)
                .toList();

        String genre = resourceBundle.getString(movie.getGenre().getCode());

        return ReceiveMovieDto.builder()
                .id(movie.getId())
                .movieData("%s(%s, %d, %s)".formatted(
                        movie.getTitle(),
                        genre,
                        movie.getReleaseYear(),
                        movie.getCountry()
                ))
                .title(movie.getTitle())
                .releaseYear(movie.getReleaseYear())
                .country(movie.getCountry())
                .genre(genre)
                .description(movie.getDescription())
                .moviePeople(moviePersonDTOS)
                .reviews(reviewDTOS)
                .build();
    }
}