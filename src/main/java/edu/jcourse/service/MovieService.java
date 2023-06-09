package edu.jcourse.service;

import edu.jcourse.dto.CreateMovieDto;
import edu.jcourse.dto.MovieFilterDto;
import edu.jcourse.dto.ReceiveMovieDto;
import edu.jcourse.exception.ServiceException;
import edu.jcourse.exception.ValidationException;

import java.util.List;
import java.util.Optional;

public interface MovieService {

    Long create(CreateMovieDto createMovieDTO) throws ServiceException, ValidationException;

    List<ReceiveMovieDto> findMovies(MovieFilterDto movieFilterDTO) throws ServiceException, ValidationException;

    Optional<ReceiveMovieDto> findById(Long id) throws ServiceException;

    List<ReceiveMovieDto> findByPersonId(Long personId) throws ServiceException;
}