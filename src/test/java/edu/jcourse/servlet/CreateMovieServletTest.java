package edu.jcourse.servlet;

import edu.jcourse.exception.ValidationException;
import edu.jcourse.service.MovieService;
import edu.jcourse.util.CodeUtil;
import edu.jcourse.util.UrlPath;
import jakarta.servlet.RequestDispatcher;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateMovieServletTest {

    @Mock
    private MovieService movieService;
    @Mock
    private HttpServletRequest req;
    @Mock
    private HttpServletResponse resp;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Spy
    @InjectMocks
    private CreateMovieServlet createMovieServlet;

    @BeforeEach
    void setUp() {
        createRequestStub();
        doReturn(requestDispatcher).when(req).getRequestDispatcher(UrlPath.ADMIN);
    }

    @SneakyThrows
    @Test
    void doGet() {
        doReturn(1L).when(movieService).create(any());

        createMovieServlet.doGet(req, resp);

        verify(createMovieServlet).doPost(req, resp);
    }

    @SneakyThrows
    @Test
    void doPost() {
        doReturn(1L).when(movieService).create(any());

        createMovieServlet.doPost(req, resp);

        verifyRequest();
        verify(movieService).create(any());
        verify(req).setAttribute("success", CodeUtil.SUCCESS_ADD_CODE);
        verify(req, never()).setAttribute(eq("errors"), any());
        verify(req).setAttribute("show_add_movie", "yes");
        verify(req).getRequestDispatcher(UrlPath.ADMIN);
        verify(requestDispatcher).forward(req, resp);
    }

    @SneakyThrows
    @Test
    void doPostWhenMovieServiceThrowsValidationException() {
        doThrow(ValidationException.class).when(movieService).create(any());

        createMovieServlet.doPost(req, resp);

        verifyRequest();
        verify(movieService).create(any());
        verify(req, never()).setAttribute("success", CodeUtil.SUCCESS_ADD_CODE);
        verify(req).setAttribute(eq("errors"), any());
        verify(req).setAttribute("show_add_movie", "yes");
        verify(req).getRequestDispatcher(UrlPath.ADMIN);
        verify(requestDispatcher).forward(req, resp);
    }

    private void verifyRequest() {
        verify(req).getParameter("member1");
        verify(req).getParameter("movie_role1");
        verify(req).getParameter("member2");
        verify(req).getParameter("movie_role2");
        verify(req).getParameter("member3");
        verify(req).getParameter("member4");
        verify(req).getParameter("title");
        verify(req).getParameter("release_year");
        verify(req).getParameter("country");
        verify(req).getParameter("genre");
        verify(req).getParameter("description");
    }

    private void createRequestStub() {
        doReturn("1").when(req).getParameter("member1");
        doReturn("dummy").when(req).getParameter("movie_role1");
        doReturn("2").when(req).getParameter("member2");
        doReturn("dummy").when(req).getParameter("movie_role2");
        doReturn(null).when(req).getParameter("member3");
        doReturn(null).when(req).getParameter("member4");
        doReturn("Title").when(req).getParameter("title");
        doReturn("2018").when(req).getParameter("release_year");
        doReturn("US").when(req).getParameter("country");
        doReturn("ACTION").when(req).getParameter("genre");
        doReturn("Description").when(req).getParameter("description");
    }
}