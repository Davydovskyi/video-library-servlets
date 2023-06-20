package edu.jcourse.dao.impl;

import edu.jcourse.dao.DaoProvider;
import edu.jcourse.dao.MoviePersonDao;
import edu.jcourse.dao.PersonDao;
import edu.jcourse.entity.MoviePerson;
import edu.jcourse.entity.Person;
import edu.jcourse.entity.PersonRole;
import edu.jcourse.exception.DAOException;
import edu.jcourse.util.ConnectionBuilder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MoviePersonDaoImpl implements MoviePersonDao {
    private static final String SAVE_SQL = """
            INSERT INTO movie_person(movie_id, person_id, person_role)
            VALUES (?, ?, ?);
            """;

    private static final String FIND_ALL_SQL = """
            SELECT id,
            movie_id,
            person_id,
            person_role
            FROM movie_person
            """;
    private static final String FIND_ALL_BY_MOVIE_ID_SQL = FIND_ALL_SQL + """
            WHERE movie_id = ?;
            """;

    @Override
    public boolean delete(Long id) {
        throw new UnsupportedOperationException("Not supported delete operation for MoviePerson yet.");
    }

    @Override
    public MoviePerson save(MoviePerson moviePerson) throws DAOException {
        try (Connection connection = ConnectionBuilder.getConnection()) {
            return save(moviePerson, connection);
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public void update(MoviePerson moviePerson) {
        throw new UnsupportedOperationException("Not supported update operation for MoviePerson yet.");
    }

    @Override
    public List<MoviePerson> findAll() {
        throw new UnsupportedOperationException("Not supported find all operation for MoviePerson yet.");
    }

    @Override
    public Optional<MoviePerson> findById(Long id) {
        throw new UnsupportedOperationException("Not supported find by id operation for MoviePerson yet.");
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

    @Override
    public List<MoviePerson> findAllByMovieId(Long movieId, Connection connection) throws DAOException {
        List<MoviePerson> moviePeople = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_BY_MOVIE_ID_SQL)) {
            preparedStatement.setLong(1, movieId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                MoviePerson moviePerson = buildMoviePerson(resultSet);
                setPerson(moviePerson, resultSet);
                moviePeople.add(moviePerson);
            }
            return moviePeople;
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    private MoviePerson buildMoviePerson(ResultSet resultSet) throws SQLException {
        return MoviePerson.builder()
                .id(resultSet.getLong("id"))
                .person(Person.builder()
                        .id(resultSet.getLong("person_id"))
                        .build())
                .personRole(PersonRole.valueOf(resultSet.getString("person_role")))
                .build();
    }

    private void setPerson(MoviePerson moviePerson, ResultSet resultSet) throws SQLException, DAOException {
        PersonDao personDAO = DaoProvider.getInstance().getPersonDao();
        Optional<Person> person = personDAO.findById(moviePerson.getPerson().getId(), resultSet.getStatement().getConnection());
        person.ifPresent(moviePerson::setPerson);
    }
}