package edu.jcourse.dto;

import edu.jcourse.entity.Gender;
import edu.jcourse.entity.Role;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ReceiveUserDto(Long id,
                             String name,
                             LocalDate birthday,
                             String image,
                             String email,
                             Role role,
                             Gender gender) {
}