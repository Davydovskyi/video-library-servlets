package edu.jcourse.dao.impl;

import edu.jcourse.dao.MoviePersonDAO;
import edu.jcourse.entity.MoviePerson;
import edu.jcourse.exception.DAOException;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class MoviePersonDAOImpl implements MoviePersonDAO {
    private static final String SAVE_SQL = """
            INSERT INTO movie_person(movie_id, person_id, person_role)
            VALUES (?, ?, ?);
            """;

    @Override
    public boolean delete(Long id) throws DAOException {
        return false;
    }

    @Override
    public MoviePerson save(MoviePerson moviePerson) throws DAOException {
        return null;
    }

    @Override
    public void update(MoviePerson moviePerson) throws DAOException {

    }

    @Override
    public List<MoviePerson> findAll() throws DAOException {
        return null;
    }

    @Override
    public Optional<MoviePerson> findById(Long id) throws DAOException {
        return Optional.empty();
    }

    @Override
    public MoviePerson save(MoviePerson moviePerson, Connection connection) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, moviePerson.getMovie().getId());
            preparedStatement.setLong(2, moviePerson.getPerson().getId());
            preparedStatement.setString(3, moviePerson.getPersonRole().name());

            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                moviePerson.setId(generatedKeys.getLong("id"));
            }
            return moviePerson;
        }
    }
}