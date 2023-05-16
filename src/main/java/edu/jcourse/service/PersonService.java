package edu.jcourse.service;

import edu.jcourse.dto.CreatePersonDTO;
import edu.jcourse.exception.ServiceException;
import edu.jcourse.exception.ValidationException;

public interface PersonService {

    Long create(CreatePersonDTO createPersonDTO) throws ServiceException, ValidationException;
}
