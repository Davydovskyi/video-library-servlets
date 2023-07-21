package edu.jcourse.servlet;

import edu.jcourse.dto.ReceiveMovieDto;
import edu.jcourse.dto.ReceiveReviewDto;
import edu.jcourse.dto.ReceiveUserDto;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieDetailsServletTest {
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
    @Spy
    @InjectMocks
    private MovieDetailsServlet movieDetailsServlet;

    @SneakyThrows
    @Test
    void doGetWhenReviewExists() {
        doReturn("movie/1").when(req).getPathInfo();
        ReceiveMovieDto movieDTO = buildReceiveMovieDto(1L);
        doReturn(Optional.of(movieDTO)).when(movieService).findById(any());
        doReturn(reqDispatcher).when(req).getRequestDispatcher(any());
        doReturn(session).when(req).getSession();
        ReceiveUserDto userDTO = buildReceiveUserDto(1L);
        doReturn(userDTO).when(session).getAttribute("user");

        movieDetailsServlet.doGet(req, resp);

        verify(req).getPathInfo();
        verify(req).setAttribute("movieId", 1L);
        verify(movieService).findById(1L);
        verify(req).setAttribute("movie", movieDTO);
        verify(req).getSession();
        verify(session).getAttribute("user");
        verify(req).setAttribute("review_exists", "yes");
        verify(req).getRequestDispatcher(JSPHelper.getPath("movie"));
        verify(reqDispatcher).forward(req, resp);
        verify(resp, never()).setStatus(404);
    }

    @SneakyThrows
    @Test
    void doGetWhenReviewDoesNotExist() {
        doReturn("movie/1").when(req).getPathInfo();
        ReceiveMovieDto movieDTO = buildReceiveMovieDto(1L);
        doReturn(Optional.of(movieDTO)).when(movieService).findById(any());
        doReturn(reqDispatcher).when(req).getRequestDispatcher(any());
        doReturn(session).when(req).getSession();
        ReceiveUserDto userDTO = buildReceiveUserDto(2L);
        doReturn(userDTO).when(session).getAttribute("user");

        movieDetailsServlet.doGet(req, resp);

        verify(req).getPathInfo();
        verify(req).setAttribute("movieId", 1L);
        verify(movieService).findById(1L);
        verify(req).setAttribute("movie", movieDTO);
        verify(req).getSession();
        verify(session).getAttribute("user");
        verify(req, never()).setAttribute("review_exists", "yes");
        verify(req).getRequestDispatcher(JSPHelper.getPath("movie"));
        verify(reqDispatcher).forward(req, resp);
        verify(resp, never()).setStatus(404);
    }

    @SneakyThrows
    @Test
    void doGetWhenMovieDoesNotExist() {
        doReturn("movie/1").when(req).getPathInfo();
        doReturn(Optional.empty()).when(movieService).findById(any());
        doReturn(reqDispatcher).when(req).getRequestDispatcher(any());

        movieDetailsServlet.doGet(req, resp);

        verify(req).getPathInfo();
        verify(req).setAttribute("movieId", 1L);
        verify(movieService).findById(1L);
        verify(resp).setStatus(404);
        verify(req).getRequestDispatcher(JSPHelper.getPath("movie"));
        verify(reqDispatcher).forward(req, resp);
        verify(req, never()).setAttribute(eq("movie"), any());
        verify(req, never()).getSession();
        verify(session, never()).getAttribute("user");
        verify(req, never()).setAttribute(eq("review_exists"), any());
    }

    @SneakyThrows
    @Test
    void doPost() {
        doReturn("movie/1").when(req).getPathInfo();
        ReceiveMovieDto movieDTO = buildReceiveMovieDto(1L);
        doReturn(Optional.of(movieDTO)).when(movieService).findById(any());
        doReturn(reqDispatcher).when(req).getRequestDispatcher(any());
        doReturn(session).when(req).getSession();
        ReceiveUserDto userDTO = buildReceiveUserDto(1L);
        doReturn(userDTO).when(session).getAttribute("user");

        movieDetailsServlet.doPost(req, resp);

        verify(req).setAttribute("show_add_review", "yes");
        verify(movieDetailsServlet).doGet(req, resp);
    }

    private ReceiveMovieDto buildReceiveMovieDto(Long userId) {
        return ReceiveMovieDto.builder()
                .id(1L)
                .title("title")
                .country("US")
                .releaseYear(2000)
                .genre("Action")
                .description("description")
                .reviews(List.of(buildReceiveReviewDto(userId)))
                .build();
    }

    private ReceiveUserDto buildReceiveUserDto(Long id) {
        return ReceiveUserDto.builder()
                .id(id)
                .name("name")
                .build();
    }

    private ReceiveReviewDto buildReceiveReviewDto(Long userId) {
        return ReceiveReviewDto.builder()
                .id(1L)
                .user(buildReceiveUserDto(userId))
                .build();
    }
}