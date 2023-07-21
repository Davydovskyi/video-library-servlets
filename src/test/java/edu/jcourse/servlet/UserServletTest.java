package edu.jcourse.servlet;

import edu.jcourse.dto.ReceiveUserDto;
import edu.jcourse.service.ReviewService;
import edu.jcourse.service.UserService;
import edu.jcourse.util.JSPHelper;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
class UserServletTest {
    @Mock
    private UserService userService;
    @Mock
    private ReviewService reviewService;
    @Mock
    private HttpServletRequest req;
    @Mock
    private HttpServletResponse resp;
    @Mock
    private RequestDispatcher reqDispatcher;
    @InjectMocks
    private UserServlet userServlet;

    @SneakyThrows
    @Test
    void doGet() {
        doReturn("user/1").when(req).getPathInfo();
        ReceiveUserDto userDTO = buildUserDto();
        doReturn(Optional.of(userDTO)).when(userService).findById(any());
        doReturn(reqDispatcher).when(req).getRequestDispatcher(any());
        doReturn(List.of()).when(reviewService).findAllByUserId(any());

        userServlet.doGet(req, resp);

        verify(req).getPathInfo();
        verify(userService).findById(1L);
        verify(reviewService).findAllByUserId(1L);
        verify(req).getRequestDispatcher(JSPHelper.getPath("user"));
        verify(reqDispatcher).forward(req, resp);
        verify(req).setAttribute("user", userDTO);
        verify(req).setAttribute("reviews", List.of());
        verifyNoInteractions(resp);
    }

    @SneakyThrows
    @Test
    void doGetWhenUserNotFound() {
        doReturn("user/1").when(req).getPathInfo();
        doReturn(Optional.empty()).when(userService).findById(any());
        doReturn(reqDispatcher).when(req).getRequestDispatcher(any());

        userServlet.doGet(req, resp);

        verify(req).getPathInfo();
        verify(userService).findById(1L);
        verify(resp).setStatus(404);
        verify(req).getRequestDispatcher(JSPHelper.getPath("user"));
        verify(reqDispatcher).forward(req, resp);
        verify(req, never()).setAttribute(eq("user"), any());
        verify(req, never()).setAttribute(eq("reviews"), any());
        verifyNoInteractions(reviewService);
    }

    private ReceiveUserDto buildUserDto() {
        return ReceiveUserDto.builder()
                .id(1L)
                .build();
    }
}