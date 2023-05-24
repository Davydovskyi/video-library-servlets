package edu.jcourse.validator.impl;

import edu.jcourse.dto.MovieFilterDTO;
import edu.jcourse.exception.ServiceException;
import edu.jcourse.validator.ValidationResult;
import edu.jcourse.validator.Validator;

public class MovieFilterValidation implements Validator<MovieFilterDTO> {
    @Override
    public ValidationResult isValid(MovieFilterDTO movieFilterDTO) throws ServiceException {
        ValidationResult validationResult = new ValidationResult();
        releaseYearValidation(validationResult, movieFilterDTO.releaseYear());
        genreValidation(validationResult, movieFilterDTO.genre());
        return validationResult;
    }

    private void releaseYearValidation(ValidationResult validationResult, String releaseYear) {
        if (!releaseYear.isBlank()) {
            CommonValidator.releaseYearValidation(validationResult, releaseYear);
        }
    }

    private void genreValidation(ValidationResult validationResult, String genre) {
        if (genre != null) {
            CommonValidator.genreValidation(validationResult, genre);
        }
    }
}