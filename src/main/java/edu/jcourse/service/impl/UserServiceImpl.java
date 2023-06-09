package edu.jcourse.service.impl;

import edu.jcourse.dao.DaoProvider;
import edu.jcourse.dao.UserDao;
import edu.jcourse.dto.CreateUserDto;
import edu.jcourse.dto.LoginUserDto;
import edu.jcourse.dto.ReceiveUserDto;
import edu.jcourse.entity.User;
import edu.jcourse.exception.DAOException;
import edu.jcourse.exception.ServiceException;
import edu.jcourse.exception.ValidationException;
import edu.jcourse.mapper.MapperProvider;
import edu.jcourse.mapper.impl.CreateUserMapper;
import edu.jcourse.mapper.impl.UserMapper;
import edu.jcourse.service.ImageService;
import edu.jcourse.service.ServiceProvider;
import edu.jcourse.service.UserService;
import edu.jcourse.validator.ValidationResult;
import edu.jcourse.validator.ValidatorProvider;
import edu.jcourse.validator.impl.CreateUserValidator;
import edu.jcourse.validator.impl.LoginValidator;

import java.io.IOException;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    private final UserDao userDAO = DaoProvider.getInstance().getUserDao();
    private final CreateUserValidator createUserValidator = ValidatorProvider.getInstance().getCreateUserValidator();
    private final CreateUserMapper createUserMapper = MapperProvider.getInstance().getCreateUserMapper();
    private final UserMapper userMapper = MapperProvider.getInstance().getUserMapper();
    private final LoginValidator loginValidator = ValidatorProvider.getInstance().getLoginValidator();

    @Override
    public Long create(CreateUserDto createUserDTO) throws ServiceException, ValidationException {
        ValidationResult validationResult = createUserValidator.validate(createUserDTO);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }

        User user = createUserMapper.mapFrom(createUserDTO);
        ImageService imageService = ServiceProvider.getInstance().getImageService();

        try {
            imageService.upload(user.getImage(), createUserDTO.partImage().getInputStream());
            return userDAO.save(user).getId();
        } catch (DAOException | IOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Optional<ReceiveUserDto> login(LoginUserDto loginUserDTO) throws ServiceException, ValidationException {
        ValidationResult validationResult = loginValidator.validate(loginUserDTO);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }
        try {
            return userDAO.findByEmailAndPassword(loginUserDTO.email(), loginUserDTO.password())
                    .map(userMapper::mapFrom);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Optional<ReceiveUserDto> findById(Long id) throws ServiceException {
        try {
            Optional<User> user = userDAO.findById(id);
            return user.map(userMapper::mapFrom);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }
}