package edu.jcourse.dto;

import lombok.Builder;

@Builder
public record ReceiveReviewDTO(Long id,
                               ReceiveMovieReviewDTO movie,
                               ReceiveUserDTO user,
                               String reviewText,
                               Short rate) {
}