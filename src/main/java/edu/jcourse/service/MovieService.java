package edu.jcourse.service;

import edu.jcourse.dto.CreateMovieDTO;
import edu.jcourse.exception.ServiceException;
import edu.jcourse.exception.ValidationException;

public interface MovieService {

    Long create(CreateMovieDTO createMovieDTO) throws ServiceException, ValidationException;
}
