package edu.jcourse.exception;

import edu.jcourse.validator.Error;
import lombok.Getter;

import java.util.List;

public class ValidationException extends Exception {

    @Getter
    private final transient List<Error> errors;

    public ValidationException(List<Error> errors) {
        this.errors = errors;
    }
}
