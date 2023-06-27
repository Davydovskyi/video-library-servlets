package edu.jcourse.dao;

import edu.jcourse.dto.MovieFilterDto;
import edu.jcourse.entity.*;
import edu.jcourse.integration.IntegrationTestBase;
import edu.jcourse.util.ConnectionBuilder;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

class MovieDaoIT extends IntegrationTestBase {

    private final MovieDao movieDao = DaoProvider.getInstance().getMovieDao();

    @Test
    void delete() {
        assertThrowsExactly(UnsupportedOperationException.class, () -> movieDao.delete(1L));
    }

    @SneakyThrows
    @Test
    void save() {
        Movie movie = buildMovie("Title", 2000);

        Movie actualResult = movieDao.save(movie);

        assertNotNull(actualResult.getId());
    }

    @Test
    void update() {
        assertThrowsExactly(UnsupportedOperationException.class, () -> movieDao.update(buildMovie("Title", 2000)));
    }

    @Test
    void findAll() {
        assertThrowsExactly(UnsupportedOperationException.class, movieDao::findAll);
    }

    @SneakyThrows
    @Test
    void findById() {
        Movie movie = movieDao.save(buildMovie("Title", 2000));

        Optional<Movie> actualResult = movieDao.findById(movie.getId());

        assertThat(actualResult).isPresent().contains(movie);
    }

    @SneakyThrows
    @Test
    void shouldNotFindByIdIfMovieDoesNotExist() {
        movieDao.save(buildMovie("Title", 2000));


        Optional<Movie> actualResult = movieDao.findById(23L);

        assertThat(actualResult).isEmpty();
    }

    @SneakyThrows
    @Test
    void findByAllFields() {
        Movie movie = movieDao.save(buildMovie("Title", 2000));
        movie.setMoviePersons(List.of());

        Optional<Movie> actualResult = movieDao.findByAllFields("Title", 2000, "US", Genre.ACTION);

        assertThat(actualResult).isPresent().contains(movie);
    }

    @SneakyThrows
    @Test
    void shouldNotFindByAllFieldsIfMovieDoesNotExist() {
        movieDao.save(buildMovie("Title", 2000));

        Optional<Movie> actualResult = movieDao.findByAllFields("Title1", 2000, "US", Genre.ACTION);

        assertThat(actualResult).isEmpty();
    }

    @SneakyThrows
    @Test
    void findAllByMovieFilter() {
        Movie movie1 = movieDao.save(buildMovie("Title", 2000));
        Movie movie2 = movieDao.save(buildMovie("Title2", 2000));
        Movie movie3 = movieDao.save(buildMovie("Title3", 2000));

        MovieFilterDto movieFilterDto = MovieFilterDto.builder()
                .title("Tit")
                .releaseYear("2000")
                .country("US")
                .genre(Genre.ACTION.name())
                .limit(100)
                .offset(0)
                .build();

        List<Movie> actualResult = movieDao.findAll(movieFilterDto);

        assertThat(actualResult).hasSize(3);
        List<Long> movieIds = actualResult.stream()
                .map(Movie::getId)
                .toList();
        assertThat(movieIds).contains(movie1.getId(), movie2.getId(), movie3.getId());
    }

    @SneakyThrows
    @Test
    void shouldNotFindAllByMovieFilterIfMovieDoesNotExist() {
        movieDao.save(buildMovie("Title", 2000));
        movieDao.save(buildMovie("Title2", 2000));

        MovieFilterDto movieFilterDto = MovieFilterDto.builder()
                .title("Tit")
                .releaseYear("2001")
                .country("US")
                .genre(Genre.ACTION.name())
                .limit(100)
                .offset(0)
                .build();

        List<Movie> actualResult = movieDao.findAll(movieFilterDto);

        assertThat(actualResult).isEmpty();
    }

    @SneakyThrows
    @Test
    void findAllByPersonId() {
        Movie movie1 = movieDao.save(buildMovie("Title", 2000));
        Movie movie2 = movieDao.save(buildMovie("Title2", 2000));
        Movie movie3 = movieDao.save(buildMovie("Title3", 2000));

        List<Movie> actualResult = movieDao.findAllByPersonId(1L);

        assertThat(actualResult).hasSize(3);
        List<Long> movieIds = actualResult.stream()
                .map(Movie::getId)
                .toList();
        assertThat(movieIds).contains(movie1.getId(), movie2.getId(), movie3.getId());
    }

    @SneakyThrows
    @Test
    void shouldNotFindAllByPersonIdIfMovieDoesNotExist() {
        movieDao.save(buildMovie("Title", 2000));

        List<Movie> actualResult = movieDao.findAllByPersonId(23L);

        assertThat(actualResult).isEmpty();
    }

    @SneakyThrows
    @Test
    void findByIdWithConnection() {
        Movie movie = movieDao.save(buildMovie("Title", 2000));
        movie.setMoviePersons(List.of());

        try (Connection connection = ConnectionBuilder.getConnection()) {
            Optional<Movie> actualResult = movieDao.findById(movie.getId(), connection);

            assertThat(actualResult).isPresent().contains(movie);
        }
    }

    @SneakyThrows
    @Test
    void shouldNotFindByIdWithConnectionIfMovieDoesNotExist() {
        movieDao.save(buildMovie("Title", 2000));

        try (Connection connection = ConnectionBuilder.getConnection()) {
            Optional<Movie> actualResult = movieDao.findById(23L, connection);

            assertThat(actualResult).isEmpty();
        }
    }


    private Movie buildMovie(String title, int releaseYear) {
        Movie movie = Movie.builder()
                .title(title)
                .releaseYear(releaseYear)
                .country("US")
                .genre(Genre.ACTION)
                .description("Description")
                .reviews(List.of())
                .build();
        movie.setMoviePersons(List.of(buildMoviePerson(movie, 1L)));
        return movie;
    }

    private MoviePerson buildMoviePerson(Movie movie, Long personId) {
        return MoviePerson.builder()
                .movie(movie)
                .person(buildPerson(personId))
                .personRole(PersonRole.ACTOR)
                .build();
    }

    private Person buildPerson(Long personId) {
        return Person.builder()
                .id(personId)
                .name("Person1")
                .birthDate(LocalDate.of(1990, 1, 1))
                .build();
    }
}