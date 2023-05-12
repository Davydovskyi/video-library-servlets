package edu.jcourse.service.impl;

import edu.jcourse.dto.CreateUserDTO;
import edu.jcourse.exception.ServiceException;
import edu.jcourse.exception.ValidationException;
import edu.jcourse.service.UserService;

public class UserServiceImpl implements UserService {
    @Override
    public Long create(CreateUserDTO createUserDTO) throws ServiceException, ValidationException {
        return null;
    }
}
