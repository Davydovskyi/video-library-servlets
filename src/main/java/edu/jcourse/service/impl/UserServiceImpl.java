package edu.jcourse.service.impl;

import edu.jcourse.dao.DAOProvider;
import edu.jcourse.dao.UserDAO;
import edu.jcourse.dto.CreateUserDTO;
import edu.jcourse.dto.LoginUserDTO;
import edu.jcourse.dto.ReceiveUserDTO;
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

    private final UserDAO userDAO = DAOProvider.getInstance().getUserDAO();
    private final CreateUserValidator createUserValidator = ValidatorProvider.getInstance().getCreateUserValidator();
    private final CreateUserMapper createUserMapper = MapperProvider.getInstance().getCreateUserMapper();
    private final UserMapper userMapper = MapperProvider.getInstance().getUserMapper();
    private final LoginValidator loginValidator = ValidatorProvider.getInstance().getLoginValidator();

    @Override
    public Long create(CreateUserDTO createUserDTO) throws ServiceException, ValidationException {
        ValidationResult validationResult = createUserValidator.isValid(createUserDTO);
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
    public Optional<ReceiveUserDTO> login(LoginUserDTO loginUserDTO) throws ServiceException, ValidationException {
        ValidationResult validationResult = loginValidator.isValid(loginUserDTO);
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
}