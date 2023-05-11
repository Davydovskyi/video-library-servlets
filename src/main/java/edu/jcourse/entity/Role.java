package edu.jcourse.entity;

import java.util.Arrays;
import java.util.Optional;

public enum Role {
    ADMIN,
    USER;

    public static Optional<Role> find(String name) {
        return Arrays.stream(Role.values())
                .filter(role -> role.name().equals(name))
                .findFirst();
    }
}
