package edu.jcourse.dto;

import lombok.Builder;

@Builder
public record CreatePersonDTO(String name,
                              String birthDate) {
}
