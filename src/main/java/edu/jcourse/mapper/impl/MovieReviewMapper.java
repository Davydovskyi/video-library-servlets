package edu.jcourse.mapper.impl;

import edu.jcourse.dto.ReceiveMovieReviewDto;
import edu.jcourse.entity.Movie;
import edu.jcourse.mapper.Mapper;

import java.util.ResourceBundle;

public class MovieReviewMapper implements Mapper<Movie, ReceiveMovieReviewDto> {
    private final ResourceBundle resourceBundle = ResourceBundle.getBundle("translations_en_US");

    @Override
    public ReceiveMovieReviewDto mapFrom(Movie movie) {
        return ReceiveMovieReviewDto.builder()
                .movieId(movie.getId())
                .movieData("%s(%s, %d, %s)".formatted(
                        movie.getTitle(),
                        resourceBundle.getString(movie.getGenre().getCode()),
                        movie.getReleaseYear(),
                        movie.getCountry()
                )).build();
    }
}