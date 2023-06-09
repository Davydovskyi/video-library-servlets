package edu.jcourse.service;

import edu.jcourse.dto.CreateReviewDto;
import edu.jcourse.dto.ReceiveReviewDto;
import edu.jcourse.exception.ServiceException;
import edu.jcourse.exception.ValidationException;

import java.util.List;

public interface ReviewService {

    Long create(CreateReviewDto createReviewDTO) throws ServiceException, ValidationException;

    List<ReceiveReviewDto> findAllByUserId(Long userId) throws ServiceException;
}