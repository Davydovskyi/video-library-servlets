package edu.jcourse.validator;

import edu.jcourse.dao.PersonDao;
import edu.jcourse.dto.CreatePersonDto;
import edu.jcourse.entity.Person;
import edu.jcourse.exception.DAOException;
import edu.jcourse.exception.ServiceException;
import edu.jcourse.util.CodeUtil;
import edu.jcourse.util.LocalDateFormatter;
import edu.jcourse.validator.impl.CreatePersonValidator;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreatePersonValidatorTest {
    @Mock
    private PersonDao personDAO;
    @InjectMocks
    private CreatePersonValidator createPersonValidator;

    @SneakyThrows
    @Test
    void shouldPassValidation() {
        CreatePersonDto createPersonDTO = CreatePersonDto.builder()
                .name("John")
                .birthDate("2015-01-01")
                .build();

        doReturn(Optional.empty()).when(personDAO)
                .findByNameAndBirthDate(any(), any());

        ValidationResult actualResult = createPersonValidator.validate(createPersonDTO);

        assertTrue(actualResult.isValid());
        verify(personDAO).findByNameAndBirthDate(createPersonDTO.name(), LocalDateFormatter.parse(createPersonDTO.birthDate()));
    }

    @SneakyThrows
    @Test
    void shouldNotPassValidationIfPersonAlreadyExists() {
        CreatePersonDto createPersonDTO = CreatePersonDto.builder()
                .name("John")
                .birthDate("2015-01-01")
                .build();

        doReturn(Optional.of(Person.builder().build())).when(personDAO)
                .findByNameAndBirthDate(any(), any());

        ValidationResult actualResult = createPersonValidator.validate(createPersonDTO);

        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(CodeUtil.EXIST_PERSON_CODE);
        verify(personDAO).findByNameAndBirthDate(createPersonDTO.name(), LocalDateFormatter.parse(createPersonDTO.birthDate()));
    }

    @SneakyThrows
    @Test
    void shouldThrowServiceExceptionIfDaoThrowsException() {
        CreatePersonDto createPersonDTO = CreatePersonDto.builder()
                .name("John")
                .birthDate("2015-01-01")
                .build();

        doThrow(DAOException.class).when(personDAO).findByNameAndBirthDate(any(), any());

        assertThrowsExactly(ServiceException.class, () -> createPersonValidator.validate(createPersonDTO));

        verify(personDAO).findByNameAndBirthDate(createPersonDTO.name(), LocalDateFormatter.parse(createPersonDTO.birthDate()));
    }

    @SneakyThrows
    @Test
    void invalidName() {
        CreatePersonDto createPersonDTO = CreatePersonDto.builder()
                .name("")
                .birthDate("2015-01-01")
                .build();

        ValidationResult actualResult = createPersonValidator.validate(createPersonDTO);

        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(CodeUtil.INVALID_NAME_CODE);
        verifyNoInteractions(personDAO);
    }

    @SneakyThrows
    @Test
    void invalidBirthday() {
        CreatePersonDto createPersonDTO = CreatePersonDto.builder()
                .name("John")
                .birthDate("")
                .build();

        ValidationResult actualResult = createPersonValidator.validate(createPersonDTO);

        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(CodeUtil.INVALID_BIRTHDAY_CODE);
        verifyNoInteractions(personDAO);
    }

    @SneakyThrows
    @Test
    void invalidNameAndBirthday() {
        CreatePersonDto createPersonDTO = CreatePersonDto.builder()
                .name("")
                .birthDate("")
                .build();

        ValidationResult actualResult = createPersonValidator.validate(createPersonDTO);
        assertThat(actualResult.getErrors()).hasSize(2);
        List<String> errorCodes = actualResult.getErrors().stream()
                .map(Error::getCode)
                .toList();
        assertThat(errorCodes).contains(CodeUtil.INVALID_NAME_CODE, CodeUtil.INVALID_BIRTHDAY_CODE);
        verifyNoInteractions(personDAO);
    }
}