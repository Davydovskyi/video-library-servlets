package edu.jcourse.dto;

import lombok.Builder;

@Builder
public record CreatePersonDto(String name,
                              String birthDate) {
}
