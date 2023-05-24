package edu.jcourse.validator.impl;

import edu.jcourse.dto.CreateReviewDTO;
import edu.jcourse.exception.ServiceException;
import edu.jcourse.util.CodeUtil;
import edu.jcourse.util.MessageUtil;
import edu.jcourse.validator.Error;
import edu.jcourse.validator.ValidationResult;
import edu.jcourse.validator.Validator;

import java.util.Optional;

public class CreateReviewValidator implements Validator<CreateReviewDTO> {
    @Override
    public ValidationResult isValid(CreateReviewDTO createReviewDTO) throws ServiceException {
        ValidationResult validationResult = new ValidationResult();
        reviewTextValidation(validationResult, createReviewDTO.reviewText());
        rateValidation(validationResult, createReviewDTO.rate());
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
}