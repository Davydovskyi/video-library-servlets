package edu.jcourse.dto;

import lombok.Builder;

@Builder
public record CreateMoviePersonDto(String personId,
                                   String personRole) {
}
