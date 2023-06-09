package edu.jcourse.validator.impl;

import edu.jcourse.dao.DaoProvider;
import edu.jcourse.dao.MovieDao;
import edu.jcourse.dto.CreateMovieDto;
import edu.jcourse.dto.CreateMoviePersonDto;
import edu.jcourse.entity.Genre;
import edu.jcourse.entity.Movie;
import edu.jcourse.exception.DAOException;
import edu.jcourse.exception.ServiceException;
import edu.jcourse.util.CodeUtil;
import edu.jcourse.util.MessageUtil;
import edu.jcourse.validator.Error;
import edu.jcourse.validator.ValidationResult;
import edu.jcourse.validator.Validator;
import edu.jcourse.validator.ValidatorProvider;

import java.util.Optional;
import java.util.Set;

public class CreateMovieValidator implements Validator<CreateMovieDto> {

    private final MovieDao movieDAO;

    public CreateMovieValidator() {
        movieDAO = DaoProvider.getInstance().getMovieDao();
    }

    public CreateMovieValidator(MovieDao movieDAO) {
        this.movieDAO = movieDAO;
    }

    @Override
    public ValidationResult validate(CreateMovieDto createMovieDTO) throws ServiceException {
        ValidationResult validationResult = new ValidationResult();
        titleValidation(validationResult, createMovieDTO.title());
        CommonValidator.releaseYearValidation(validationResult, createMovieDTO.releaseYear());
        countryValidation(validationResult, createMovieDTO.country());
        CommonValidator.genreValidation(validationResult, createMovieDTO.genre());
        descriptionValidation(validationResult, createMovieDTO.description());
        moviePersonsValidation(validationResult, createMovieDTO.moviePersons());

        if (validationResult.isValid()) {
            checkForDuplicate(validationResult, createMovieDTO);
        }
        return validationResult;
    }

    private void titleValidation(ValidationResult validationResult, String title) {
        if (CommonValidator.isNullOrEmpty(title)) {
            validationResult.add(Error.of(CodeUtil.INVALID_TITLE_CODE, MessageUtil.TITLE_INVALID_MESSAGE));
        }
    }

    private void countryValidation(ValidationResult validationResult, String country) {
        if (CommonValidator.isNullOrEmpty(country)) {
            validationResult.add(Error.of(CodeUtil.INVALID_COUNTRY_CODE, MessageUtil.COUNTRY_INVALID_MESSAGE));
        }
    }

    private void descriptionValidation(ValidationResult validationResult, String description) {
        if (CommonValidator.isNullOrEmpty(description)) {
            validationResult.add(Error.of(CodeUtil.INVALID_DESCRIPTION_CODE, MessageUtil.DESCRIPTION_INVALID_MESSAGE));
        }
    }

    private void moviePersonsValidation(ValidationResult validationResult, Set<CreateMoviePersonDto> moviePersons) {
        if (moviePersons.isEmpty()) {
            validationResult.add(Error.of(CodeUtil.EMPTY_MOVIE_PERSONS_CODE, MessageUtil.MOVIE_PERSONS_EMPTY_MESSAGE));
        }

        CreateMoviePersonValidator createMoviePersonValidator = ValidatorProvider.getInstance().getCreateMoviePersonValidator();

        for (CreateMoviePersonDto moviePerson : moviePersons) {
            ValidationResult personValidatorResult = createMoviePersonValidator.validate(moviePerson);
            if (!personValidatorResult.isValid()) {
                validationResult.getErrors().addAll(personValidatorResult.getErrors());
                break;
            }
        }
    }

    private void checkForDuplicate(ValidationResult validationResult, CreateMovieDto createMovieDTO) throws ServiceException {
        try {
            Optional<Movie> movie = movieDAO.findByAllFields(createMovieDTO.title(), Integer.parseInt(createMovieDTO.releaseYear()),
                    createMovieDTO.country(), Genre.valueOf(createMovieDTO.genre()));
            movie.ifPresent(it -> validationResult.add(Error.of(CodeUtil.EXIST_MOVIE_CODE, MessageUtil.MOVIE_EXIST_MESSAGE)));
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }
}