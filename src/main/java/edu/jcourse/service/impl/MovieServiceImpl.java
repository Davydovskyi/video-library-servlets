package edu.jcourse.service.impl;

import edu.jcourse.dao.DaoProvider;
import edu.jcourse.dao.MovieDao;
import edu.jcourse.dto.CreateMovieDto;
import edu.jcourse.dto.MovieFilterDto;
import edu.jcourse.dto.ReceiveMovieDto;
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

    private final MovieDao movieDAO;
    private final CreateMovieMapper createMovieMapper;
    private final CreateMovieValidator createMovieValidator;
    private final MovieFilterValidation movieFilterValidation;
    private final MovieMapper movieMapper;


    public MovieServiceImpl() {
        movieDAO = DaoProvider.getInstance().getMovieDao();
        createMovieMapper = MapperProvider.getInstance().getCreateMovieMapper();
        createMovieValidator = ValidatorProvider.getInstance().getCreateMovieValidator();
        movieFilterValidation = ValidatorProvider.getInstance().getMovieFilterValidation();
        movieMapper = MapperProvider.getInstance().getMovieMapper();
    }

    public MovieServiceImpl(MovieDao movieDAO,
                            CreateMovieMapper createMovieMapper,
                            CreateMovieValidator createMovieValidator,
                            MovieFilterValidation movieFilterValidation,
                            MovieMapper movieMapper) {
        this.movieDAO = movieDAO;
        this.createMovieMapper = createMovieMapper;
        this.createMovieValidator = createMovieValidator;
        this.movieFilterValidation = movieFilterValidation;
        this.movieMapper = movieMapper;
    }

    @Override
    public Long create(CreateMovieDto createMovieDTO) throws ServiceException, ValidationException {
        ValidationResult validationResult = createMovieValidator.validate(createMovieDTO);
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
    public List<ReceiveMovieDto> findMovies(MovieFilterDto movieFilterDTO) throws ServiceException, ValidationException {
        ValidationResult validationResult = movieFilterValidation.validate(movieFilterDTO);
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
    public Optional<ReceiveMovieDto> findById(Long id) throws ServiceException {
        try {
            Optional<Movie> movie = movieDAO.findById(id);
            return movie.map(movieMapper::mapFrom);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<ReceiveMovieDto> findByPersonId(Long personId) throws ServiceException {
        try {
            List<Movie> movies = movieDAO.findAllByPersonId(personId);
            return movies.stream()
                    .map(movieMapper::mapFrom)
                    .toList();
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }
}