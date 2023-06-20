package edu.jcourse.dao.impl;

import edu.jcourse.dao.DaoProvider;
import edu.jcourse.dao.MovieDao;
import edu.jcourse.dao.ReviewDao;
import edu.jcourse.dao.UserDao;
import edu.jcourse.entity.Movie;
import edu.jcourse.entity.Review;
import edu.jcourse.entity.User;
import edu.jcourse.exception.DAOException;
import edu.jcourse.util.ConnectionBuilder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReviewDaoImpl implements ReviewDao {

    private static final String FIND_ALL_SQL = """
            SELECT review_id,
            movie_id,
            user_id,
            review_text,
            rate
            FROM review
            """;

    private static final String FIND_ALL_BY_MOVIE_ID_SQL = FIND_ALL_SQL + """
            WHERE movie_id = ?;
            """;

    private static final String SAVE_SQL = """
            INSERT INTO review(movie_id, user_id, review_text, rate)
            VALUES (?, ?, ?, ?);
            """;

    private static final String FIND_ALL_BY_USER_ID_SQL = FIND_ALL_SQL + """
            WHERE user_id = ?
            """;

    private static final String FIND_BY_USER_ID_AND_MOVIE_ID_SQL = FIND_ALL_BY_USER_ID_SQL + """
            AND movie_id = ?;
            """;

    @Override
    public boolean delete(Long id) {
        throw new UnsupportedOperationException("Not supported delete operation for ReviewDAO yet.");
    }

    @Override
    public Review save(Review review) throws DAOException {
        try (Connection connection = ConnectionBuilder.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, review.getMovie().getId());
            preparedStatement.setLong(2, review.getUser().getId());
            preparedStatement.setString(3, review.getText());
            preparedStatement.setShort(4, review.getRate());

            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                review.setId(generatedKeys.getLong("review_id"));
            }
            return review;
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public void update(Review review) {
        throw new UnsupportedOperationException("Not supported update operation for ReviewDAO yet.");
    }

    @Override
    public List<Review> findAll() {
        throw new UnsupportedOperationException("Not supported findAll operation for ReviewDAO yet.");
    }

    @Override
    public Optional<Review> findById(Long id) {
        throw new UnsupportedOperationException("Not supported findById operation for ReviewDAO yet.");
    }

    @Override
    public List<Review> findAllByMovieId(Long movieId, Connection connection) throws DAOException {
        List<Review> reviews = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_BY_MOVIE_ID_SQL)) {
            preparedStatement.setLong(1, movieId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Review review = buildReview(resultSet);
                setUser(review, resultSet);
                reviews.add(review);
            }
            return reviews;
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public List<Review> findAllByUserId(Long userId) throws DAOException {
        List<Review> reviews = new ArrayList<>();
        try (Connection connection = ConnectionBuilder.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_BY_USER_ID_SQL)) {
            preparedStatement.setLong(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Review review = buildReview(resultSet);
                setMovie(review, resultSet);
                reviews.add(review);
            }

            return reviews;
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public Optional<Review> findByUserIdAndMovieId(Long userId, Long movieId) throws DAOException {
        try (Connection connection = ConnectionBuilder.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_USER_ID_AND_MOVIE_ID_SQL)) {
            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, movieId);

            Review review = null;
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                review = buildReview(resultSet);
            }

            return Optional.ofNullable(review);
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    private Review buildReview(ResultSet resultSet) throws SQLException {
        return Review.builder()
                .id(resultSet.getLong("review_id"))
                .movie(Movie.builder()
                        .id(resultSet.getLong("movie_id"))
                        .build())
                .user(User.builder()
                        .id(resultSet.getLong("user_id"))
                        .build())
                .text(resultSet.getString("review_text"))
                .rate(resultSet.getShort("rate"))
                .build();
    }

    private void setUser(Review review, ResultSet resultSet) throws SQLException, DAOException {
        UserDao userDAO = DaoProvider.getInstance().getUserDao();
        Optional<User> user = userDAO.findById(review.getUser().getId(), resultSet.getStatement().getConnection());
        user.ifPresent(review::setUser);
    }

    private void setMovie(Review review, ResultSet resultSet) throws DAOException, SQLException {
        MovieDao movieDAO = DaoProvider.getInstance().getMovieDao();
        Optional<Movie> movie = movieDAO.findById(review.getMovie().getId(), resultSet.getStatement().getConnection());
        movie.ifPresent(review::setMovie);
    }
}