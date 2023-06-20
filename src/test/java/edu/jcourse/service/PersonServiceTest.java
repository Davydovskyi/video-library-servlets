package edu.jcourse.service;

import edu.jcourse.dao.PersonDao;
import edu.jcourse.dto.CreatePersonDto;
import edu.jcourse.dto.ReceivePersonDto;
import edu.jcourse.entity.Person;
import edu.jcourse.exception.DAOException;
import edu.jcourse.exception.ServiceException;
import edu.jcourse.exception.ValidationException;
import edu.jcourse.mapper.impl.CreatePersonMapper;
import edu.jcourse.mapper.impl.PersonMapper;
import edu.jcourse.service.impl.PersonServiceImpl;
import edu.jcourse.util.CodeUtil;
import edu.jcourse.util.MessageUtil;
import edu.jcourse.validator.Error;
import edu.jcourse.validator.ValidationResult;
import edu.jcourse.validator.impl.CreatePersonValidator;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {
    @Mock
    private PersonDao personDao;
    @Mock
    private CreatePersonValidator createPersonValidator;
    @Mock
    private CreatePersonMapper createPersonMapper;
    @Mock
    private PersonMapper personMapper;
    @InjectMocks
    private PersonServiceImpl personService;
    @Captor
    private ArgumentCaptor<CreatePersonDto> createPersonDtoArgumentCaptor;
    @Captor
    private ArgumentCaptor<Person> personArgumentCaptor;

    @SneakyThrows
    @Test
    void create() {
        doReturn(new ValidationResult()).when(createPersonValidator).validate(any());
        CreatePersonDto createPersonDto = buildCreatePersonDto();

        Person person = buildPerson(1L, "name");
        doReturn(person).when(createPersonMapper).mapFrom(any());
        doReturn(person).when(personDao).save(any());

        Person actualResult = personService.create(createPersonDto);

        assertThat(actualResult).isEqualTo(person);
        verify(createPersonValidator).validate(createPersonDtoArgumentCaptor.capture());
        assertThat(createPersonDtoArgumentCaptor.getValue()).isEqualTo(createPersonDto);
        verify(createPersonMapper).mapFrom(createPersonDtoArgumentCaptor.capture());
        assertThat(createPersonDtoArgumentCaptor.getValue()).isEqualTo(createPersonDto);
        verify(personDao).save(personArgumentCaptor.capture());
        assertThat(personArgumentCaptor.getValue()).isEqualTo(actualResult);
    }

    @SneakyThrows
    @Test
    void createShouldThrowExceptionIfPersonDtoIsInvalid() {
        ValidationResult validationResult = new ValidationResult();
        validationResult.add(Error.of(CodeUtil.INVALID_NAME_CODE, MessageUtil.NAME_INVALID_MESSAGE));
        doReturn(validationResult).when(createPersonValidator).validate(any());

        assertThrowsExactly(ValidationException.class, () -> personService.create(buildCreatePersonDto()));

        verify(createPersonValidator).validate(any());
        verifyNoInteractions(personDao, createPersonMapper);
    }

    @SneakyThrows
    @Test
    void createShouldThrowServiceExceptionIfDaoThrowsException() {
        doReturn(new ValidationResult()).when(createPersonValidator).validate(any());

        CreatePersonDto createPersonDto = buildCreatePersonDto();
        Person person = buildPerson(1L, "name");
        doReturn(person).when(createPersonMapper).mapFrom(any());

        doThrow(DAOException.class).when(personDao).save(any());

        assertThrowsExactly(ServiceException.class, () -> personService.create(createPersonDto));
        verify(createPersonValidator).validate(createPersonDtoArgumentCaptor.capture());
        assertThat(createPersonDtoArgumentCaptor.getValue()).isEqualTo(createPersonDto);
        verify(personDao).save(personArgumentCaptor.capture());
        assertThat(personArgumentCaptor.getValue()).isEqualTo(person);
        verify(createPersonMapper).mapFrom(createPersonDtoArgumentCaptor.capture());
        assertThat(createPersonDtoArgumentCaptor.getValue()).isEqualTo(createPersonDto);
    }

    @SneakyThrows
    @Test
    void findAll() {
        List<Person> people = List.of(buildPerson(1L, "name"),
                buildPerson(2L, "name2"));
        doReturn(people).when(personDao).findAll();

        List<ReceivePersonDto> expectedResult = List.of(buildReceivePersonDto(1L, "name"),
                buildReceivePersonDto(2L, "name2"));

        when(personMapper.mapFrom(any())).thenReturn(expectedResult.get(0))
                .thenReturn(expectedResult.get(1));

        List<ReceivePersonDto> actualResult = personService.findAll();

        assertThat(actualResult).isEqualTo(expectedResult);
        verify(personDao).findAll();
        verify(personMapper, times(2)).mapFrom(any());
        verify(personMapper).mapFrom(people.get(0));
        verify(personMapper).mapFrom(people.get(1));
    }

    @SneakyThrows
    @Test
    void shouldReturnEmptyListIfNoPersonsFound() {
        doReturn(List.of()).when(personDao).findAll();

        List<ReceivePersonDto> actualResult = personService.findAll();

        assertThat(actualResult).isEmpty();
        verify(personDao).findAll();
        verify(personMapper, never()).mapFrom(any());
    }

    @SneakyThrows
    @Test
    void findAllShouldThrowServiceExceptionIfDaoThrowsException() {
        doThrow(DAOException.class).when(personDao).findAll();

        assertThrowsExactly(ServiceException.class, () -> personService.findAll());
        verify(personDao).findAll();
        verify(personMapper, never()).mapFrom(any());
    }

    @SneakyThrows
    @Test
    void findById() {
        Person person = buildPerson(1L, "name");
        doReturn(Optional.of(person)).when(personDao).findById(any());

        ReceivePersonDto expectedResult = buildReceivePersonDto(1L, "name");
        when(personMapper.mapFrom(any())).thenReturn(expectedResult);

        Optional<ReceivePersonDto> actualResult = personService.findById(1L);

        assertThat(actualResult).isPresent().contains(expectedResult);
        verify(personDao).findById(1L);
        verify(personMapper).mapFrom(person);
    }

    @SneakyThrows
    @Test
    void findByIdShouldThrowServiceExceptionIfDaoThrowsException() {
        doThrow(DAOException.class).when(personDao).findById(any());

        assertThrowsExactly(ServiceException.class, () -> personService.findById(1L));
        verify(personDao).findById(1L);
        verifyNoInteractions(personMapper);
    }

    private Person buildPerson(Long id, String name) {
        return Person.builder()
                .id(id)
                .name(name)
                .birthDate(LocalDate.of(2020, 1, 1))
                .build();
    }


    private CreatePersonDto buildCreatePersonDto() {
        return CreatePersonDto.builder()
                .name("name")
                .birthDate("2020-01-01")
                .build();
    }

    private ReceivePersonDto buildReceivePersonDto(Long id, String name) {
        return ReceivePersonDto.builder()
                .id(id)
                .personData(String.format("%s(%d)", name, 2020))
                .build();
    }
}