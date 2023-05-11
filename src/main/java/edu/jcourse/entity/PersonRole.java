package edu.jcourse.entity;

import java.util.Arrays;
import java.util.Optional;

public enum PersonRole {
    PRODUCER("Producer"),
    DIRECTOR("Director"),
    ACTOR("Actor"),
    COMPOSER("Composer");

    private final String name;

    PersonRole(String name) {
        this.name = name;
    }

    public static Optional<PersonRole> find(String name) {
        return Arrays.stream(PersonRole.values())
                .filter(role -> role.name.equals(name))
                .findFirst();
    }

    public String getName() {
        return name;
    }
}
