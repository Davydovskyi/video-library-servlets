package edu.jcourse.service.impl;

import edu.jcourse.dao.DAOProvider;
import edu.jcourse.dao.UserDAO;
import edu.jcourse.dto.CreateUserDTO;
import edu.jcourse.entity.User;
import edu.jcourse.exception.DAOException;
import edu.jcourse.exception.ServiceException;
import edu.jcourse.exception.ValidationException;
import edu.jcourse.mapper.MapperProvider;
import edu.jcourse.mapper.impl.CreateUserMapper;
import edu.jcourse.service.ImageService;
import edu.jcourse.service.ServiceProvider;
import edu.jcourse.service.UserService;
import edu.jcourse.validator.ValidationResult;
import edu.jcourse.validator.ValidatorProvider;
import edu.jcourse.validator.impl.CreateUserValidator;

import java.io.IOException;

public class UserServiceImpl implements UserService {

    private final UserDAO userDAO = DAOProvider.getInstance().getUserDAO();
    private final CreateUserValidator createUserValidator = ValidatorProvider.getInstance().getCreateUserValidator();
    private final CreateUserMapper createUserMapper = MapperProvider.getInstance().getCreateUserMapper();

    @Override
    public Long create(CreateUserDTO createUserDTO) throws ServiceException, ValidationException {
        try {
            ValidationResult validationResult = createUserValidator.isValid(createUserDTO);
            if (!validationResult.isValid()) {
                throw new ValidationException(validationResult.getErrors());
            }
            User user = createUserMapper.mapFrom(createUserDTO);
            ImageService imageService = ServiceProvider.getInstance().getImageService();
            imageService.upload(user.getImage(), createUserDTO.partImage().getInputStream());
            return userDAO.save(user).getId();
        } catch (DAOException | IOException e) {
            throw new ServiceException(e);
        }
    }
}
