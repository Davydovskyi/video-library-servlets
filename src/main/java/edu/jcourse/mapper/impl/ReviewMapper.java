package edu.jcourse.mapper.impl;

import edu.jcourse.dto.ReceiveReviewDTO;
import edu.jcourse.entity.Review;
import edu.jcourse.mapper.Mapper;
import edu.jcourse.mapper.MapperProvider;

public class ReviewMapper implements Mapper<Review, ReceiveReviewDTO> {
    @Override
    public ReceiveReviewDTO mapFrom(Review review) {
        UserMapper userMapper = MapperProvider.getInstance().getUserMapper();
        return ReceiveReviewDTO.builder()
                .id(review.getId())
                .movieId(review.getMovie().getId())
                .user(userMapper.mapFrom(review.getUser()))
                .reviewText(review.getText())
                .rate(review.getRate())
                .build();
    }
}