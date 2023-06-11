package edu.jcourse.mapper;

import edu.jcourse.dto.*;
import edu.jcourse.entity.*;
import edu.jcourse.mapper.impl.MovieMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MovieMapperTest {

    private final MovieMapper movieMapper = MapperProvider.getInstance().getMovieMapper();

    @Test
    void map() {
        Movie movie = buildMovie();

        ReceiveMovieDto actualResult = movieMapper.mapFrom(movie);

        ReceiveMovieDto expectedResult = buildReceiveMovieDto();

        assertEquals(expectedResult, actualResult);
    }

    private Movie buildMovie() {
        Movie movie = Movie.builder()
                .id(1L)
                .title("Title")
                .releaseYear(2020)
                .country("US")
                .description("Description")
                .genre(Genre.ACTION)
                .build();
        movie.setReviews(List.of(buildReview(movie)));
        movie.setMoviePersons(List.of(buildMoviePerson(movie)));
        return movie;
    }

    private MoviePerson buildMoviePerson(Movie movie) {
        return MoviePerson.builder()
                .id(3L)
                .movie(movie)
                .person(Person.builder()
                        .id(2L)
                        .name("Person")
                        .birthDate(LocalDate.of(2017, 1, 1))
                        .build())
                .personRole(PersonRole.ACTOR)
                .build();
    }

    private Review buildReview(Movie movie) {
        return Review.builder()
                .id(4L)
                .movie(movie)
                .user(buildUser())
                .text("Review")
                .rate((short) 10)
                .build();
    }

    private User buildUser() {
        return User.builder()
                .id(5L)
                .name("User")
                .birthDate(LocalDate.of(2000, 2, 2))
                .image("userImage/image.jpg")
                .email("email@example.com")
                .password("password")
                .role(Role.USER)
                .gender(Gender.FEMALE)
                .build();
    }

    private ReceiveMovieDto buildReceiveMovieDto() {
        return ReceiveMovieDto.builder()
                .id(1L)
                .movieData("%s(%s, %d, %s)".formatted(
                        "Title",
                        "Action",
                        2020,
                        "US"
                ))
                .title("Title")
                .releaseYear(2020)
                .country("US")
                .genre("Action")
                .description("Description")
                .moviePeople(List.of(buildReceiveMoviePersonDto()))
                .reviews(List.of(buildReceiveReviewDto()))
                .build();
    }

    private ReceiveReviewDto buildReceiveReviewDto() {
        return ReceiveReviewDto.builder()
                .id(4L)
                .movie(ReceiveMovieReviewDto.builder()
                        .movieId(1L)
                        .movieData("%s(%s, %d, %s)".formatted(
                                "Title",
                                "Action",
                                2020,
                                "US"
                        ))
                        .build())
                .user(buildReceiveUserDto())
                .reviewText("Review")
                .rate((short) 10)
                .build();
    }

    private ReceiveUserDto buildReceiveUserDto() {
        return ReceiveUserDto.builder()
                .id(5L)
                .name("User")
                .birthday(LocalDate.of(2000, 2, 2))
                .image("userImage/image.jpg")
                .email("email@example.com")
                .role(Role.USER)
                .gender(Gender.FEMALE)
                .build();
    }

    private ReceiveMoviePersonDto buildReceiveMoviePersonDto() {
        return ReceiveMoviePersonDto.builder()
                .id(3L)
                .movieId(1L)
                .person(buildReceivePersonDto())
                .personRole("Actor")
                .build();
    }

    private ReceivePersonDto buildReceivePersonDto() {
        return ReceivePersonDto.builder()
                .id(2L)
                .personData("Person(2017)")
                .build();
    }
}