package edu.jcourse.validator.impl;

import edu.jcourse.dto.LoginUserDto;
import edu.jcourse.validator.ValidationResult;
import edu.jcourse.validator.Validator;

public class LoginValidator implements Validator<LoginUserDto> {
    @Override
    public ValidationResult validate(LoginUserDto loginUserDTO) {
        ValidationResult validationResult = new ValidationResult();
        CommonValidator.emailValidation(validationResult, loginUserDTO.email());
        CommonValidator.passwordValidation(validationResult, loginUserDTO.password());
        return validationResult;
    }
}