package edu.jcourse.dao.impl;

import edu.jcourse.dao.PersonDAO;
import edu.jcourse.entity.Person;
import edu.jcourse.exception.DAOException;
import edu.jcourse.util.ConnectionBuilder;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PersonDAOImpl implements PersonDAO {

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

    @Override
    public boolean delete(Long id) throws DAOException {
        return false;
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
    public void update(Person person) throws DAOException {

    }

    @Override
    public List<Person> findAll() throws DAOException {
        return new ArrayList<>();
    }

    @Override
    public Optional<Person> findById(Long id) throws DAOException {
        return Optional.empty();
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

    private Person buildPerson(ResultSet resultSet) throws SQLException {
        return Person.builder()
                .id(resultSet.getLong("person_id"))
                .name(resultSet.getString("person_name"))
                .birthDate(resultSet.getDate("person_birth_date").toLocalDate())
                .build();
    }
}