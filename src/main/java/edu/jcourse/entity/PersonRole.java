package edu.jcourse.entity;

import java.util.Arrays;
import java.util.Optional;

public enum PersonRole {
    PRODUCER("personRole.producer"),
    DIRECTOR("personRole.director"),
    ACTOR("personRole.actor"),
    COMPOSER("personRole.composer");

    private final String code;

    PersonRole(String name) {
        this.code = name;
    }

    public static Optional<PersonRole> find(String name) {
        return Arrays.stream(PersonRole.values())
                .filter(role -> role.name().equals(name))
                .findFirst();
    }

    public String getCode() {
        return code;
    }
}
