package edu.jcourse.servlet;

import edu.jcourse.dto.CreateReviewDto;
import edu.jcourse.dto.ReceiveUserDto;
import edu.jcourse.exception.ValidationException;
import edu.jcourse.service.ReviewService;
import edu.jcourse.util.UrlPath;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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
class CreateReviewServletTest {

    @Mock
    private ReviewService reviewService;
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
    private CreateReviewServlet createReviewServlet;

    @BeforeEach
    void setUp() {
        createRequestStub();
    }

    @SneakyThrows
    @Test
    void doGet() {
        CreateReviewDto createReviewDto = buildCreateReviewDto();
        doReturn(1L).when(reviewService).create(any());

        createReviewServlet.doGet(req, resp);

        verify(createReviewServlet).doPost(req, resp);
    }

    @SneakyThrows
    @Test
    void doPost() {
        CreateReviewDto createReviewDto = buildCreateReviewDto();
        doReturn(1L).when(reviewService).create(any());

        createReviewServlet.doPost(req, resp);

        verifyRequest();
        verify(reviewService).create(createReviewDto);
        verify(resp).sendRedirect(UrlPath.MOVIE + "/" + "1");
        verify(req, never()).setAttribute(eq("errors"), any());
        verifyNoInteractions(requestDispatcher);
    }

    @SneakyThrows
    @Test
    void doPostWhenReviewServiceThrowsValidationException() {
        doThrow(ValidationException.class).when(reviewService).create(any());
        doReturn(requestDispatcher).when(req).getRequestDispatcher(UrlPath.MOVIE + "/" + "1");

        createReviewServlet.doPost(req, resp);

        verifyRequest();
        verify(reviewService).create(any());
        verify(resp, never()).sendRedirect(UrlPath.MOVIE + "/" + "1");
        verify(req).setAttribute(eq("errors"), any());
        verify(req).getRequestDispatcher(UrlPath.MOVIE + "/" + "1");
        verify(requestDispatcher).forward(req, resp);
    }

    private void createRequestStub() {
        doReturn("movie/1").when(req).getPathInfo();
        doReturn(session).when(req).getSession();
        doReturn(buildReceiveUserDto()).when(session).getAttribute("user");
        doReturn("review").when(req).getParameter("review");
        doReturn("10").when(req).getParameter("rate");
    }

    private void verifyRequest() {
        verify(req).getPathInfo();
        verify(req).getSession();
        verify(session).getAttribute("user");
        verify(req).getParameter("review");
        verify(req).getParameter("rate");
    }

    private CreateReviewDto buildCreateReviewDto() {
        return CreateReviewDto.builder()
                .moveId("1")
                .userId(1L)
                .reviewText("review")
                .rate("10")
                .build();
    }

    private ReceiveUserDto buildReceiveUserDto() {
        return ReceiveUserDto.builder()
                .id(1L)
                .build();
    }


}