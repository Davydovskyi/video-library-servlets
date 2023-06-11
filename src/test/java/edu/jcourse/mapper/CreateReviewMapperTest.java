package edu.jcourse.mapper;

import edu.jcourse.dto.CreateReviewDto;
import edu.jcourse.entity.Movie;
import edu.jcourse.entity.Review;
import edu.jcourse.entity.User;
import edu.jcourse.mapper.impl.CreateReviewMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CreateReviewMapperTest {

    private final CreateReviewMapper createReviewMapper = MapperProvider.getInstance().getCreateReviewMapper();

    @Test
    void map() {
        CreateReviewDto createReviewDto = CreateReviewDto.builder()
                .moveId("1")
                .userId(1L)
                .reviewText("This is a test")
                .rate("10")
                .build();

        Review actualResult = createReviewMapper.mapFrom(createReviewDto);

        Review expectedResult = Review.builder()
                .movie(Movie.builder()
                        .id(1L)
                        .build())
                .user(User.builder()
                        .id(1L)
                        .build())
                .text("This is a test")
                .rate((short) 10)
                .build();

        assertEquals(expectedResult, actualResult);
    }
}