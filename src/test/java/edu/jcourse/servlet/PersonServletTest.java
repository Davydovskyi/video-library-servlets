package edu.jcourse.servlet;

import edu.jcourse.dto.ReceiveMovieDto;
import edu.jcourse.dto.ReceivePersonDto;
import edu.jcourse.service.MovieService;
import edu.jcourse.service.PersonService;
import edu.jcourse.util.JSPHelper;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServletTest {
    @Mock
    private PersonService personService;
    @Mock
    private MovieService movieService;
    @Mock
    private HttpServletRequest req;
    @Mock
    private HttpServletResponse resp;
    @Mock
    private RequestDispatcher reqDispatcher;
    @Mock
    private HttpSession session;
    @InjectMocks
    private PersonServlet personServlet;

    @SneakyThrows
    @Test
    void doGet() {
        doReturn("person/1").when(req).getPathInfo();
        ReceivePersonDto personDTO = buildReceivePersonDto();
        doReturn(Optional.of(personDTO)).when(personService).findById(any());
        doReturn(reqDispatcher).when(req).getRequestDispatcher(any());
        List<ReceiveMovieDto> receiveMovieDtos = List.of(buildReceiveMovieDto());
        doReturn(receiveMovieDtos).when(movieService).findByPersonId(any());
        doReturn(session).when(req).getSession();

        personServlet.doGet(req, resp);

        verify(req).getPathInfo();
        verify(personService).findById(1L);
        verify(req).setAttribute("person", personDTO);
        verify(movieService).findByPersonId(1L);
        verify(req).setAttribute("movies", receiveMovieDtos);
        verify(req).getSession();
        verify(session).setAttribute("person-movies", receiveMovieDtos);
        verify(req).getRequestDispatcher(JSPHelper.getPath("person"));
        verify(reqDispatcher).forward(req, resp);
    }

    @SneakyThrows
    @Test
    void doGetWhenPersonNotFound() {
        doReturn("person/1").when(req).getPathInfo();
        doReturn(Optional.empty()).when(personService).findById(any());
        doReturn(reqDispatcher).when(req).getRequestDispatcher(any());

        personServlet.doGet(req, resp);

        verify(req).getPathInfo();
        verify(personService).findById(1L);
        verify(resp).setStatus(404);
        verify(req).getRequestDispatcher(JSPHelper.getPath("person"));
        verify(reqDispatcher).forward(req, resp);
        verify(req, never()).setAttribute(eq("person"), any());
        verify(req, never()).setAttribute(eq("movies"), any());
        verifyNoInteractions(movieService, session);
    }

    private ReceivePersonDto buildReceivePersonDto() {
        return ReceivePersonDto.builder()
                .id(1L)
                .build();
    }

    private ReceiveMovieDto buildReceiveMovieDto() {
        return ReceiveMovieDto.builder()
                .id(1L)
                .build();
    }
}