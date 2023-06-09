package edu.jcourse.dto;

import lombok.Builder;

@Builder
public record LoginUserDto(String email,
                           String password) {
}
