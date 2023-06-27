package edu.jcourse.dao;

import edu.jcourse.entity.Gender;
import edu.jcourse.entity.Role;
import edu.jcourse.entity.User;
import edu.jcourse.integration.IntegrationTestBase;
import edu.jcourse.util.ConnectionBuilder;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

class UserDaoIT extends IntegrationTestBase {

    private final UserDao userDao = DaoProvider.getInstance().getUserDao();

    @SneakyThrows
    @Test
    void delete() {
        assertThrowsExactly(UnsupportedOperationException.class, () -> userDao.delete(1L));
    }

    @SneakyThrows
    @Test
    void save() {
        User user = buildUser();

        User actualResult = userDao.save(user);

        assertThat(actualResult.getId()).isNotNull();
    }

    @SneakyThrows
    @Test
    void update() {
        assertThrowsExactly(UnsupportedOperationException.class, () -> userDao.update(buildUser()));
    }

    @SneakyThrows
    @Test
    void findAll() {
        assertThrowsExactly(UnsupportedOperationException.class, userDao::findAll);
    }

    @SneakyThrows
    @Test
    void findById() {
        User user = userDao.save(buildUser());

        Optional<User> actualResult = userDao.findById(user.getId());

        assertThat(actualResult).isPresent().contains(user);
    }

    @SneakyThrows
    @Test
    void shouldNotFindByIdIfUserDoesNotExist() {
        userDao.save(buildUser());

        Optional<User> actualResult = userDao.findById(34L);

        assertThat(actualResult).isEmpty();
    }

    @SneakyThrows
    @Test
    void findByEmail() {
        User user = userDao.save(buildUser());

        Optional<User> actualResult = userDao.findByEmail(user.getEmail());

        assertThat(actualResult).isPresent().contains(user);
    }

    @SneakyThrows
    @Test
    void shouldNotFindByEmailIfUserDoesNotExist() {
        userDao.save(buildUser());

        Optional<User> actualResult = userDao.findByEmail("dummy");

        assertThat(actualResult).isEmpty();
    }

    @SneakyThrows
    @Test
    void findByEmailAndPassword() {
        User user = userDao.save(buildUser());

        Optional<User> actualResult = userDao.findByEmailAndPassword(user.getEmail(), user.getPassword());

        assertThat(actualResult).isPresent().contains(user);
    }

    @SneakyThrows
    @Test
    void shouldNotFindEmailByEmailAndPasswordIfUserDoesNotExist() {
        userDao.save(buildUser());

        Optional<User> actualResult = userDao.findByEmailAndPassword("dummy", "dummy");

        assertThat(actualResult).isEmpty();
    }

    @SneakyThrows
    @Test
    void findByIdWithConnection() {
        User user = userDao.save(buildUser());

        try (Connection connection = ConnectionBuilder.getConnection()) {
            Optional<User> actualResult = userDao.findById(user.getId(), connection);

            assertThat(actualResult).isPresent().contains(user);
        }
    }

    @SneakyThrows
    @Test
    void shouldNotFindByIdWithConnectionIfUserDoesNotExist() {
        userDao.save(buildUser());

        try (Connection connection = ConnectionBuilder.getConnection()) {
            Optional<User> actualResult = userDao.findById(34L, connection);

            assertThat(actualResult).isEmpty();
        }
    }


    private User buildUser() {
        return User.builder()
                .name("user")
                .email("user@example.com")
                .password("password")
                .gender(Gender.MALE)
                .birthDate(LocalDate.of(2000, 1, 1))
                .role(Role.USER)
                .image("image")
                .build();
    }
}