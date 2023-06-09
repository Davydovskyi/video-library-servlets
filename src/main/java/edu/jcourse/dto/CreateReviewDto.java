package edu.jcourse.dto;

import lombok.Builder;

@Builder
public record CreateReviewDto(String moveId,
                              Long userId,
                              String reviewText,
                              String rate) {
}