package edu.jcourse.servlet;

import edu.jcourse.dto.LoginUserDto;
import edu.jcourse.dto.ReceiveUserDto;
import edu.jcourse.entity.Gender;
import edu.jcourse.entity.Role;
import edu.jcourse.exception.ValidationException;
import edu.jcourse.service.UserService;
import edu.jcourse.util.JSPHelper;
import edu.jcourse.util.UrlPath;
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

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServletTest {
    @Mock
    private UserService userService;
    @Mock
    private HttpServletRequest req;
    @Mock
    private HttpServletResponse resp;
    @Mock
    private HttpSession session;
    @Mock
    private RequestDispatcher dispatcher;
    @Spy
    @InjectMocks
    private LoginServlet loginServlet;

    @SneakyThrows
    @Test
    void doGet() {
        doReturn(dispatcher).when(req).getRequestDispatcher(any());

        loginServlet.doGet(req, resp);

        verify(req).getRequestDispatcher(JSPHelper.getPath("login"));
        verify(dispatcher).forward(req, resp);
    }

    @SneakyThrows
    @Test
    void doPostWhenLoginSuccessAndRoleIsUser() {
        LoginUserDto loginUserDto = buildLoginUserDto();
        doReturn("email@example.com").when(req).getParameter("email");
        doReturn("password").when(req).getParameter("password");
        Optional<ReceiveUserDto> receiveUserDto = Optional.of(buildReceiveUserDto(Role.USER));
        doReturn(receiveUserDto).when(userService).login(any());
        doReturn(session).when(req).getSession();

        loginServlet.doPost(req, resp);

        verify(req).getParameter("email");
        verify(req).getParameter("password");
        verify(userService).login(loginUserDto);
        verify(req).getSession();
        verify(session).setAttribute("user", receiveUserDto.get());
        verify(resp).sendRedirect(UrlPath.MOVIES);
        verify(req, never()).setAttribute(eq("errors"), any());
        verify(loginServlet, never()).doGet(req, resp);
        verify(resp, never()).sendRedirect(UrlPath.LOGIN + "?error&email=email@example.com");
        verify(resp, never()).sendRedirect(UrlPath.ADMIN);
    }

    @SneakyThrows
    @Test
    void doPostWhenLoginSuccessAndRoleIsAdmin() {
        LoginUserDto loginUserDto = buildLoginUserDto();
        doReturn("email@example.com").when(req).getParameter("email");
        doReturn("password").when(req).getParameter("password");
        Optional<ReceiveUserDto> receiveUserDto = Optional.of(buildReceiveUserDto(Role.ADMIN));
        doReturn(receiveUserDto).when(userService).login(any());
        doReturn(session).when(req).getSession();

        loginServlet.doPost(req, resp);

        verify(req).getParameter("email");
        verify(req).getParameter("password");
        verify(userService).login(loginUserDto);
        verify(req).getSession();
        verify(session).setAttribute("user", receiveUserDto.get());
        verify(resp).sendRedirect(UrlPath.ADMIN);
        verify(req, never()).setAttribute(eq("errors"), any());
        verify(loginServlet, never()).doGet(req, resp);
        verify(resp, never()).sendRedirect(UrlPath.LOGIN + "?error&email=email@example.com");
        verify(resp, never()).sendRedirect(UrlPath.MOVIES);

    }

    @SneakyThrows
    @Test
    void doPostWhenLoginFail() {
        LoginUserDto loginUserDto = buildLoginUserDto();
        doReturn("email@example.com").when(req).getParameter("email");
        doReturn("password").when(req).getParameter("password");
        Optional<ReceiveUserDto> receiveUserDto = Optional.empty();
        doReturn(receiveUserDto).when(userService).login(any());

        loginServlet.doPost(req, resp);

        verify(req, times(2)).getParameter("email");
        verify(req).getParameter("password");
        verify(userService).login(loginUserDto);
        verify(req, never()).getSession();
        verify(resp).sendRedirect(UrlPath.LOGIN + "?error&email=email@example.com");
        verifyNoInteractions(session);
        verify(req, never()).setAttribute(eq("errors"), any());
        verify(loginServlet, never()).doGet(req, resp);
        verify(resp, never()).sendRedirect(UrlPath.MOVIES);
        verify(resp, never()).sendRedirect(UrlPath.ADMIN);
    }

    @SneakyThrows
    @Test
    void doPostWhenUserServiceThrowsValidationException() {
        LoginUserDto loginUserDto = buildLoginUserDto();
        doReturn("email@example.com").when(req).getParameter("email");
        doReturn("password").when(req).getParameter("password");
        doThrow(ValidationException.class).when(userService).login(any());
        doReturn(dispatcher).when(req).getRequestDispatcher(any());

        loginServlet.doPost(req, resp);

        verify(req).getParameter("email");
        verify(req).getParameter("password");
        verify(userService).login(loginUserDto);
        verify(req).setAttribute(eq("errors"), any());
        verify(loginServlet).doGet(req, resp);
        verify(req, never()).getSession();
        verify(resp, never()).sendRedirect(UrlPath.LOGIN + "?error&email=email@example.com");
        verify(resp, never()).sendRedirect(UrlPath.MOVIES);
        verify(resp, never()).sendRedirect(UrlPath.ADMIN);
    }

    private LoginUserDto buildLoginUserDto() {
        return LoginUserDto.builder()
                .email("email@example.com")
                .password("password")
                .build();
    }

    private ReceiveUserDto buildReceiveUserDto(Role role) {
        return ReceiveUserDto.builder()
                .id(1L)
                .name("name")
                .role(role)
                .email("email@example.com")
                .gender(Gender.MALE)
                .birthday(LocalDate.of(2000, 1, 1))
                .image("images/image.jpg")
                .build();
    }
}