package edu.jcourse.dao;

import edu.jcourse.entity.Movie;
import edu.jcourse.entity.Review;
import edu.jcourse.entity.User;
import edu.jcourse.integration.IntegrationTestBase;
import edu.jcourse.util.ConnectionBuilder;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

class ReviewDaoIT extends IntegrationTestBase {

    private final ReviewDao reviewDao = DaoProvider.getInstance().getReviewDao();

    @SneakyThrows
    @Test
    void delete() {
        assertThrowsExactly(UnsupportedOperationException.class, () -> reviewDao.delete(1L));
    }

    @SneakyThrows
    @Test
    void save() {
        Review review = buildReview((short) 5);

        reviewDao.save(review);

        assertThat(review.getId()).isNotNull();
    }

    @SneakyThrows
    @Test
    void update() {
        assertThrowsExactly(UnsupportedOperationException.class, () -> reviewDao.update(buildReview((short) 5)));
    }

    @SneakyThrows
    @Test
    void findAll() {
        assertThrowsExactly(UnsupportedOperationException.class, reviewDao::findAll);
    }

    @SneakyThrows
    @Test
    void findById() {
        assertThrowsExactly(UnsupportedOperationException.class, () -> reviewDao.findById(1L));
    }

    @SneakyThrows
    @Test
    void findAllByMovieIdWithConnection() {
        Review review1 = reviewDao.save(buildReview((short) 5));

        try (Connection connection = ConnectionBuilder.getConnection()) {
            List<Review> actualResult = reviewDao.findAllByMovieId(1L, connection);

            assertThat(actualResult).hasSize(1);
            List<Long> reviewIds = actualResult.stream()
                    .map(Review::getId)
                    .toList();
            assertThat(reviewIds).contains(review1.getId());
        }
    }

    @SneakyThrows
    @Test
    void shouldNotFindAllByMovieIdWithConnectionIfReviewDoesNotExist() {
        reviewDao.save(buildReview((short) 6));

        try (Connection connection = ConnectionBuilder.getConnection()) {
            List<Review> actualResult = reviewDao.findAllByMovieId(13L, connection);

            assertThat(actualResult).isEmpty();
        }
    }

    @SneakyThrows
    @Test
    void findAllByUserId() {
        Review review = reviewDao.save(buildReview((short) 5));

        List<Review> actualResult = reviewDao.findAllByUserId(1L);

        assertThat(actualResult).hasSize(1);
        List<Long> reviewIds = actualResult.stream()
                .map(Review::getId)
                .toList();
        assertThat(reviewIds).contains(review.getId());
    }

    @SneakyThrows
    @Test
    void shouldNotFindAllByUserIdIfReviewDoesNotExist() {
        reviewDao.save(buildReview((short) 6));

        List<Review> actualResult = reviewDao.findAllByUserId(45L);

        assertThat(actualResult).isEmpty();
    }

    @SneakyThrows
    @Test
    void findByUserIdAndMovieId() {
        Review review = reviewDao.save(buildReview((short) 5));

        Optional<Review> actualResult = reviewDao.findByUserIdAndMovieId(1L, 1L);

        assertThat(actualResult).isPresent().contains(review);
    }

    @SneakyThrows
    @Test
    void shouldNotFindByUserIdAndMovieIdIfReviewDoesNotExist() {
        reviewDao.save(buildReview((short) 6));

        Optional<Review> actualResult = reviewDao.findByUserIdAndMovieId(1L, 45L);

        assertThat(actualResult).isEmpty();
    }

    private Review buildReview(Short rate) {
        return Review.builder()
                .movie(Movie.builder().id(1L).build())
                .user(User.builder().id(1L).build())
                .text("test")
                .rate(rate)
                .build();
    }

}