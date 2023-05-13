package edu.jcourse.validator.impl;

import edu.jcourse.dao.DAOProvider;
import edu.jcourse.dao.UserDAO;
import edu.jcourse.dto.CreateUserDTO;
import edu.jcourse.entity.Gender;
import edu.jcourse.entity.Role;
import edu.jcourse.util.LocalDateFormatter;
import edu.jcourse.validator.Error;
import edu.jcourse.validator.ValidationResult;
import edu.jcourse.validator.Validator;
import jakarta.servlet.http.Part;
import lombok.SneakyThrows;

import java.time.LocalDate;

public class CreateUserValidator implements Validator<CreateUserDTO> {

    private static final String INVALID_GENDER_CODE = "invalid.gender";
    private static final String INVALID_ROLE_CODE = "invalid.role";
    private static final String INVALID_EMAIL_CODE = "invalid.email";
    private static final String EXIST_EMAIL_CODE = "exist.email";
    private static final String INVALID_NAME_CODE = "invalid.name";
    private static final String INVALID_BIRTHDAY_CODE = "invalid.birthday";
    private static final String INVALID_PASSWORD_CODE = "invalid.password";
    private static final String INVALID_IMAGE = "invalid.image";
    private static final String INVALID_IMAGE_FORMAT = "invalid.image.format";
    private static final String INVALID_IMAGE_SIZE = "invalid.image.size";

    private static final String GENDER_INVALID_MESSAGE = "Gender is invalid";
    private static final String ROLE_INVALID_MESSAGE = "Role is invalid";
    private static final String EMAIL_INVALID_MESSAGE = "Email is invalid";
    private static final String EMAIL_EXISTS_MESSAGE = "Email exists yet";
    private static final String PASSWORD_INVALID_MESSAGE = "Password is invalid";
    private static final String NAME_INVALID_MESSAGE = "Name is invalid";
    private static final String BIRTHDAY_INVALID_MESSAGE = "Birthday is invalid";
    private static final String IMAGE_DOES_NOT_EXIST_MESSAGE = "The Image doesn't exist";
    private static final String IMAGE_TOO_BIG_MESSAGE = "The image is too big";
    private static final String IMAGE_FORMAT_INVALID_MESSAGE = "The image format is invalid";

    private static final String EMAIL_REGEX = "^([a-z0-9_-]+\\.)*[a-z0-9_-]+@[a-z0-9_-]+(\\.[a-z0-9_-]+)*\\.[a-z]{2,6}$";
    private static final Long IMAGE_SIZE = 1024L * 1024;
    private static final String IMAGE_FORMAT_REGEX = ".+\\.(PNG|JPG|JPEG)$";

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
            validationResult.add(Error.of(INVALID_NAME_CODE, NAME_INVALID_MESSAGE));
        }
    }

    private void roleValidation(ValidationResult validationResult, String role) {
        if (Role.find(role).isEmpty()) {
            validationResult.add(Error.of(INVALID_ROLE_CODE, ROLE_INVALID_MESSAGE));
        }
    }

    private void genderValidation(ValidationResult validationResult, String gender) {
        if (Gender.find(gender).isEmpty()) {
            validationResult.add(Error.of(INVALID_GENDER_CODE, GENDER_INVALID_MESSAGE));
        }
    }

    @SneakyThrows
    private void emailValidation(ValidationResult validationResult, String email) {
        if (email == null || email.isBlank() || !email.matches(EMAIL_REGEX)) {
            validationResult.add(Error.of(INVALID_EMAIL_CODE, EMAIL_INVALID_MESSAGE));
        } else if (userDAO.findByEmail(email).isPresent()) {
            validationResult.add(Error.of(EXIST_EMAIL_CODE, EMAIL_EXISTS_MESSAGE));
        }
    }

    private void imageValidation(ValidationResult validationResult, Part imagePart) {
        if (imagePart == null) {
            validationResult.add(Error.of(INVALID_IMAGE, IMAGE_DOES_NOT_EXIST_MESSAGE));
        } else if (!imagePart.getSubmittedFileName().toUpperCase().matches(IMAGE_FORMAT_REGEX)) {
            validationResult.add(Error.of(INVALID_IMAGE_FORMAT, IMAGE_FORMAT_INVALID_MESSAGE));
        } else if (imagePart.getSize() > IMAGE_SIZE) {
            validationResult.add(Error.of(INVALID_IMAGE_SIZE, IMAGE_TOO_BIG_MESSAGE));
        }
    }

    private void passwordValidation(ValidationResult validationResult, String password) {
        if (password == null || password.isBlank() || password.length() < 8) {
            validationResult.add(Error.of(INVALID_PASSWORD_CODE, PASSWORD_INVALID_MESSAGE));
        }
    }

    private void birthDateValidation(ValidationResult validationResult, String birthDate) {
        if (!LocalDateFormatter.isValid(birthDate) || LocalDateFormatter.parse(birthDate).isAfter(LocalDate.now())) {
            validationResult.add(Error.of(INVALID_BIRTHDAY_CODE, BIRTHDAY_INVALID_MESSAGE));
        }
    }
}