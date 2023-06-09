package edu.jcourse.dto;

import jakarta.servlet.http.Part;
import lombok.Builder;

@Builder
public record CreateUserDto(String name,
                            String birthDate,
                            Part partImage,
                            String email,
                            String password,
                            String role,
                            String gender) {
}
