package edu.jcourse.dto;

import lombok.Builder;

@Builder
public record MovieFilterDTO(int limit,
                             int offset,
                             String title,
                             String releaseYear,
                             String country,
                             String genre) {
}
