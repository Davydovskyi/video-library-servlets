package edu.jcourse.mapper.impl;

import edu.jcourse.dto.CreateReviewDto;
import edu.jcourse.entity.Movie;
import edu.jcourse.entity.Review;
import edu.jcourse.entity.User;
import edu.jcourse.mapper.Mapper;

public class CreateReviewMapper implements Mapper<CreateReviewDto, Review> {
    @Override
    public Review mapFrom(CreateReviewDto createReviewDTO) {
        return Review.builder()
                .movie(Movie.builder()
                        .id(Long.parseLong(createReviewDTO.moveId()))
                        .build())
                .user(User.builder()
                        .id(createReviewDTO.userId())
                        .build())
                .text(createReviewDTO.reviewText())
                .rate(Short.parseShort(createReviewDTO.rate()))
                .build();
    }
}
