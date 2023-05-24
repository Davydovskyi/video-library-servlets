package edu.jcourse.dto;

import lombok.Builder;

@Builder
public record CreateReviewDTO(String moveId,
                              Long userId,
                              String reviewText,
                              String rate) {
}