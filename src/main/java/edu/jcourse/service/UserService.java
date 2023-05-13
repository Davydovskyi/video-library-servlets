package edu.jcourse.service;

import edu.jcourse.dto.CreateUserDTO;
import edu.jcourse.dto.LoginUserDTO;
import edu.jcourse.dto.UserDTO;
import edu.jcourse.exception.ServiceException;
import edu.jcourse.exception.ValidationException;

import java.util.Optional;

public interface UserService {

    Long create(CreateUserDTO createUserDTO) throws ServiceException, ValidationException;

    Optional<UserDTO> login(LoginUserDTO loginUserDTO) throws ServiceException, ValidationException;
}
