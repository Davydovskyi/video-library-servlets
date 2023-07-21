package edu.jcourse.servlet;

import edu.jcourse.dto.CreatePersonDto;
import edu.jcourse.dto.ReceivePersonDto;
import edu.jcourse.entity.Person;
import edu.jcourse.exception.ValidationException;
import edu.jcourse.mapper.impl.PersonMapper;
import edu.jcourse.service.PersonService;
import edu.jcourse.util.CodeUtil;
import edu.jcourse.util.UrlPath;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreatePersonServletTest {

    @Mock
    private PersonService personService;
    @Mock
    private PersonMapper personMapper;
    @Mock
    private HttpServletRequest req;
    @Mock
    private HttpServletResponse resp;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ServletContext servletContext;
    @Spy
    @InjectMocks
    private CreatePersonServlet createPersonServlet;

    @BeforeEach
    void setUp() {
        doReturn("Aron").when(req).getParameter("name");
        doReturn("2020-01-01").when(req).getParameter("birthday");
    }

    @SneakyThrows
    @Test
    void doGet() {
        CreatePersonDto createPersonDto = buildCreatePersonDto();
        Person person = buildPerson();
        doReturn(person).when(personService).create(any());
        doReturn(servletContext).when(req).getServletContext();
        CopyOnWriteArrayList<ReceivePersonDto> persons = new CopyOnWriteArrayList<>(List.of(buildReceivePersonDto(1L, "Mike")));
        doReturn(persons).when(servletContext).getAttribute("filmMembers");
        ReceivePersonDto personDto2 = buildReceivePersonDto(2L, "Aron");
        doReturn(personDto2).when(personMapper).mapFrom(any());
        doReturn(requestDispatcher).when(req).getRequestDispatcher(UrlPath.ADMIN);

        createPersonServlet.doGet(req, resp);

        verify(createPersonServlet).doPost(req, resp);
    }

    @SneakyThrows
    @Test
    void doPost() {
        CreatePersonDto createPersonDto = buildCreatePersonDto();
        Person person = buildPerson();
        doReturn(person).when(personService).create(any());
        doReturn(servletContext).when(req).getServletContext();
        CopyOnWriteArrayList<ReceivePersonDto> persons = new CopyOnWriteArrayList<>(List.of(buildReceivePersonDto(1L, "Mike")));
        doReturn(persons).when(servletContext).getAttribute("filmMembers");
        ReceivePersonDto personDto2 = buildReceivePersonDto(2L, "Aron");
        doReturn(personDto2).when(personMapper).mapFrom(any());
        doReturn(requestDispatcher).when(req).getRequestDispatcher(UrlPath.ADMIN);

        createPersonServlet.doPost(req, resp);

        verify(req).getParameter("name");
        verify(req).getParameter("birthday");
        verify(personService).create(createPersonDto);
        verify(req).setAttribute("success", CodeUtil.SUCCESS_ADD_CODE);
        verify(req).getServletContext();
        verify(servletContext).getAttribute("filmMembers");
        verify(personMapper).mapFrom(any());
        verify(req, never()).setAttribute(eq("errors"), any());
        verify(req).setAttribute("show_add_person", "yes");
        verify(requestDispatcher).forward(req, resp);
        assertThat(persons).hasSize(2);
        List<Long> personIds = persons.stream()
                .map(ReceivePersonDto::id)
                .toList();
        assertThat(personIds).containsExactly(2L, 1L);
    }

    @SneakyThrows
    @Test
    void doPostWhenPersonServiceThrowsValidationException() {
        doThrow(ValidationException.class).when(personService).create(any());
        doReturn(requestDispatcher).when(req).getRequestDispatcher(UrlPath.ADMIN);

        createPersonServlet.doPost(req, resp);

        verify(req).getParameter("name");
        verify(req).getParameter("birthday");
        verify(personService).create(any());
        verify(req, never()).setAttribute("success", CodeUtil.SUCCESS_ADD_CODE);
        verify(req, never()).getServletContext();
        verify(req).setAttribute(eq("errors"), any());
        verify(req).setAttribute("show_add_person", "yes");
        verify(requestDispatcher).forward(req, resp);
    }

    private CreatePersonDto buildCreatePersonDto() {
        return CreatePersonDto.builder()
                .name("Aron")
                .birthDate("2020-01-01")
                .build();
    }

    private ReceivePersonDto buildReceivePersonDto(Long id, String name) {
        return ReceivePersonDto.builder()
                .id(id)
                .personData(name + "(2020)")
                .build();
    }

    private Person buildPerson() {
        return Person.builder()
                .id(2L)
                .name("Aron")
                .birthDate(LocalDate.of(2020, 1, 1))
                .build();
    }
}