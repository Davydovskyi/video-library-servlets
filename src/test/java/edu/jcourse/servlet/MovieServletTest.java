package edu.jcourse.servlet;

import edu.jcourse.dto.ReceiveMovieDto;
import edu.jcourse.entity.Genre;
import edu.jcourse.exception.ValidationException;
import edu.jcourse.service.MovieService;
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
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServletTest {
    @Mock
    private MovieService movieService;
    @Mock
    private HttpServletRequest req;
    @Mock
    private HttpServletResponse resp;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private HttpSession session;
    @Spy
    @InjectMocks
    private MovieServlet movieServlet;

    @SneakyThrows
    @Test
    void doGet() {
        doReturn(requestDispatcher).when(req).getRequestDispatcher(any());

        movieServlet.doGet(req, resp);

        verify(req).setAttribute("genres", Genre.values());
        verify(req).getRequestDispatcher(JSPHelper.getPath("movies"));
        verify(requestDispatcher).forward(req, resp);
    }

    @SneakyThrows
    @Test
    void doPost() {
        doReturn(requestDispatcher).when(req).getRequestDispatcher(any());
        doReturn("Title").when(req).getParameter("title");
        doReturn("2000").when(req).getParameter("release_year");
        doReturn("USA").when(req).getParameter("country");
        doReturn("Action").when(req).getParameter("genre");
        doReturn("10").when(req).getParameter("limit");
        doReturn("0").when(req).getParameter("offset");
        List<ReceiveMovieDto> receiveMovieDtos = List.of(buildReceiveMovieDto());
        doReturn(receiveMovieDtos).when(movieService).findMovies(any());
        doReturn(session).when(req).getSession();

        movieServlet.doPost(req, resp);

        verify(req).getParameter("title");
        verify(req).getParameter("release_year");
        verify(req).getParameter("country");
        verify(req).getParameter("genre");
        verify(req).getParameter("limit");
        verify(req).getParameter("offset");
        verify(movieService).findMovies(any());
        verify(req).setAttribute("movies", receiveMovieDtos);
        verify(req).getSession();
        verify(session).setAttribute("movies", receiveMovieDtos);
        verify(movieServlet).doGet(req, resp);
        verify(req, never()).setAttribute(eq("errors"), any());
    }

    @SneakyThrows
    @Test
    void doPostWhenMovieServiceThrowsValidationException() {
        doReturn(requestDispatcher).when(req).getRequestDispatcher(any());
        doReturn("Title").when(req).getParameter("title");
        doReturn("2000").when(req).getParameter("release_year");
        doReturn("USA").when(req).getParameter("country");
        doReturn("Action").when(req).getParameter("genre");
        doReturn("10").when(req).getParameter("limit");
        doReturn("0").when(req).getParameter("offset");
        doThrow(ValidationException.class).when(movieService).findMovies(any());

        movieServlet.doPost(req, resp);

        verify(req).getParameter("title");
        verify(req).getParameter("release_year");
        verify(req).getParameter("country");
        verify(req).getParameter("genre");
        verify(req).getParameter("limit");
        verify(req).getParameter("offset");
        verify(movieService).findMovies(any());
        verify(req).setAttribute(eq("errors"), any());
        verify(movieServlet).doGet(req, resp);
        verify(req, never()).setAttribute(eq("movies"), any());
        verifyNoInteractions(session);
        verify(req, never()).getSession();
    }

    private ReceiveMovieDto buildReceiveMovieDto() {
        return ReceiveMovieDto.builder()
                .id(1L)
                .title("Title")
                .releaseYear(2000)
                .country("USA")
                .genre("Action")
                .description("Description")
                .build();
    }


}