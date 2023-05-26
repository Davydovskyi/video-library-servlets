package edu.jcourse.mapper.impl;

import edu.jcourse.dto.ReceiveMovieReviewDTO;
import edu.jcourse.entity.Movie;
import edu.jcourse.mapper.Mapper;

import java.util.ResourceBundle;

public class MovieReviewMapper implements Mapper<Movie, ReceiveMovieReviewDTO> {
    private final ResourceBundle resourceBundle = ResourceBundle.getBundle("translations_en_US");

    @Override
    public ReceiveMovieReviewDTO mapFrom(Movie movie) {
        return ReceiveMovieReviewDTO.builder()
                .movieId(movie.getId())
                .movieData("%s(%s, %d, %s)".formatted(
                        movie.getTitle(),
                        resourceBundle.getString(movie.getGenre().getCode()),
                        movie.getReleaseYear(),
                        movie.getCountry()
                )).build();
    }
}