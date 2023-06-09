package edu.jcourse.dto;

import lombok.Builder;

@Builder
public record ReceiveMoviePersonDto(Long id,
                                    Long movieId,
                                    ReceivePersonDto person,
                                    String personRole) {
}