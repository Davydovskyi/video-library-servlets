package edu.jcourse.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private Set<MoviePerson> moviePersons;

    public void addReview(Review review) {
        if (reviews == null) {
            reviews = new ArrayList<>();
        }
        reviews.add(review);
    }

    public void addMoviePerson(MoviePerson moviePerson) {
        if (moviePersons == null) {
            moviePersons = new HashSet<>();
        }
        moviePersons.add(moviePerson);
    }
}