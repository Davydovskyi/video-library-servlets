package edu.jcourse.dto;

import lombok.Builder;

@Builder
public record ReceiveMoviePersonDTO(Long id,
                                    Long movieId,
                                    ReceivePersonDTO person,
                                    String personRole) {
}