package edu.jcourse.service;

import edu.jcourse.dto.CreatePersonDto;
import edu.jcourse.dto.ReceivePersonDto;
import edu.jcourse.entity.Person;
import edu.jcourse.exception.ValidationException;
import edu.jcourse.integration.IntegrationTestBase;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

class PersonServiceIT extends IntegrationTestBase {

    private final PersonService personService = ServiceProvider.getInstance().getPersonService();

    @SneakyThrows
    @Test
    void create() {
        CreatePersonDto createPersonDto = buildCreatePersonDto("person");

        Person actualResult = personService.create(createPersonDto);

        assertThat(actualResult.getId()).isNotNull();
    }

    @SneakyThrows
    @Test
    void shouldThrowValidationExceptionIfPersonDtoIsInvalid() {
        CreatePersonDto createPersonDto = buildCreatePersonDto("");

        ValidationException actualResult = assertThrowsExactly(ValidationException.class, () -> personService.create(createPersonDto));

        assertThat(actualResult.getErrors()).hasSize(1);
    }

    @SneakyThrows
    @Test
    void findAll() {
        personService.create(buildCreatePersonDto("person"));
        personService.create(buildCreatePersonDto("person2"));

        List<ReceivePersonDto> actualResult = personService.findAll();

        assertThat(actualResult).hasSize(3);
    }

    @SneakyThrows
    @Test
    void findById() {
        personService.create(buildCreatePersonDto("person"));

        Optional<ReceivePersonDto> actualResult = personService.findById(2L);

        assertThat(actualResult).isPresent();
    }

    @SneakyThrows
    @Test
    void shouldNotFindByIdIfPersonDoesNotExist() {
        personService.create(buildCreatePersonDto("person"));

        Optional<ReceivePersonDto> actualResult = personService.findById(24L);

        assertThat(actualResult).isNotPresent();
    }

    private CreatePersonDto buildCreatePersonDto(String name) {
        return CreatePersonDto.builder()
                .name(name)
                .birthDate("2015-05-05")
                .build();
    }
}