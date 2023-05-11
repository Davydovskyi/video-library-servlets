package edu.jcourse.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Locale;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private Locale id;
    private String name;
    private LocalDate birthDate;
    private String image;
    private String email;
    private String password;
    private Role role;
    private Gender gender;
}
