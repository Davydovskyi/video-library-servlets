package edu.jcourse.dto;

import lombok.Builder;

@Builder
public record ReceiveReviewDto(Long id,
                               ReceiveMovieReviewDto movie,
                               ReceiveUserDto user,
                               String reviewText,
                               Short rate) {
}