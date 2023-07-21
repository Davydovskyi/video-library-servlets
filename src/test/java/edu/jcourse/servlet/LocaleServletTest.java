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
class LocaleServletTest {
    @Mock
    private HttpServletRequest req;
    @Mock
    private HttpServletResponse resp;
    @Mock
    private HttpSession session;
    @Spy
    private LocaleServlet localeServlet;

    @SneakyThrows
    @Test
    void doPost() {
        doReturn("en").when(req).getParameter("locale");
        doReturn(session).when(req).getSession();
        doReturn(UrlPath.ADMIN).when(req).getHeader("referer");

        localeServlet.doPost(req, resp);

        verify(req).getParameter("locale");
        verify(req).getSession();
        verify(session).setAttribute("locale", "en");
        verify(req).getHeader("referer");
        verify(resp).sendRedirect(UrlPath.ADMIN + "?locale=en");
    }

    @SneakyThrows
    @Test
    void doPostWhitNoReferer() {
        doReturn("en").when(req).getParameter("locale");
        doReturn(session).when(req).getSession();
        doReturn(null).when(req).getHeader("referer");

        localeServlet.doPost(req, resp);

        verify(req).getParameter("locale");
        verify(req).getSession();
        verify(session).setAttribute("locale", "en");
        verify(req).getHeader("referer");
        verify(resp).sendRedirect(UrlPath.LOGIN + "?locale=en");
    }
}