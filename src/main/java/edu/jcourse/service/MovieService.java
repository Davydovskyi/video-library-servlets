package edu.jcourse.service;

import edu.jcourse.dto.CreateMovieDTO;
import edu.jcourse.dto.MovieFilterDTO;
import edu.jcourse.dto.ReceiveMovieDTO;
import edu.jcourse.exception.ServiceException;
import edu.jcourse.exception.ValidationException;

import java.util.List;
import java.util.Optional;

public interface MovieService {

    Long create(CreateMovieDTO createMovieDTO) throws ServiceException, ValidationException;

    List<ReceiveMovieDTO> findMovies(MovieFilterDTO movieFilterDTO) throws ServiceException, ValidationException;

    Optional<ReceiveMovieDTO> findById(Long id) throws ServiceException;

    List<ReceiveMovieDTO> findByPersonId(Long personId) throws ServiceException;
}