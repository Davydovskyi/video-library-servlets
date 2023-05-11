package edu.jcourse.entity;

import java.util.Arrays;
import java.util.Optional;

public enum Genre {
    FANTASY("Fantasy"),
    FICTION("Fiction"),
    ACTION("Action"),
    SCIENCE_FICTION("Science fiction"),
    THRILLER("Thriller"),
    DRAMA("Drama"),
    ROMANCE("Romance"),
    ADVENTURE("Adventure"),
    MUSICAL("Musical");

    private final String name;

    Genre(String name) {
        this.name = name;
    }

    public static Optional<Genre> find(String name) {
        return Arrays.stream(Genre.values())
                .filter(genre -> genre.name.equals(name))
                .findFirst();
    }

    public String getName() {
        return name;
    }
}
