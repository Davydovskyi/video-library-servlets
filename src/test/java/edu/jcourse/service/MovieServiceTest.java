package edu.jcourse.service;

import edu.jcourse.dao.MovieDao;
import edu.jcourse.dto.CreateMovieDto;
import edu.jcourse.dto.ReceiveMovieDto;
import edu.jcourse.entity.Genre;
import edu.jcourse.entity.Movie;
import edu.jcourse.exception.DAOException;
import edu.jcourse.exception.ServiceException;
import edu.jcourse.exception.ValidationException;
import edu.jcourse.mapper.impl.CreateMovieMapper;
import edu.jcourse.mapper.impl.MovieMapper;
import edu.jcourse.service.impl.MovieServiceImpl;
import edu.jcourse.util.CodeUtil;
import edu.jcourse.util.MessageUtil;
import edu.jcourse.validator.Error;
import edu.jcourse.validator.ValidationResult;
import edu.jcourse.validator.impl.CreateMovieValidator;
import edu.jcourse.validator.impl.MovieFilterValidation;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {
    @Mock
    private MovieDao movieDao;
    @Mock
    private CreateMovieMapper createMovieMapper;
    @Mock
    private CreateMovieValidator createMovieValidator;
    @Mock
    private MovieFilterValidation movieFilterValidation;
    @Mock
    private MovieMapper movieMapper;
    @InjectMocks
    private MovieServiceImpl movieService;
    @Captor
    private ArgumentCaptor<Movie> movieCaptor;
    @Captor
    private ArgumentCaptor<CreateMovieDto> createMovieDtoCaptor;

    @SneakyThrows
    @Test
    void createMovie() {
        CreateMovieDto createMovieDto = buildCreateMovieDto();
        doReturn(new ValidationResult()).when(createMovieValidator).validate(createMovieDto);

        Movie movie = buildMovie("title", 2020);
        doReturn(movie).when(createMovieMapper).mapFrom(createMovieDto);

        doReturn(movie).when(movieDao).save(any());

        Long actualResult = movieService.create(createMovieDto);

        assertThat(actualResult).isEqualTo(movie.getId());

        verify(movieDao).save(movieCaptor.capture());
        assertThat(movieCaptor.getValue()).isEqualTo(movie);
        verify(createMovieValidator).validate(createMovieDtoCaptor.capture());
        assertThat(createMovieDtoCaptor.getValue()).isEqualTo(createMovieDto);
        verify(createMovieMapper).mapFrom(createMovieDtoCaptor.capture());
        assertThat(createMovieDtoCaptor.getValue()).isEqualTo(createMovieDto);
    }

    @SneakyThrows
    @Test
    void createShouldThrowExceptionIfMovieDtoIsInvalid() {
        ValidationResult validationResult = new ValidationResult();
        validationResult.add(Error.of(CodeUtil.INVALID_TITLE_CODE, MessageUtil.TITLE_INVALID_MESSAGE));

        doReturn(validationResult).when(createMovieValidator).validate(any());

        assertThrowsExactly(ValidationException.class, () -> movieService.create(any()));

        verifyNoInteractions(movieDao, createMovieMapper);
        verify(createMovieValidator).validate(any());
    }

    @SneakyThrows
    @Test
    void createShouldServiceThrowExceptionIfMovieDaoThrowsException() {
        doReturn(new ValidationResult()).when(createMovieValidator).validate(any());

        Movie movie = buildMovie("title", 2020);
        CreateMovieDto createMovieDto = buildCreateMovieDto();
        doReturn(movie).when(createMovieMapper).mapFrom(any());
        doThrow(DAOException.class).when(movieDao).save(any());

        assertThrowsExactly(ServiceException.class, () -> movieService.create(createMovieDto));
        verify(movieDao).save(movieCaptor.capture());
        assertThat(movieCaptor.getValue()).isEqualTo(movie);
        verify(createMovieValidator).validate(createMovieDtoCaptor.capture());
        assertThat(createMovieDtoCaptor.getValue()).isEqualTo(createMovieDto);
        verify(createMovieMapper).mapFrom(createMovieDtoCaptor.capture());
        assertThat(createMovieDtoCaptor.getValue()).isEqualTo(createMovieDto);
    }

    @SneakyThrows
    @Test
    void findMovies() {
        doReturn(new ValidationResult()).when(movieFilterValidation).validate(any());
        List<Movie> movies = List.of(buildMovie("title", 2020),
                buildMovie("title2", 2021));

        doReturn(movies).when(movieDao).findAll(any());

        List<ReceiveMovieDto> expectedResult = List.of(
                buildReceiveMovieDto("title", 2020),
                buildReceiveMovieDto("title2", 2021)
        );

        when(movieMapper.mapFrom(any())).thenReturn(expectedResult.get(0))
                .thenReturn(expectedResult.get(1));

        List<ReceiveMovieDto> actualResult = movieService.findMovies(any());

        assertThat(actualResult).isEqualTo(expectedResult);
        verify(movieDao).findAll(any());
        verify(movieFilterValidation).validate(any());
        verify(movieMapper, times(2)).mapFrom(any());
        verify(movieMapper).mapFrom(movies.get(0));
        verify(movieMapper).mapFrom(movies.get(1));
    }

    @SneakyThrows
    @Test
    void findMoviesShouldTrowExceptionIfFilterDtoIsInvalid() {
        ValidationResult validationResult = new ValidationResult();
        validationResult.add(Error.of(CodeUtil.INVALID_TITLE_CODE, MessageUtil.TITLE_INVALID_MESSAGE));

        doReturn(validationResult).when(movieFilterValidation).validate(any());

        assertThrowsExactly(ValidationException.class, () -> movieService.findMovies(any()));

        verifyNoInteractions(movieDao, movieMapper);
        verify(movieFilterValidation).validate(any());
    }

    @SneakyThrows
    @Test
    void shouldReturnEmptyListIfNoMoviesFound() {
        doReturn(new ValidationResult()).when(movieFilterValidation).validate(any());
        doReturn(List.of()).when(movieDao).findAll(any());

        List<ReceiveMovieDto> actualResult = movieService.findMovies(any());

        assertThat(actualResult).isEmpty();
        verify(movieDao).findAll(any());
        verify(movieFilterValidation).validate(any());
        verifyNoInteractions(movieMapper);
    }

    @SneakyThrows
    @Test
    void findMoviesShouldTrowServiceExceptionIfDaoThrowsException() {
        doReturn(new ValidationResult()).when(movieFilterValidation).validate(any());

        doThrow(DAOException.class).when(movieDao).findAll(any());

        assertThrowsExactly(ServiceException.class, () -> movieService.findMovies(any()));
        verify(movieDao).findAll(any());
        verify(movieFilterValidation).validate(any());
        verifyNoInteractions(movieMapper);
    }

    @SneakyThrows
    @Test
    void findById() {
        Movie movie = buildMovie("title", 2020);
        doReturn(Optional.of(movie)).when(movieDao).findById(1L);

        ReceiveMovieDto expectedResult = buildReceiveMovieDto("title", 2020);
        doReturn(expectedResult).when(movieMapper).mapFrom(any());

        Optional<ReceiveMovieDto> actualResult = movieService.findById(1L);

        assertThat(actualResult).isPresent()
                .contains(expectedResult);
        verify(movieDao).findById(1L);
        verify(movieMapper).mapFrom(movie);
    }

    @SneakyThrows
    @Test
    void findByIdShouldThrowServiceExceptionIfDaoThrowsException() {
        doThrow(DAOException.class).when(movieDao).findById(1L);

        assertThrowsExactly(ServiceException.class, () -> movieService.findById(1L));

        verify(movieDao).findById(1L);
        verifyNoInteractions(movieMapper);
    }

    @SneakyThrows
    @Test
    void findByPersonId() {
        List<Movie> movies = List.of(buildMovie("title", 2020),
                buildMovie("title2", 2021));
        doReturn(movies).when(movieDao).findAllByPersonId(1L);

        List<ReceiveMovieDto> expectedResult = List.of(
                buildReceiveMovieDto("title", 2020),
                buildReceiveMovieDto("title2", 2021)
        );

        when(movieMapper.mapFrom(any())).thenReturn(expectedResult.get(0))
                .thenReturn(expectedResult.get(1));

        List<ReceiveMovieDto> actualResult = movieService.findByPersonId(1L);

        assertThat(actualResult).isEqualTo(expectedResult);
        verify(movieDao).findAllByPersonId(1L);
        verify(movieMapper, times(2)).mapFrom(any());
        verify(movieMapper).mapFrom(movies.get(0));
        verify(movieMapper).mapFrom(movies.get(1));
    }

    @SneakyThrows
    @Test
    void findByPersonIdShouldThrowServiceExceptionIfDaoThrowsException() {
        doThrow(DAOException.class).when(movieDao).findAllByPersonId(1L);

        assertThrowsExactly(ServiceException.class, () -> movieService.findByPersonId(1L));

        verify(movieDao).findAllByPersonId(1L);
        verifyNoInteractions(movieMapper);
    }

    private Movie buildMovie(String title, int releaseYear) {
        return Movie.builder()
                .id(1L)
                .title(title)
                .releaseYear(releaseYear)
                .country("US")
                .description("Description")
                .genre(Genre.ACTION)
                .build();
    }

    private CreateMovieDto buildCreateMovieDto() {
        return CreateMovieDto.builder()
                .title("title")
                .releaseYear("2020")
                .country("US")
                .genre(Genre.ACTION.name())
                .description("Description")
                .build();
    }

    private ReceiveMovieDto buildReceiveMovieDto(String title, int releaseYear) {
        return ReceiveMovieDto.builder()
                .title(title)
                .releaseYear(releaseYear)
                .country("US")
                .genre(Genre.ACTION.name())
                .description("Description")
                .build();
    }
}