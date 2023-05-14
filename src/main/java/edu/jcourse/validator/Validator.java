package edu.jcourse.validator;

import edu.jcourse.exception.ServiceException;

public interface Validator<T> {

    ValidationResult isValid(T t) throws ServiceException;
}
