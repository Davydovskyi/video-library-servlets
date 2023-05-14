package edu.jcourse.entity;

import java.util.Arrays;
import java.util.Optional;

public enum Gender {
    MALE("gender.male"),
    FEMALE("gender.female"),
    OTHER("gender.other");

    private final String code;

    Gender(String name) {
        this.code = name;
    }

    public static Optional<Gender> find(String name) {
        return Arrays.stream(Gender.values())
                .filter(gender -> gender.name().equals(name))
                .findFirst();
    }

    public String getCode() {
        return code;
    }
}
