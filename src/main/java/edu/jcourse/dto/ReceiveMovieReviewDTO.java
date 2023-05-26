package edu.jcourse.dto;

import lombok.Builder;

@Builder
public record ReceiveMovieReviewDTO(Long movieId,
                                    String movieData) {
}