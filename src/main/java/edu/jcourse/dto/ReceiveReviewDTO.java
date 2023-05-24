package edu.jcourse.dto;

import lombok.Builder;

@Builder
public record ReceiveReviewDTO(Long id,
                               Long movieId,
                               ReceiveUserDTO user,
                               String reviewText,
                               Short rate) {
}