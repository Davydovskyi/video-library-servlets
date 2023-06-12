package edu.jcourse.service;

import edu.jcourse.dao.MovieDao;
import edu.jcourse.dto.CreateMovieDto;
import edu.jcourse.entity.Genre;
import edu.jcourse.entity.Movie;
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

        Movie movie = buildMovie();
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
    void shouldThrowExceptionIfDtoIsInvalid() {
        ValidationResult validationResult = new ValidationResult();
        validationResult.add(Error.of(CodeUtil.INVALID_TITLE_CODE, MessageUtil.TITLE_INVALID_MESSAGE));

        doReturn(validationResult).when(createMovieValidator).validate(any());

        assertThrowsExactly(ValidationException.class, () -> movieService.create(any()));

        verifyNoInteractions(movieDao, createMovieMapper);
        verify(createMovieValidator).validate(any());
    }

    private Movie buildMovie() {
        return Movie.builder()
                .id(1L)
                .title("Title")
                .releaseYear(2020)
                .country("US")
                .description("Description")
                .genre(Genre.ACTION)
                .build();
    }

    private CreateMovieDto buildCreateMovieDto() {
        return CreateMovieDto.builder()
                .title("Title")
                .releaseYear("2020")
                .country("US")
                .genre(Genre.ACTION.name())
                .description("Description")
                .build();
    }

}