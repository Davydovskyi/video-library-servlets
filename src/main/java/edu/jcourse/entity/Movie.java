package edu.jcourse.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Movie {
    private Long id;
    private String title;
    private Integer releaseYear;
    private String country;
    private Genre genre;
    private String description;
    private List<Review> reviews;
    private List<MoviePerson> moviePersons;
}