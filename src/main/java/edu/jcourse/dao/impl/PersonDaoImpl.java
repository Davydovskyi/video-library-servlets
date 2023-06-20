package edu.jcourse.dao.impl;

import edu.jcourse.dao.PersonDao;
import edu.jcourse.entity.Person;
import edu.jcourse.exception.DAOException;
import edu.jcourse.util.ConnectionBuilder;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PersonDaoImpl implements PersonDao {

    private static final String SAVE_SQL = """
            INSERT INTO person(person_name, person_birth_date)
            VALUES (?, ?);
            """;

    private static final String FIND_ALL_SQL = """
            SELECT person_id,
            person_name,
            person_birth_date
            FROM person
            """;

    private static final String FIND_BY_NAME_AND_BIRTHDATE = FIND_ALL_SQL + """
            WHERE upper(person_name) = upper(?)
            AND person_birth_date = ?;
            """;

    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE person_id = ?;
            """;

    @Override
    public boolean delete(Long id) {
        throw new UnsupportedOperationException("Not supported delete operation for PersonDao yet.");
    }

    @Override
    public Person save(Person person) throws DAOException {
        try (Connection connection = ConnectionBuilder.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, person.getName());
            preparedStatement.setDate(2, Date.valueOf(person.getBirthDate()));

            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                person.setId(generatedKeys.getLong("person_id"));
            }
            return person;
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public void update(Person person) {
        throw new UnsupportedOperationException("Not supported update operation for PersonDao yet.");
    }

    @Override
    public List<Person> findAll() throws DAOException {
        List<Person> personList = new ArrayList<>();
        try (Connection connection = ConnectionBuilder.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                personList.add(buildPerson(resultSet));
            }
            return personList;
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public Optional<Person> findById(Long id) throws DAOException {
        try (Connection connection = ConnectionBuilder.getConnection()) {
            return findById(id, connection);
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public Optional<Person> findByNameAndBirthDate(String name, LocalDate birthDate) throws DAOException {
        try (Connection connection = ConnectionBuilder.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_NAME_AND_BIRTHDATE)) {
            preparedStatement.setString(1, name);
            preparedStatement.setDate(2, Date.valueOf(birthDate));

            ResultSet resultSet = preparedStatement.executeQuery();

            Person person = null;

            if (resultSet.next()) {
                person = buildPerson(resultSet);
            }
            return Optional.ofNullable(person);
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public Optional<Person> findById(Long id, Connection connection) throws DAOException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);

            Person person = null;
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                person = buildPerson(resultSet);
            }
            return Optional.ofNullable(person);
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    private Person buildPerson(ResultSet resultSet) throws SQLException {
        return Person.builder()
                .id(resultSet.getLong("person_id"))
                .name(resultSet.getString("person_name"))
                .birthDate(resultSet.getDate("person_birth_date").toLocalDate())
                .build();
    }
}