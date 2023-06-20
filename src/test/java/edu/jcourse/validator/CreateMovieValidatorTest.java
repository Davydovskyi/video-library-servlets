package edu.jcourse.validator;

import edu.jcourse.dao.MovieDao;
import edu.jcourse.dto.CreateMovieDto;
import edu.jcourse.dto.CreateMoviePersonDto;
import edu.jcourse.entity.Genre;
import edu.jcourse.entity.Movie;
import edu.jcourse.entity.PersonRole;
import edu.jcourse.exception.DAOException;
import edu.jcourse.exception.ServiceException;
import edu.jcourse.util.CodeUtil;
import edu.jcourse.validator.impl.CreateMovieValidator;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateMovieValidatorTest {

    @Mock
    private MovieDao movieDAO;
    @InjectMocks
    private CreateMovieValidator createMovieValidator;

    @SneakyThrows
    @Test
    void shouldPassValidation() {
        CreateMovieDto createMovieDTO = CreateMovieDto.builder()
                .title("Title")
                .releaseYear("2020")
                .country("USA")
                .genre(Genre.ROMANCE.name())
                .description("Description")
                .moviePersons(buildCreatePersonDTOs())
                .build();

        doReturn(Optional.empty()).when(movieDAO).findByAllFields(any(), any(), any(), any());

        ValidationResult actualResult = createMovieValidator.validate(createMovieDTO);

        assertTrue(actualResult.isValid());
        verify(movieDAO).findByAllFields(
                createMovieDTO.title(),
                Integer.parseInt(createMovieDTO.releaseYear()),
                createMovieDTO.country(),
                Genre.valueOf(createMovieDTO.genre()));
    }

    @SneakyThrows
    @Test
    void invalidTitle() {
        CreateMovieDto createMovieDTO = CreateMovieDto.builder()
                .title("")
                .releaseYear("2020")
                .country("USA")
                .genre(Genre.ROMANCE.name())
                .description("Description")
                .moviePersons(buildCreatePersonDTOs())
                .build();

        ValidationResult actualResult = createMovieValidator.validate(createMovieDTO);

        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(CodeUtil.INVALID_TITLE_CODE);
        verifyNoInteractions(movieDAO);
    }

    @SneakyThrows
    @Test
    void invalidReleaseYear() {
        CreateMovieDto createMovieDTO = CreateMovieDto.builder()
                .title("Title")
                .releaseYear("")
                .country("USA")
                .genre(Genre.ROMANCE.name())
                .description("Description")
                .moviePersons(buildCreatePersonDTOs())
                .build();

        ValidationResult actualResult = createMovieValidator.validate(createMovieDTO);

        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(CodeUtil.INVALID_RELEASE_YEAR_CODE);
        verifyNoInteractions(movieDAO);
    }

    @SneakyThrows
    @Test
    void invalidCountry() {
        CreateMovieDto createMovieDTO = CreateMovieDto.builder()
                .title("Title")
                .releaseYear("2020")
                .country("")
                .genre(Genre.ROMANCE.name())
                .description("Description")
                .moviePersons(buildCreatePersonDTOs())
                .build();

        ValidationResult actualResult = createMovieValidator.validate(createMovieDTO);

        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(CodeUtil.INVALID_COUNTRY_CODE);
        verifyNoInteractions(movieDAO);
    }

    @SneakyThrows
    @Test
    void invalidGenre() {
        CreateMovieDto createMovieDTO = CreateMovieDto.builder()
                .title("Title")
                .releaseYear("2020")
                .country("USA")
                .genre("")
                .description("Description")
                .moviePersons(buildCreatePersonDTOs())
                .build();

        ValidationResult actualResult = createMovieValidator.validate(createMovieDTO);

        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(CodeUtil.INVALID_GENRE_CODE);
        verifyNoInteractions(movieDAO);
    }

    @SneakyThrows
    @Test
    void invalidDescription() {
        CreateMovieDto createMovieDTO = CreateMovieDto.builder()
                .title("Title")
                .releaseYear("2020")
                .country("USA")
                .genre(Genre.ROMANCE.name())
                .description("")
                .moviePersons(buildCreatePersonDTOs())
                .build();

        ValidationResult actualResult = createMovieValidator.validate(createMovieDTO);

        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(CodeUtil.INVALID_DESCRIPTION_CODE);
        verifyNoInteractions(movieDAO);
    }

    @SneakyThrows
    @Test
    void invalidMoviePersonsIfItIsEmpty() {
        CreateMovieDto createMovieDTO = CreateMovieDto.builder()
                .title("Title")
                .releaseYear("2020")
                .country("USA")
                .genre(Genre.ROMANCE.name())
                .description("Description")
                .moviePersons(Set.of())
                .build();

        ValidationResult actualResult = createMovieValidator.validate(createMovieDTO);

        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(CodeUtil.EMPTY_MOVIE_PERSONS_CODE);
        verifyNoInteractions(movieDAO);
    }

    @SneakyThrows
    @Test
    void invalidMoviePersonsIfPersonIsInvalid() {
        CreateMovieDto createMovieDTO = CreateMovieDto.builder()
                .title("Title")
                .releaseYear("2020")
                .country("USA")
                .genre(Genre.ROMANCE.name())
                .description("Description")
                .moviePersons(Set.of(CreateMoviePersonDto.builder()
                        .personRole("invalid")
                        .build()))
                .build();

        ValidationResult actualResult = createMovieValidator.validate(createMovieDTO);

        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(CodeUtil.INVALID_MOVIE_PERSONS_CODE);
        verifyNoInteractions(movieDAO);
    }

    @SneakyThrows
    @Test
    void invalidAllFields() {
        CreateMovieDto createMovieDTO = CreateMovieDto.builder()
                .title("")
                .releaseYear("")
                .country("")
                .genre("invalid")
                .description("")
                .moviePersons(Set.of(CreateMoviePersonDto.builder()
                        .personRole("invalid")
                        .build()))
                .build();

        ValidationResult actualResult = createMovieValidator.validate(createMovieDTO);

        assertThat(actualResult.getErrors()).hasSize(6);
        List<String> errorCodes = actualResult.getErrors().stream()
                .map(Error::getCode)
                .toList();

        assertThat(errorCodes).contains(
                CodeUtil.INVALID_RELEASE_YEAR_CODE,
                CodeUtil.INVALID_COUNTRY_CODE,
                CodeUtil.INVALID_GENRE_CODE,
                CodeUtil.INVALID_DESCRIPTION_CODE,
                CodeUtil.INVALID_TITLE_CODE,
                CodeUtil.INVALID_MOVIE_PERSONS_CODE);

        verifyNoInteractions(movieDAO);
    }

    @Test
    @SneakyThrows
    void shouldNotPassValidationIfMovieAlreadyExists() {
        CreateMovieDto createMovieDTO = CreateMovieDto.builder()
                .title("Title")
                .releaseYear("2020")
                .country("USA")
                .genre(Genre.ROMANCE.name())
                .description("Description")
                .moviePersons(buildCreatePersonDTOs())
                .build();

        doReturn(Optional.of(Movie.builder().build())).when(movieDAO).findByAllFields(any(), any(), any(), any());

        ValidationResult actualResult = createMovieValidator.validate(createMovieDTO);

        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(CodeUtil.EXIST_MOVIE_CODE);

        verify(movieDAO).findByAllFields(
                createMovieDTO.title(),
                Integer.parseInt(createMovieDTO.releaseYear()),
                createMovieDTO.country(),
                Genre.valueOf(createMovieDTO.genre()));
    }

    @SneakyThrows
    @Test
    void shouldThrowServiceExceptionIfDaoThrowsException() {
        CreateMovieDto createMovieDTO = CreateMovieDto.builder()
                .title("Title")
                .releaseYear("2020")
                .country("USA")
                .genre(Genre.ROMANCE.name())
                .description("Description")
                .moviePersons(buildCreatePersonDTOs())
                .build();

        doThrow(DAOException.class).when(movieDAO).findByAllFields(any(), any(), any(), any());

        assertThrowsExactly(ServiceException.class, () -> createMovieValidator.validate(createMovieDTO));
        verify(movieDAO).findByAllFields(
                createMovieDTO.title(),
                Integer.parseInt(createMovieDTO.releaseYear()),
                createMovieDTO.country(),
                Genre.valueOf(createMovieDTO.genre()));
    }

    private Set<CreateMoviePersonDto> buildCreatePersonDTOs() {
        return Set.of(CreateMoviePersonDto.builder()
                .personRole(PersonRole.ACTOR.name())
                .build());
    }
}