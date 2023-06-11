package edu.jcourse.mapper;

import edu.jcourse.dto.ReceiveMovieReviewDto;
import edu.jcourse.entity.Genre;
import edu.jcourse.entity.Movie;
import edu.jcourse.mapper.impl.MovieReviewMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MovieReviewMapperTest {

    private final MovieReviewMapper movieReviewMapper = MapperProvider.getInstance().getMovieReviewMapper();

    @Test
    void map() {
        Movie movie = Movie.builder()
                .id(1L)
                .title("title")
                .genre(Genre.ACTION)
                .releaseYear(2000)
                .country("US")
                .build();

        ReceiveMovieReviewDto actualResult = movieReviewMapper.mapFrom(movie);

        ReceiveMovieReviewDto expectedResult = ReceiveMovieReviewDto.builder()
                .movieId(1L)
                .movieData("title(Action, 2000, US)")
                .build();

        assertEquals(expectedResult, actualResult);
    }
}