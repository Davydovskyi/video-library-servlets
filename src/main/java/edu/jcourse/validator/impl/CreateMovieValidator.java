package edu.jcourse.validator.impl;

import edu.jcourse.dao.DAOProvider;
import edu.jcourse.dao.MovieDAO;
import edu.jcourse.dto.CreateMovieDTO;
import edu.jcourse.dto.CreateMoviePersonDTO;
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

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

public class CreateMovieValidator implements Validator<CreateMovieDTO> {

    private final MovieDAO movieDAO = DAOProvider.getInstance().getMovieDAO();

    @Override
    public ValidationResult isValid(CreateMovieDTO createMovieDTO) throws ServiceException {
        ValidationResult validationResult = new ValidationResult();
        titleValidation(validationResult, createMovieDTO.title());
        releaseYearValidation(validationResult, createMovieDTO.releaseYear());
        countryValidation(validationResult, createMovieDTO.country());
        genreValidation(validationResult, createMovieDTO.genre());
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

    private void releaseYearValidation(ValidationResult validationResult, String releaseYear) {
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

    private void countryValidation(ValidationResult validationResult, String country) {
        if (CommonValidator.isNullOrEmpty(country)) {
            validationResult.add(Error.of(CodeUtil.INVALID_COUNTRY_CODE, MessageUtil.COUNTRY_INVALID_MESSAGE));
        }
    }

    private void genreValidation(ValidationResult validationResult, String genre) {
        if (Genre.find(genre).isEmpty()) {
            validationResult.add(Error.of(CodeUtil.INVALID_GENRE_CODE, MessageUtil.GENRE_INVALID_MESSAGE));
        }
    }

    private void descriptionValidation(ValidationResult validationResult, String description) {
        if (CommonValidator.isNullOrEmpty(description)) {
            validationResult.add(Error.of(CodeUtil.INVALID_DESCRIPTION_CODE, MessageUtil.DESCRIPTION_INVALID_MESSAGE));
        }
    }

    private void moviePersonsValidation(ValidationResult validationResult, Set<CreateMoviePersonDTO> moviePersons) {
        if (moviePersons.isEmpty()) {
            validationResult.add(Error.of(CodeUtil.EMPTY_MOVIE_PERSONS_CODE, MessageUtil.MOVIE_PERSONS_EMPTY_MESSAGE));
        }

        CreateMoviePersonValidator createMoviePersonValidator = ValidatorProvider.getInstance().getCreateMoviePersonValidator();

        for (CreateMoviePersonDTO moviePerson : moviePersons) {
            ValidationResult personValidatorResult = createMoviePersonValidator.isValid(moviePerson);
            if (!personValidatorResult.isValid()) {
                validationResult.getErrors().addAll(personValidatorResult.getErrors());
                break;
            }
        }
    }

    private void checkForDuplicate(ValidationResult validationResult, CreateMovieDTO createMovieDTO) throws ServiceException {
        try {
            Optional<Movie> movie = movieDAO.findByAllFields(createMovieDTO.title(), Integer.parseInt(createMovieDTO.releaseYear()),
                    createMovieDTO.country(), Genre.valueOf(createMovieDTO.genre()));
            movie.ifPresent(it -> validationResult.add(Error.of(CodeUtil.EXIST_MOVIE_CODE, MessageUtil.MOVIE_EXIST_MESSAGE)));
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }
}