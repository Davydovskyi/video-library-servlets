package edu.jcourse.servlet;

import edu.jcourse.dto.ReceiveUserDto;
import edu.jcourse.entity.Gender;
import edu.jcourse.entity.Role;
import edu.jcourse.exception.ValidationException;
import edu.jcourse.service.UserService;
import edu.jcourse.util.CodeUtil;
import edu.jcourse.util.JSPHelper;
import edu.jcourse.util.UrlPath;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationServletTest {
    @Mock
    private UserService userService;
    @Mock
    private HttpServletRequest req;
    @Mock
    private HttpServletResponse resp;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private Part image;
    @Mock
    private HttpSession session;
    @Spy
    @InjectMocks
    private RegistrationServlet registrationServlet;

    @SneakyThrows
    @Test
    void doGet() {
        doReturn(requestDispatcher).when(req).getRequestDispatcher(any());

        registrationServlet.doGet(req, resp);

        verify(req).setAttribute("genders", Gender.values());
        verify(req).setAttribute("roles", Role.values());
        verify(req).getRequestDispatcher(JSPHelper.getPath("registration"));
        verify(requestDispatcher).forward(req, resp);
    }

    @SneakyThrows
    @Test
    void doPostWhenUserRoleIsNotNullAndRoleIsSuperAdmin() {
        doReturn(requestDispatcher).when(req).getRequestDispatcher(any());
        doReturn("Name").when(req).getParameter("user_name");
        doReturn("2020-01-01").when(req).getParameter("birthday");
        doReturn(image).when(req).getPart("image");
        doReturn("email@example.com").when(req).getParameter("email");
        doReturn("password").when(req).getParameter("password");
        doReturn("Male").when(req).getParameter("gender");
        doReturn("SuperAdmin").when(req).getParameter("role");
        doReturn(session).when(req).getSession();
        ReceiveUserDto receiveUserDto = buildReceiveUserDto();
        doReturn(receiveUserDto).when(session).getAttribute("user");
        doReturn(1L).when(userService).create(any());

        registrationServlet.doPost(req, resp);

        verify(req).getParameter("user_name");
        verify(req).getParameter("birthday");
        verify(req).getPart("image");
        verify(req).getParameter("email");
        verify(req).getParameter("password");
        verify(req).getParameter("gender");
        verify(req).getParameter("role");
        verify(userService).create(any());
        verify(req).getSession();
        verify(session).getAttribute("user");
        verify(req).setAttribute("success", CodeUtil.SUCCESS_ADD_CODE);
        verify(registrationServlet).doGet(req, resp);
        verifyNoInteractions(resp);
        verify(req, never()).setAttribute(eq("errors"), any());
    }

    @SneakyThrows
    @Test
    void doPostWhenUserIsNull() {
        doReturn("Name").when(req).getParameter("user_name");
        doReturn("2020-01-01").when(req).getParameter("birthday");
        doReturn(image).when(req).getPart("image");
        doReturn("email@example.com").when(req).getParameter("email");
        doReturn("password").when(req).getParameter("password");
        doReturn("Male").when(req).getParameter("gender");
        doReturn("SuperAdmin").when(req).getParameter("role");
        doReturn(session).when(req).getSession();
        doReturn(null).when(session).getAttribute("user");
        doReturn(1L).when(userService).create(any());

        registrationServlet.doPost(req, resp);

        verify(req).getParameter("user_name");
        verify(req).getParameter("birthday");
        verify(req).getPart("image");
        verify(req).getParameter("email");
        verify(req).getParameter("password");
        verify(req).getParameter("gender");
        verify(req).getParameter("role");
        verify(userService).create(any());
        verify(req).getSession();
        verify(session).getAttribute("user");
        verify(req, never()).setAttribute("success", CodeUtil.SUCCESS_ADD_CODE);
        verify(registrationServlet, never()).doGet(req, resp);
        verify(resp).sendRedirect(UrlPath.LOGIN);
        verify(req, never()).setAttribute(eq("errors"), any());
    }

    @SneakyThrows
    @Test
    void doPostWhenUserServiceThrowsValidationException() {
        doReturn(requestDispatcher).when(req).getRequestDispatcher(any());
        doReturn("Name").when(req).getParameter("user_name");
        doReturn("2020-01-01").when(req).getParameter("birthday");
        doReturn(image).when(req).getPart("image");
        doReturn("email@example.com").when(req).getParameter("email");
        doReturn("password").when(req).getParameter("password");
        doReturn("Male").when(req).getParameter("gender");
        doReturn("SuperAdmin").when(req).getParameter("role");
        doThrow(ValidationException.class).when(userService).create(any());

        registrationServlet.doPost(req, resp);

        verify(req).getParameter("user_name");
        verify(req).getParameter("birthday");
        verify(req).getPart("image");
        verify(req).getParameter("email");
        verify(req).getParameter("password");
        verify(req).getParameter("gender");
        verify(req).getParameter("role");
        verify(userService).create(any());
        verifyNoInteractions(session, resp);
        verify(req, never()).setAttribute("success", CodeUtil.SUCCESS_ADD_CODE);
        verify(registrationServlet).doGet(req, resp);
        verify(req).setAttribute(eq("errors"), any());
    }

    private ReceiveUserDto buildReceiveUserDto() {
        return ReceiveUserDto.builder()
                .role(Role.SUPER_ADMIN)
                .build();
    }
}