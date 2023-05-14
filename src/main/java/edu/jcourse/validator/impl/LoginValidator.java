package edu.jcourse.validator.impl;

import edu.jcourse.dto.LoginUserDTO;
import edu.jcourse.validator.ValidationResult;
import edu.jcourse.validator.Validator;

public class LoginValidator implements Validator<LoginUserDTO> {
    @Override
    public ValidationResult isValid(LoginUserDTO loginUserDTO) {
        ValidationResult validationResult = new ValidationResult();
        CommonValidator.nameValidation(validationResult, loginUserDTO.email());
        CommonValidator.passwordValidation(validationResult, loginUserDTO.password());
        return validationResult;
    }
}
