package edu.jcourse.entity;

import java.util.Arrays;
import java.util.Optional;

public enum Gender {
    MALE("male"),
    FEMALE("female"),
    OTHER("other");

    private final String name;

    Gender(String name) {
        this.name = name;
    }

    public static Optional<Gender> find(String name) {
        return Arrays.stream(Gender.values())
                .filter(gender -> gender.name().equals(name))
                .findFirst();
    }

    public String getName() {
        return name;
    }
}
