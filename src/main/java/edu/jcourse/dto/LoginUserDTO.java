package edu.jcourse.dto;

import lombok.Builder;

@Builder
public record LoginUserDTO(String email,
                           String password) {
}
