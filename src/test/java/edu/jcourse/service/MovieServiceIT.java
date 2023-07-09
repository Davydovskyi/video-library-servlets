package edu.jcourse.service;

import edu.jcourse.dto.CreateMovieDto;
import edu.jcourse.dto.CreateMoviePersonDto;
import edu.jcourse.dto.MovieFilterDto;
import edu.jcourse.dto.ReceiveMovieDto;
import edu.jcourse.entity.Genre;
import edu.jcourse.entity.PersonRole;
import edu.jcourse.exception.ValidationException;
import edu.jcourse.integration.IntegrationTestBase;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

class MovieServiceIT extends IntegrationTestBase {

    private final MovieService movieService = ServiceProvider.getInstance().getMovieService();

    @SneakyThrows
    @Test
    void create() {
        CreateMovieDto createMovieDto = buildCreateMovieDto("2000");

        Long id = movieService.create(createMovieDto);

        assertThat(id).isNotNull();
    }

    @SneakyThrows
    @Test
    void shouldThrowValidationExceptionIfMovieDtoIsInvalid() {
        CreateMovieDto createMovieDto = buildCreateMovieDto("dummy");

        ValidationException actualResult = assertThrowsExactly(ValidationException.class, () -> movieService.create(createMovieDto));
        assertThat(actualResult.getErrors()).hasSize(1);
    }

    @SneakyThrows
    @Test
    void findMoviesByMovieFilter() {
        Long movieId1 = movieService.create(buildCreateMovieDto("2000"));
        Long movieId2 = movieService.create(buildCreateMovieDto("2001"));

        MovieFilterDto movieFilterDto = buildMovieFilterDto();

        List<ReceiveMovieDto> receiveMovieDtos = movieService.findMovies(movieFilterDto);

        assertThat(receiveMovieDtos).hasSize(2);
        List<Long> movieIds = receiveMovieDtos.stream()
                .map(ReceiveMovieDto::id)
                .toList();
        assertThat(movieIds).contains(movieId1, movieId2);
    }

    @SneakyThrows
    @Test
    void shouldThrowValidationExceptionIfMovieFilterIsInvalid() {
        MovieFilterDto movieFilterDto = MovieFilterDto.builder()
                .title("Movie")
                .country("US")
                .releaseYear("200")
                .genre("genre")
                .offset(0)
                .limit(1)
                .build();

        ValidationException actualResult = assertThrowsExactly(ValidationException.class, () -> movieService.findMovies(movieFilterDto));
        assertThat(actualResult.getErrors()).hasSize(2);
    }

    @SneakyThrows
    @Test
    void findById() {
        Long movieId1 = movieService.create(buildCreateMovieDto("2000"));

        Optional<ReceiveMovieDto> receiveMovieDto = movieService.findById(movieId1);

        assertThat(receiveMovieDto).isNotNull();
    }

    @SneakyThrows
    @Test
    void shouldNotFindByIdIfMovieDoesNotExist() {
        movieService.create(buildCreateMovieDto("2000"));

        Optional<ReceiveMovieDto> receiveMovieDto = movieService.findById(34L);

        assertThat(receiveMovieDto).isNotPresent();
    }

    @SneakyThrows
    @Test
    void findByPersonId() {
        Long movieId1 = movieService.create(buildCreateMovieDto("2000"));
        Long movieId2 = movieService.create(buildCreateMovieDto("2001"));

        List<ReceiveMovieDto> receiveMovieDtos = movieService.findByPersonId(1L);

        assertThat(receiveMovieDtos).hasSize(2);
        List<Long> movieIds = receiveMovieDtos.stream()
               .map(ReceiveMovieDto::id)
               .toList();
        assertThat(movieIds).contains(movieId1, movieId2);
    }

    @SneakyThrows
    @Test
    void shouldNotFindByPersonIdIfMovieDoesNotExist() {
        movieService.create(buildCreateMovieDto("2000"));

        List<ReceiveMovieDto> receiveMovieDtos = movieService.findByPersonId(34L);

        assertThat(receiveMovieDtos).isEmpty();
    }

    private MovieFilterDto buildMovieFilterDto() {
        return MovieFilterDto.builder()
                .title("Movie")
                .releaseYear("")
                .country("")
                .genre(null)
                .limit(100)
                .offset(0)
                .build();
    }

    private CreateMovieDto buildCreateMovieDto(String releaseYear) {
        return CreateMovieDto.builder()
                .title("Movie")
                .releaseYear(releaseYear)
                .country("US")
                .genre(Genre.ACTION.name())
                .description("Movie description")
                .moviePersons(Set.of(buildCreateMoviePersonDto()))
                .build();
    }

    private CreateMoviePersonDto buildCreateMoviePersonDto() {
        return CreateMoviePersonDto.builder()
                .personId("1")
                .personRole(PersonRole.ACTOR.name())
                .build();
    }

}