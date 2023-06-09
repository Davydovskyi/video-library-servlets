package edu.jcourse.validator.impl;

import edu.jcourse.entity.Genre;
import edu.jcourse.util.CodeUtil;
import edu.jcourse.util.LocalDateFormatter;
import edu.jcourse.util.MessageUtil;
import edu.jcourse.util.RegexUtil;
import edu.jcourse.validator.Error;
import edu.jcourse.validator.ValidationResult;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.util.Optional;

@UtilityClass
public class CommonValidator {

    public static void nameValidation(ValidationResult validationResult, String name) {
        if (isNullOrEmpty(name)) {
            validationResult.add(Error.of(CodeUtil.INVALID_NAME_CODE, MessageUtil.NAME_INVALID_MESSAGE));
        }
    }

    public static void birthdayValidation(ValidationResult validationResult, String birthDate) {
        if (!LocalDateFormatter.isValid(birthDate) || LocalDateFormatter.parse(birthDate).isAfter(LocalDate.now())) {
            validationResult.add(Error.of(CodeUtil.INVALID_BIRTHDAY_CODE, MessageUtil.BIRTHDAY_INVALID_MESSAGE));
        }
    }

    public static void emailValidation(ValidationResult validationResult, String email) {
        if (isNullOrEmpty(email) || !email.matches(RegexUtil.EMAIL_REGEX)) {
            validationResult.add(Error.of(CodeUtil.INVALID_EMAIL_CODE, MessageUtil.EMAIL_INVALID_MESSAGE));
        }
    }

    public static void passwordValidation(ValidationResult validationResult, String password) {
        if (isNullOrEmpty(password) || password.length() < 8) {
            validationResult.add(Error.of(CodeUtil.INVALID_PASSWORD_CODE, MessageUtil.PASSWORD_INVALID_MESSAGE));
        }
    }

    public static void releaseYearValidation(ValidationResult validationResult, String releaseYear) {
        try {
            Optional<Integer> year = Optional.ofNullable(releaseYear).map(Integer::parseInt);
            int currentYear = LocalDate.now().getYear();
            if (year.isEmpty() || year.get() > currentYear || year.get() < 1900) {
                validationResult.add(Error.of(CodeUtil.INVALID_RELEASE_YEAR_CODE, MessageUtil.RELEASE_YEAR_INVALID_MESSAGE));
            }
        } catch (NumberFormatException e) {
            validationResult.add(Error.of(CodeUtil.INVALID_RELEASE_YEAR_CODE, MessageUtil.RELEASE_YEAR_INVALID_MESSAGE));
        }
    }

    public static void genreValidation(ValidationResult validationResult, String genre) {
        if (Genre.find(genre).isEmpty()) {
            validationResult.add(Error.of(CodeUtil.INVALID_GENRE_CODE, MessageUtil.GENRE_INVALID_MESSAGE));
        }
    }

    public static boolean isNullOrEmpty(String object) {
        return object == null || object.isBlank();
    }
}