package edu.jcourse.dao;

import edu.jcourse.entity.Person;
import edu.jcourse.exception.DAOException;

import java.time.LocalDate;
import java.util.Optional;

public interface PersonDAO extends DAO<Long, Person> {

    Optional<Person> findByNameAndBirthDate(String name, LocalDate birthDate) throws DAOException;
}
