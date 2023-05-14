package edu.jcourse.dao.impl;

import edu.jcourse.dao.UserDAO;
import edu.jcourse.entity.Gender;
import edu.jcourse.entity.Role;
import edu.jcourse.entity.User;
import edu.jcourse.exception.DAOException;
import edu.jcourse.util.ConnectionBuilder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAOImpl implements UserDAO {

    private static final String FIND_ALL_SQL = """
            SELECT user_id,
            user_name,
            user_birth_date,
            user_image,
            user_email,
            user_password,
            role,
            gender
            FROM users
            """;

    private static final String FIND_BY_EMAIL_SQL = FIND_ALL_SQL + """
            WHERE user_email = ?
            """;

    private static final String FIND_BY_EMAIL_AND_PASSWORD = FIND_BY_EMAIL_SQL + """
            AND user_password = ?;
            """;

    private static final String SAVE_SQL = """
            INSERT INTO users (user_name, user_birth_date, user_image, user_email, user_password, role, gender)
            VALUES (?, ?, ?, ?, ?, ?, ?);
            """;

    @Override
    public boolean delete(Long id) throws DAOException {
        return false;
    }

    @Override
    public User save(User user) throws DAOException {
        try (Connection connection = ConnectionBuilder.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setDate(2, Date.valueOf(user.getBirthDate()));
            preparedStatement.setString(3, user.getImage());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setString(5, user.getPassword());
            preparedStatement.setString(6, user.getRole().name());
            preparedStatement.setString(7, user.getGender().name());

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getLong("user_id"));
            }
            return user;
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public void update(User user) throws DAOException {

    }

    @Override
    public List<User> findAll() throws DAOException {
        return new ArrayList<>();
    }

    @Override
    public Optional<User> findById(Long id) throws DAOException {
        return Optional.empty();
    }

    @Override
    public Optional<User> findByEmail(String email) throws DAOException {
        try (Connection connection = ConnectionBuilder.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_EMAIL_SQL)) {
            preparedStatement.setString(1, email);

            ResultSet resultSet = preparedStatement.executeQuery();

            User user = null;
            if (resultSet.next()) {
                user = buildUser(resultSet);
            }
            return Optional.ofNullable(user);
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) throws DAOException {
        try (Connection connection = ConnectionBuilder.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_EMAIL_AND_PASSWORD)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            User user = null;
            if (resultSet.next()) {
                user = buildUser(resultSet);
            }
            return Optional.ofNullable(user);
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    private User buildUser(ResultSet resultSet) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("user_id"))
                .name(resultSet.getString("user_name"))
                .birthDate(resultSet.getDate("user_birth_date").toLocalDate())
                .image(resultSet.getString("user_image"))
                .email(resultSet.getString("user_email"))
                .password(resultSet.getString("user_password"))
                .role(Role.valueOf(resultSet.getString("role")))
                .gender(Gender.valueOf(resultSet.getString("gender")))
                .build();
    }
}
