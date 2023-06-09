package edu.jcourse.validator.impl;

import edu.jcourse.dto.CreateMoviePersonDto;
import edu.jcourse.entity.PersonRole;
import edu.jcourse.util.CodeUtil;
import edu.jcourse.util.MessageUtil;
import edu.jcourse.validator.Error;
import edu.jcourse.validator.ValidationResult;
import edu.jcourse.validator.Validator;

public class CreateMoviePersonValidator implements Validator<CreateMoviePersonDto> {
    @Override
    public ValidationResult validate(CreateMoviePersonDto createMoviePersonDTO) {
        ValidationResult validationResult = new ValidationResult();
        roleValidation(validationResult, createMoviePersonDTO.personRole());
        return validationResult;
    }

    private void roleValidation(ValidationResult validationResult, String role) {
        if (PersonRole.find(role).isEmpty()) {
            validationResult.add(Error.of(CodeUtil.INVALID_MOVIE_PERSONS_CODE, MessageUtil.MOVIE_PERSONS_INVALID_MESSAGE));
        }
    }
}