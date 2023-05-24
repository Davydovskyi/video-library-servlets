package edu.jcourse.mapper.impl;

import edu.jcourse.dto.ReceiveMovieDTO;
import edu.jcourse.dto.ReceiveMoviePersonDTO;
import edu.jcourse.dto.ReceiveReviewDTO;
import edu.jcourse.entity.Movie;
import edu.jcourse.mapper.Mapper;
import edu.jcourse.mapper.MapperProvider;

import java.util.List;
import java.util.ResourceBundle;

public class MovieMapper implements Mapper<Movie, ReceiveMovieDTO> {
    private final ResourceBundle resourceBundle = ResourceBundle.getBundle("translations_en_US");

    @Override
    public ReceiveMovieDTO mapFrom(Movie movie) {
        MoviePersonMapper moviePersonMapper = MapperProvider.getInstance().getMoviePersonMapper();
        ReviewMapper reviewMapper = MapperProvider.getInstance().getReviewMapper();

        List<ReceiveMoviePersonDTO> moviePersonDTOS = movie.getMoviePersons().stream()
                .map(moviePersonMapper::mapFrom)
                .toList();
        List<ReceiveReviewDTO> reviewDTOS = movie.getReviews().stream()
                .map(reviewMapper::mapFrom)
                .toList();

        String genre = resourceBundle.getString(movie.getGenre().getCode());

        return ReceiveMovieDTO.builder()
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