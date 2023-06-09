package edu.jcourse.validator.impl;

import edu.jcourse.dto.MovieFilterDto;
import edu.jcourse.validator.ValidationResult;
import edu.jcourse.validator.Validator;

public class MovieFilterValidation implements Validator<MovieFilterDto> {
    @Override
    public ValidationResult validate(MovieFilterDto movieFilterDTO) {
        ValidationResult validationResult = new ValidationResult();
        releaseYearValidation(validationResult, movieFilterDTO.releaseYear());
        genreValidation(validationResult, movieFilterDTO.genre());
        return validationResult;
    }

    private void releaseYearValidation(ValidationResult validationResult, String releaseYear) {
        if (releaseYear != null && !releaseYear.isBlank()) {
            CommonValidator.releaseYearValidation(validationResult, releaseYear);
        }
    }

    private void genreValidation(ValidationResult validationResult, String genre) {
        if (genre != null) {
            CommonValidator.genreValidation(validationResult, genre);
        }
    }
}