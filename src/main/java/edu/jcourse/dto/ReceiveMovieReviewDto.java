package edu.jcourse.dto;

import lombok.Builder;

@Builder
public record ReceiveMovieReviewDto(Long movieId,
                                    String movieData) {
}