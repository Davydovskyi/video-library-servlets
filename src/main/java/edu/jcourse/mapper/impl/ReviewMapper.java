package edu.jcourse.mapper.impl;

import edu.jcourse.dto.ReceiveReviewDto;
import edu.jcourse.entity.Review;
import edu.jcourse.mapper.Mapper;
import edu.jcourse.mapper.MapperProvider;

public class ReviewMapper implements Mapper<Review, ReceiveReviewDto> {
    @Override
    public ReceiveReviewDto mapFrom(Review review) {
        UserMapper userMapper = MapperProvider.getInstance().getUserMapper();
        MovieReviewMapper movieReviewMapper = MapperProvider.getInstance().getMovieReviewMapper();
        return ReceiveReviewDto.builder()
                .id(review.getId())
                .movie(movieReviewMapper.mapFrom(review.getMovie()))
                .user(userMapper.mapFrom(review.getUser()))
                .reviewText(review.getText())
                .rate(review.getRate())
                .build();
    }
}