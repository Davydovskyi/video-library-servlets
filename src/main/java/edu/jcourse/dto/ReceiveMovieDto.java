package edu.jcourse.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ReceiveMovieDto(Long id,
                              String movieData,
                              String title,
                              Integer releaseYear,
                              String country,
                              String genre,
                              String description,
                              List<ReceiveReviewDto> reviews,
                              List<ReceiveMoviePersonDto> moviePeople) {
}