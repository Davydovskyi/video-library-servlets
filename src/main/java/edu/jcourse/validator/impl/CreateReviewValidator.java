package edu.jcourse.validator.impl;

import edu.jcourse.dao.DAOProvider;
import edu.jcourse.dao.ReviewDAO;
import edu.jcourse.dto.CreateReviewDTO;
import edu.jcourse.entity.Review;
import edu.jcourse.exception.DAOException;
import edu.jcourse.exception.ServiceException;
import edu.jcourse.util.CodeUtil;
import edu.jcourse.util.MessageUtil;
import edu.jcourse.validator.Error;
import edu.jcourse.validator.ValidationResult;
import edu.jcourse.validator.Validator;

import java.util.Optional;

public class CreateReviewValidator implements Validator<CreateReviewDTO> {

    private final ReviewDAO reviewDAO = DAOProvider.getInstance().getReviewDAO();

    @Override
    public ValidationResult isValid(CreateReviewDTO createReviewDTO) throws ServiceException {
        ValidationResult validationResult = new ValidationResult();
        reviewTextValidation(validationResult, createReviewDTO.reviewText());
        rateValidation(validationResult, createReviewDTO.rate());

        if (!validationResult.isValid()) {
            checkForDuplicate(validationResult, createReviewDTO.userId(), Long.parseLong(createReviewDTO.moveId()));
        }
        return validationResult;
    }


    private void reviewTextValidation(ValidationResult validationResult, String reviewText) {
        if (CommonValidator.isNullOrEmpty(reviewText)) {
            validationResult.add(Error.of(CodeUtil.INVALID_REVIEW_CODE, MessageUtil.REVIEW_INVALID_MESSAGE));
        } else if (reviewText.length() > 256) {
            validationResult.add(Error.of(CodeUtil.INVALID_REVIEW_SIZE_CODE, MessageUtil.REVIEW_INVALID_SIZE_MESSAGE));
        }
    }

    private void rateValidation(ValidationResult validationResult, String rate) {
        try {
            Optional<Short> grade = Optional.ofNullable(rate).map(Short::parseShort);
            if (grade.isEmpty() || grade.get() > 10 || grade.get() < 1) {
                validationResult.add(Error.of(CodeUtil.INVALID_RATE_CODE, MessageUtil.RATE_INVALID_MESSAGE));
            }
        } catch (NumberFormatException e) {
            validationResult.add(Error.of(CodeUtil.INVALID_RATE_CODE, MessageUtil.RATE_INVALID_MESSAGE));
        }
    }

    private void checkForDuplicate(ValidationResult validationResult, Long userId, Long movieId) throws ServiceException {
        try {
            Optional<Review> review = reviewDAO.findByUserIdAndMovieId(userId, movieId);
            review.ifPresent(it -> validationResult.add(Error.of(CodeUtil.EXIST_REVIEW_CODE, MessageUtil.REVIEW_EXIST_MESSAGE)));
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }
}