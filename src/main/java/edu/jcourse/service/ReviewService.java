package edu.jcourse.service;

import edu.jcourse.dto.CreateReviewDTO;
import edu.jcourse.dto.ReceiveReviewDTO;
import edu.jcourse.exception.ServiceException;
import edu.jcourse.exception.ValidationException;

import java.util.List;

public interface ReviewService {

    Long create(CreateReviewDTO createReviewDTO) throws ServiceException, ValidationException;

    List<ReceiveReviewDTO> findAllByUserId(Long userId) throws ServiceException;
}