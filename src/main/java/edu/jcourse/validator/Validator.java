package edu.jcourse.validator;

import edu.jcourse.exception.DAOException;

public interface Validator<T> {

    ValidationResult isValid(T t);
}
