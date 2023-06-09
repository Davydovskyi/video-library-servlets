package edu.jcourse.service;

import edu.jcourse.dto.CreateUserDto;
import edu.jcourse.dto.LoginUserDto;
import edu.jcourse.dto.ReceiveUserDto;
import edu.jcourse.exception.ServiceException;
import edu.jcourse.exception.ValidationException;

import java.util.Optional;

public interface UserService {

    Long create(CreateUserDto createUserDTO) throws ServiceException, ValidationException;

    Optional<ReceiveUserDto> login(LoginUserDto loginUserDTO) throws ServiceException, ValidationException;

    Optional<ReceiveUserDto> findById(Long id) throws ServiceException;
}
