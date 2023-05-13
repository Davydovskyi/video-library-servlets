package edu.jcourse.validator.impl;

import edu.jcourse.dto.LoginUserDTO;
import edu.jcourse.util.CodeUtil;
import edu.jcourse.util.MessageUtil;
import edu.jcourse.util.RegexUtil;
import edu.jcourse.validator.Error;
import edu.jcourse.validator.ValidationResult;
import edu.jcourse.validator.Validator;

public class LoginValidator implements Validator<LoginUserDTO> {
    @Override
    public ValidationResult isValid(LoginUserDTO loginUserDTO) {
        ValidationResult validationResult = new ValidationResult();
        emailValidation(validationResult, loginUserDTO.email());
        passwordValidation(validationResult, loginUserDTO.password());
        return validationResult;
    }

    private void emailValidation(ValidationResult validationResult, String email) {
        if (email == null || email.isBlank() || !email.matches(RegexUtil.EMAIL_REGEX)) {
            validationResult.add(Error.of(CodeUtil.INVALID_EMAIL_CODE, MessageUtil.EMAIL_INVALID_MESSAGE));
        }
    }

    private void passwordValidation(ValidationResult validationResult, String password) {
        if (password == null || password.isBlank() || password.length() < 8) {
            validationResult.add(Error.of(CodeUtil.INVALID_PASSWORD_CODE, MessageUtil.PASSWORD_INVALID_MESSAGE));
        }
    }
}
