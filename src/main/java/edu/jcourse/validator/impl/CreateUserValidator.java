package edu.jcourse.validator.impl;

import edu.jcourse.dao.DAOProvider;
import edu.jcourse.dao.UserDAO;
import edu.jcourse.dto.CreateUserDTO;
import edu.jcourse.entity.Gender;
import edu.jcourse.entity.Role;
import edu.jcourse.exception.DAOException;
import edu.jcourse.exception.ServiceException;
import edu.jcourse.util.CodeUtil;
import edu.jcourse.util.MessageUtil;
import edu.jcourse.util.RegexUtil;
import edu.jcourse.validator.Error;
import edu.jcourse.validator.ValidationResult;
import edu.jcourse.validator.Validator;
import jakarta.servlet.http.Part;

public class CreateUserValidator implements Validator<CreateUserDTO> {
    private static final Long IMAGE_SIZE = 1024L * 1024;

    public final UserDAO userDAO = DAOProvider.getInstance().getUserDAO();

    @Override
    public ValidationResult isValid(CreateUserDTO createUserDTO) throws ServiceException {
        ValidationResult validationResult = new ValidationResult();

        CommonValidator.emailValidation(validationResult, createUserDTO.email());
        CommonValidator.passwordValidation(validationResult, createUserDTO.password());
        CommonValidator.nameValidation(validationResult, createUserDTO.name());
        CommonValidator.birthDateValidation(validationResult, createUserDTO.birthDate());

        genderValidation(validationResult, createUserDTO.gender());
        roleValidation(validationResult, createUserDTO.role());
        imageValidation(validationResult, createUserDTO.partImage());

        if (validationResult.isValid()) {
            emailValidation(validationResult, createUserDTO.email());
        }

        return validationResult;
    }

    private void roleValidation(ValidationResult validationResult, String role) {
        if (Role.find(role).isEmpty()) {
            validationResult.add(Error.of(CodeUtil.INVALID_ROLE_CODE, MessageUtil.ROLE_INVALID_MESSAGE));
        }
    }

    private void genderValidation(ValidationResult validationResult, String gender) {
        if (Gender.find(gender).isEmpty()) {
            validationResult.add(Error.of(CodeUtil.INVALID_GENDER_CODE, MessageUtil.GENDER_INVALID_MESSAGE));
        }
    }

    private void emailValidation(ValidationResult validationResult, String email) throws ServiceException {
        try {
            if (userDAO.findByEmail(email).isPresent()) {
                validationResult.add(Error.of(CodeUtil.EXIST_EMAIL_CODE, MessageUtil.EMAIL_EXISTS_MESSAGE));
            }
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    private void imageValidation(ValidationResult validationResult, Part imagePart) {
        if (imagePart == null) {
            validationResult.add(Error.of(CodeUtil.INVALID_IMAGE, MessageUtil.IMAGE_DOES_NOT_EXIST_MESSAGE));
        } else if (!imagePart.getSubmittedFileName().toUpperCase().matches(RegexUtil.IMAGE_FORMAT_REGEX)) {
            validationResult.add(Error.of(CodeUtil.INVALID_IMAGE_FORMAT, MessageUtil.IMAGE_FORMAT_INVALID_MESSAGE));
        } else if (imagePart.getSize() > IMAGE_SIZE) {
            validationResult.add(Error.of(CodeUtil.INVALID_IMAGE_SIZE, MessageUtil.IMAGE_TOO_BIG_MESSAGE));
        }
    }
}