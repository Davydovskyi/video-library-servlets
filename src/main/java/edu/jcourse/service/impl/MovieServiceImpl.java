package edu.jcourse.service.impl;

import edu.jcourse.dao.DAOProvider;
import edu.jcourse.dao.MovieDAO;
import edu.jcourse.dto.CreateMovieDTO;
import edu.jcourse.dto.MovieFilterDTO;
import edu.jcourse.dto.ReceiveMovieDTO;
import edu.jcourse.entity.Movie;
import edu.jcourse.exception.DAOException;
import edu.jcourse.exception.ServiceException;
import edu.jcourse.exception.ValidationException;
import edu.jcourse.mapper.MapperProvider;
import edu.jcourse.mapper.impl.CreateMovieMapper;
import edu.jcourse.mapper.impl.MovieMapper;
import edu.jcourse.service.MovieService;
import edu.jcourse.validator.ValidationResult;
import edu.jcourse.validator.ValidatorProvider;
import edu.jcourse.validator.impl.CreateMovieValidator;
import edu.jcourse.validator.impl.MovieFilterValidation;

import java.util.List;
import java.util.Optional;

public class MovieServiceImpl implements MovieService {

    private final MovieDAO movieDAO = DAOProvider.getInstance().getMovieDAO();
    private final CreateMovieMapper createMovieMapper = MapperProvider.getInstance().getCreateMovieMapper();
    private final CreateMovieValidator createMovieValidator = ValidatorProvider.getInstance().getCreateMovieValidator();
    private final MovieFilterValidation movieFilterValidation = ValidatorProvider.getInstance().getMovieFilterValidation();
    private final MovieMapper movieMapper = MapperProvider.getInstance().getMovieMapper();

    @Override
    public Long create(CreateMovieDTO createMovieDTO) throws ServiceException, ValidationException {
        ValidationResult validationResult = createMovieValidator.isValid(createMovieDTO);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }

        Movie movie = createMovieMapper.mapFrom(createMovieDTO);
        try {
            return movieDAO.save(movie).getId();
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<ReceiveMovieDTO> findMovies(MovieFilterDTO movieFilterDTO) throws ServiceException, ValidationException {
        ValidationResult validationResult = movieFilterValidation.isValid(movieFilterDTO);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }

        try {
            List<Movie> movies = movieDAO.findAll(movieFilterDTO);
            return movies.stream()
                    .map(movieMapper::mapFrom)
                    .toList();
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Optional<ReceiveMovieDTO> findById(Long id) throws ServiceException {
        try {
            Optional<Movie> movie = movieDAO.findById(id);
            return movie.map(movieMapper::mapFrom);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }
}