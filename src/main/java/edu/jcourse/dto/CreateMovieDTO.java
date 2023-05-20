package edu.jcourse.dto;

import lombok.Builder;

import java.util.Set;

@Builder
public record CreateMovieDTO(String title,
                             String releaseYear,
                             String country,
                             String genre,
                             String description,
                             Set<CreateMoviePersonDTO> moviePersons) {
}
