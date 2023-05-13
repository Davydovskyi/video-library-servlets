package edu.jcourse.validator;

public interface Validator<T> {

    ValidationResult isValid(T t);
}
