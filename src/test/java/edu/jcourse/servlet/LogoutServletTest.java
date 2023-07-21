package edu.jcourse.servlet;

import edu.jcourse.util.UrlPath;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LogoutServletTest {

    @Mock
    private HttpServletRequest req;
    @Mock
    private HttpServletResponse resp;
    @Mock
    private HttpSession session;
    @Spy
    private LogoutServlet logoutServlet;

    @SneakyThrows
    @Test
    void doPost() {
        doReturn(session).when(req).getSession();

        logoutServlet.doPost(req, resp);

        verify(req).getSession();
        verify(session).invalidate();
        verify(resp).sendRedirect(UrlPath.LOGIN);
    }
}