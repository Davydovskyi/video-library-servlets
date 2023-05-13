package edu.jcourse.validator.impl;

import edu.jcourse.dao.DAOProvider;
import edu.jcourse.dao.UserDAO;
import edu.jcourse.dto.CreateUserDTO;
import edu.jcourse.entity.Gender;
import edu.jcourse.entity.Role;
import edu.jcourse.util.CodeUtil;
import edu.jcourse.util.LocalDateFormatter;
import edu.jcourse.util.MessageUtil;
import edu.jcourse.util.RegexUtil;
import edu.jcourse.validator.Error;
import edu.jcourse.validator.ValidationResult;
import edu.jcourse.validator.Validator;
import jakarta.servlet.http.Part;
import lombok.SneakyThrows;

import java.time.LocalDate;

public class CreateUserValidator implements Validator<CreateUserDTO> {
    private static final Long IMAGE_SIZE = 1024L * 1024;

    public final UserDAO userDAO = DAOProvider.getInstance().getUserDAO();

    @Override
    public ValidationResult isValid(CreateUserDTO createUserDTO) {
        ValidationResult validationResult = new ValidationResult();

        genderValidation(validationResult, createUserDTO.gender());
        roleValidation(validationResult, createUserDTO.role());
        emailValidation(validationResult, createUserDTO.email());
        passwordValidation(validationResult, createUserDTO.password());
        nameValidation(validationResult, createUserDTO.name());
        birthDateValidation(validationResult, createUserDTO.birthDate());
        imageValidation(validationResult, createUserDTO.partImage());

        return validationResult;
    }

    private void nameValidation(ValidationResult validationResult, String name) {
        if (name == null || name.isBlank()) {
            validationResult.add(Error.of(CodeUtil.INVALID_NAME_CODE, MessageUtil.NAME_INVALID_MESSAGE));
        }
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

    @SneakyThrows
    private void emailValidation(ValidationResult validationResult, String email) {
        if (email == null || email.isBlank() || !email.matches(RegexUtil.EMAIL_REGEX)) {
            validationResult.add(Error.of(CodeUtil.INVALID_EMAIL_CODE, MessageUtil.EMAIL_INVALID_MESSAGE));
        } else if (userDAO.findByEmail(email).isPresent()) {
            validationResult.add(Error.of(CodeUtil.EXIST_EMAIL_CODE, MessageUtil.EMAIL_EXISTS_MESSAGE));
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

    private void passwordValidation(ValidationResult validationResult, String password) {
        if (password == null || password.isBlank() || password.length() < 8) {
            validationResult.add(Error.of(CodeUtil.INVALID_PASSWORD_CODE, MessageUtil.PASSWORD_INVALID_MESSAGE));
        }
    }

    private void birthDateValidation(ValidationResult validationResult, String birthDate) {
        if (!LocalDateFormatter.isValid(birthDate) || LocalDateFormatter.parse(birthDate).isAfter(LocalDate.now())) {
            validationResult.add(Error.of(CodeUtil.INVALID_BIRTHDAY_CODE, MessageUtil.BIRTHDAY_INVALID_MESSAGE));
        }
    }
}