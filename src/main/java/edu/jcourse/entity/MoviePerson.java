package edu.jcourse.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MoviePerson {
    private Long id;
    private Movie movie;
    private Person person;
    private PersonRole personRole;
}
