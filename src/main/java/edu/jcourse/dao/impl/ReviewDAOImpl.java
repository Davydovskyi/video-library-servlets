package edu.jcourse.dao.impl;

import edu.jcourse.dao.DAOProvider;
import edu.jcourse.dao.ReviewDAO;
import edu.jcourse.dao.UserDAO;
import edu.jcourse.entity.Review;
import edu.jcourse.entity.User;
import edu.jcourse.exception.DAOException;
import edu.jcourse.util.ConnectionBuilder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReviewDAOImpl implements ReviewDAO {

    private static final String FIND_ALL_SQL = """
            SELECT review_id,
            movie_id,
            user_id,
            review_text,
            rate
            FROM review
            """;

    private static final String FIND_BY_MOVIE_ID_SQL = FIND_ALL_SQL + """
            WHERE movie_id = ?;
            """;

    private static final String SAVE_SQL = """
            INSERT INTO review(movie_id, user_id, review_text, rate)
            VALUES (?, ?, ?, ?);
            """;

    @Override
    public boolean delete(Long id) throws DAOException {
        return false;
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
    public void update(Review review) throws DAOException {

    }

    @Override
    public List<Review> findAll() throws DAOException {
        return new ArrayList<>();
    }

    @Override
    public Optional<Review> findById(Long id) throws DAOException {
        return Optional.empty();
    }

    @Override
    public List<Review> findByMovieId(Long movieId, Connection connection) throws DAOException {
        List<Review> reviews = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_MOVIE_ID_SQL)) {
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

    private Review buildReview(ResultSet resultSet) throws SQLException {
        return Review.builder()
                .id(resultSet.getLong("review_id"))
                .user(User.builder()
                        .id(resultSet.getLong("user_id"))
                        .build())
                .text(resultSet.getString("review_text"))
                .rate(resultSet.getShort("rate"))
                .build();
    }

    private void setUser(Review review, ResultSet resultSet) throws SQLException, DAOException {
        UserDAO userDAO = DAOProvider.getInstance().getUserDAO();
        Optional<User> user = userDAO.findById(review.getUser().getId(), resultSet.getStatement().getConnection());
        user.ifPresent(review::setUser);
    }
}