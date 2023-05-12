package edu.jcourse.service;

import edu.jcourse.dto.CreateUserDTO;
import edu.jcourse.exception.ServiceException;
import edu.jcourse.exception.ValidationException;

public interface UserService {

    Long create(CreateUserDTO createUserDTO) throws ServiceException, ValidationException;

}
