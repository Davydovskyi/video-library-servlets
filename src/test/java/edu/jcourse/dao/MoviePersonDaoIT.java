package edu.jcourse.dao;

import edu.jcourse.entity.Movie;
import edu.jcourse.entity.MoviePerson;
import edu.jcourse.entity.Person;
import edu.jcourse.entity.PersonRole;
import edu.jcourse.integration.IntegrationTestBase;
import edu.jcourse.util.ConnectionBuilder;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

class MoviePersonDaoIT extends IntegrationTestBase {

    private final MoviePersonDao moviePersonDao = DaoProvider.getInstance().getMoviePersonDao();

    @Test
    void delete() {
        assertThrowsExactly(UnsupportedOperationException.class, () -> moviePersonDao.delete(1L));
    }

    @SneakyThrows
    @Test
    void save() {
        MoviePerson moviePerson = buildMoviePerson(PersonRole.ACTOR);

        MoviePerson actualResult = moviePersonDao.save(moviePerson);

        assertThat(actualResult.getId()).isNotNull();
    }

    @SneakyThrows
    @Test
    void update() {
        assertThrowsExactly(UnsupportedOperationException.class, () -> moviePersonDao.update(buildMoviePerson(PersonRole.ACTOR)));
    }

    @SneakyThrows
    @Test
    void findAll() {
        assertThrowsExactly(UnsupportedOperationException.class, moviePersonDao::findAll);
    }

    @SneakyThrows
    @Test
    void findById() {
        assertThrowsExactly(UnsupportedOperationException.class, () -> moviePersonDao.findById(1L));
    }

    @SneakyThrows
    @Test
    void saveWithConnection() {
        MoviePerson moviePerson = buildMoviePerson(PersonRole.ACTOR);

        try (Connection connection = ConnectionBuilder.getConnection()) {
            moviePersonDao.save(moviePerson, connection);

            assertThat(moviePerson.getId()).isNotNull();
        }
    }

    @SneakyThrows
    @Test
    void findAllByMovieId() {
        MoviePerson moviePerson1 = moviePersonDao.save(buildMoviePerson(PersonRole.ACTOR));
        MoviePerson moviePerson2 = moviePersonDao.save(buildMoviePerson(PersonRole.DIRECTOR));
        MoviePerson moviePerson3 = moviePersonDao.save(buildMoviePerson(PersonRole.PRODUCER));

        List<MoviePerson> actualResult = moviePersonDao.findAllByMovieId(1L, ConnectionBuilder.getConnection());

        assertThat(actualResult).hasSize(3);
        List<Long> moviePersonIds = actualResult.stream()
                .map(MoviePerson::getId)
                .toList();
        assertThat(moviePersonIds).contains(moviePerson1.getId(), moviePerson2.getId(), moviePerson3.getId());
    }

    @SneakyThrows
    @Test
    void shouldNotFindByMovieIdIfMoviePersonDoesNotExist() {
        moviePersonDao.save(buildMoviePerson(PersonRole.ACTOR));

        List<MoviePerson> actualResult = moviePersonDao.findAllByMovieId(23L, ConnectionBuilder.getConnection());

        assertThat(actualResult).isEmpty();
    }


    private MoviePerson buildMoviePerson(PersonRole personRole) {
        return MoviePerson.builder()
                .person(Person.builder().id(1L).build())
                .personRole(personRole)
                .movie(Movie.builder().id(1L).build())
                .build();
    }
}