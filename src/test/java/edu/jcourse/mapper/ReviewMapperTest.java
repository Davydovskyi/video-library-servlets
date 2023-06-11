package edu.jcourse.mapper;

import edu.jcourse.dto.ReceiveMovieReviewDto;
import edu.jcourse.dto.ReceiveReviewDto;
import edu.jcourse.dto.ReceiveUserDto;
import edu.jcourse.entity.*;
import edu.jcourse.mapper.impl.ReviewMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReviewMapperTest {

    private final ReviewMapper reviewMapper = MapperProvider.getInstance().getReviewMapper();

    @Test
    void map() {
        Review review = Review.builder()
                .id(1L)
                .text("test")
                .movie(Movie.builder()
                        .id(2L)
                        .title("Title")
                        .genre(Genre.ROMANCE)
                        .releaseYear(2000)
                        .country("US")
                        .build())
                .user(User.builder()
                        .id(3L)
                        .name("User")
                        .birthDate(LocalDate.of(2011, 1, 1))
                        .image("userImage/image.jpg")
                        .email("email@mail.com")
                        .role(Role.USER)
                        .gender(Gender.FEMALE)
                        .build())
                .rate((short) 10)
                .build();

        ReceiveReviewDto actualResult = reviewMapper.mapFrom(review);

        ReceiveReviewDto expectedResult = ReceiveReviewDto.builder()
                .id(1L)
                .movie(ReceiveMovieReviewDto.builder()
                        .movieId(2L)
                        .movieData("Title(Romance, 2000, US)")
                        .build())
                .user(ReceiveUserDto.builder()
                        .id(3L)
                        .name("User")
                        .birthday(LocalDate.of(2011, 1, 1))
                        .image("userImage/image.jpg")
                        .email("email@mail.com")
                        .gender(Gender.FEMALE)
                        .role(Role.USER)
                        .build())
                .reviewText("test")
                .rate((short) 10)
                .build();

        assertEquals(expectedResult, actualResult);
    }
}