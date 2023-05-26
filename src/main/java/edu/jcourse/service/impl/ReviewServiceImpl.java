package edu.jcourse.service.impl;

import edu.jcourse.dao.DAOProvider;
import edu.jcourse.dao.ReviewDAO;
import edu.jcourse.dto.CreateReviewDTO;
import edu.jcourse.dto.ReceiveReviewDTO;
import edu.jcourse.entity.Review;
import edu.jcourse.exception.DAOException;
import edu.jcourse.exception.ServiceException;
import edu.jcourse.exception.ValidationException;
import edu.jcourse.mapper.MapperProvider;
import edu.jcourse.mapper.impl.CreateReviewMapper;
import edu.jcourse.mapper.impl.ReviewMapper;
import edu.jcourse.service.ReviewService;
import edu.jcourse.validator.ValidationResult;
import edu.jcourse.validator.ValidatorProvider;
import edu.jcourse.validator.impl.CreateReviewValidator;

import java.util.List;

public class ReviewServiceImpl implements ReviewService {

    private final ReviewDAO reviewDAO = DAOProvider.getInstance().getReviewDAO();
    private final CreateReviewValidator createReviewValidator = ValidatorProvider.getInstance().getCreateReviewValidator();
    private final CreateReviewMapper createReviewMapper = MapperProvider.getInstance().getCreateReviewMapper();
    private final ReviewMapper reviewMapper = MapperProvider.getInstance().getReviewMapper();

    @Override
    public Long create(CreateReviewDTO createReviewDTO) throws ServiceException, ValidationException {
        ValidationResult validationResult = createReviewValidator.isValid(createReviewDTO);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }

        Review review = createReviewMapper.mapFrom(createReviewDTO);
        try {
            reviewDAO.save(review);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return review.getId();
    }

    @Override
    public List<ReceiveReviewDTO> findAllByUserId(Long userId) throws ServiceException {
        try {
            List<Review> reviews = reviewDAO.findAllByUserId(userId);
            return reviews.stream()
                    .map(reviewMapper::mapFrom)
                    .toList();
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }
}