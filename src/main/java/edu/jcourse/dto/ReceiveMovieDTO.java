package edu.jcourse.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ReceiveMovieDTO(Long id,
                              String movieData,
                              String title,
                              Integer releaseYear,
                              String country,
                              String genre,
                              String description,
                              List<ReceiveReviewDTO> reviews,
                              List<ReceiveMoviePersonDTO> moviePeople) {
}