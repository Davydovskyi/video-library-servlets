package edu.jcourse.dao;

import edu.jcourse.entity.Person;
import edu.jcourse.integration.IntegrationTestBase;
import edu.jcourse.util.ConnectionBuilder;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

class PersonDaoIT extends IntegrationTestBase {

    private final PersonDao personDao = DaoProvider.getInstance().getPersonDao();

    @Test
    void delete() {
        assertThrowsExactly(UnsupportedOperationException.class, () -> personDao.delete(1L));
    }

    @SneakyThrows
    @Test
    void save() {
        Person person = buildPerson();

        Person actualResult = personDao.save(person);

        assertThat(actualResult.getId()).isNotNull();
    }

    @Test
    void update() {
        assertThrowsExactly(UnsupportedOperationException.class, () -> personDao.update(buildPerson()));
    }

    @SneakyThrows
    @Test
    void findAll() {
        Person person = personDao.save(buildPerson());

        List<Person> actualResult = personDao.findAll();

        assertThat(actualResult).hasSize(2).contains(person);
    }

    @SneakyThrows
    @Test
    void findById() {
        Person person = personDao.save(buildPerson());

        Optional<Person> actualResult = personDao.findById(person.getId());

        assertThat(actualResult).isPresent().contains(person);
    }

    @SneakyThrows
    @Test
    void shouldNotFindByIdIfPersonDoesNotExist() {
        personDao.save(buildPerson());

        Optional<Person> actualResult = personDao.findById(45L);

        assertThat(actualResult).isEmpty();
    }

    @SneakyThrows
    @Test
    void findByNameAndBirthDate() {
        Person person = personDao.save(buildPerson());

        Optional<Person> actualResult = personDao.findByNameAndBirthDate(person.getName(), person.getBirthDate());

        assertThat(actualResult).isPresent().contains(person);
    }

    @SneakyThrows
    @Test
    void shouldNotFindByNameAndBirthDateIfPersonDoesNotExist() {
        Person person = personDao.save(buildPerson());

        Optional<Person> actualResult = personDao.findByNameAndBirthDate("dummy", person.getBirthDate());

        assertThat(actualResult).isEmpty();
    }

    @SneakyThrows
    @Test
    void findByIdWithConnection() {
        Person person = personDao.save(buildPerson());

        try (Connection connection = ConnectionBuilder.getConnection()) {
            Optional<Person> actualResult = personDao.findById(person.getId(), connection);

            assertThat(actualResult).isPresent().contains(person);
        }
    }

    @SneakyThrows
    @Test
    void shouldNotFindByIdWithConnectionIfPersonDoesNotExist() {
        personDao.save(buildPerson());

        try (Connection connection = ConnectionBuilder.getConnection()) {
            Optional<Person> actualResult = personDao.findById(45L, connection);

            assertThat(actualResult).isEmpty();
        }
    }

    private Person buildPerson() {
        return Person.builder()
                .name("Person")
                .birthDate(LocalDate.of(1990, 1, 1))
                .build();
    }
}