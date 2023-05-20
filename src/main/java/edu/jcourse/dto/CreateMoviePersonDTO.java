package edu.jcourse.dto;

import lombok.Builder;

@Builder
public record CreateMoviePersonDTO(String personId,
                                   String personRole) {
}
