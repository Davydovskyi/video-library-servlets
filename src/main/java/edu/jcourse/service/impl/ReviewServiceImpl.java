package edu.jcourse.service.impl;

import edu.jcourse.dao.DaoProvider;
import edu.jcourse.dao.ReviewDao;
import edu.jcourse.dto.CreateReviewDto;
import edu.jcourse.dto.ReceiveReviewDto;
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

    private final ReviewDao reviewDAO;
    private final CreateReviewValidator createReviewValidator;
    private final CreateReviewMapper createReviewMapper;
    private final ReviewMapper reviewMapper;

    public ReviewServiceImpl() {
        this(DaoProvider.getInstance().getReviewDao(),
                ValidatorProvider.getInstance().getCreateReviewValidator(),
                MapperProvider.getInstance().getCreateReviewMapper(),
                MapperProvider.getInstance().getReviewMapper());
    }

    public ReviewServiceImpl(ReviewDao reviewDAO, CreateReviewValidator createReviewValidator, CreateReviewMapper createReviewMapper, ReviewMapper reviewMapper) {
        this.reviewDAO = reviewDAO;
        this.createReviewValidator = createReviewValidator;
        this.createReviewMapper = createReviewMapper;
        this.reviewMapper = reviewMapper;
    }

    @Override
    public Long create(CreateReviewDto createReviewDto) throws ServiceException, ValidationException {
        ValidationResult validationResult = createReviewValidator.validate(createReviewDto);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }

        Review review = createReviewMapper.mapFrom(createReviewDto);
        try {
            reviewDAO.save(review);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return review.getId();
    }

    @Override
    public List<ReceiveReviewDto> findAllByUserId(Long userId) throws ServiceException {
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