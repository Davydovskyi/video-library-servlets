package edu.jcourse.service;

import edu.jcourse.dto.CreateReviewDTO;
import edu.jcourse.exception.ServiceException;
import edu.jcourse.exception.ValidationException;

public interface ReviewService {

    Long create(CreateReviewDTO createReviewDTO) throws ServiceException, ValidationException;
}