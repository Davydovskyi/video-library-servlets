package edu.jcourse.validator;

import edu.jcourse.exception.ServiceException;

public interface Validator<T> {

    ValidationResult validate(T t) throws ServiceException;
}
