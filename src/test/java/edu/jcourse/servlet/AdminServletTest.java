package edu.jcourse.servlet;

import edu.jcourse.dto.ReceivePersonDto;
import edu.jcourse.entity.Genre;
import edu.jcourse.entity.PersonRole;
import edu.jcourse.service.PersonService;
import edu.jcourse.util.JSPHelper;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Enumeration;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AdminServletTest {

    @Mock
    private PersonService personService;
    @Mock
    private ServletConfig config;
    @Mock
    private HttpServletRequest req;
    @Mock
    private HttpServletResponse resp;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ServletContext servletContext;
    @Mock
    private Enumeration<String> enumeration;
    @InjectMocks
    private AdminServlet adminServlet;

    @SneakyThrows
    @Test
    void init() {
        ArrayList<ReceivePersonDto> members = new ArrayList<>();
        doReturn(members).when(personService).findAll();
        doReturn(servletContext).when(config).getServletContext();

        adminServlet.init(config);

        verify(servletContext).setAttribute("filmMembers", members);
        verify(personService).findAll();
        verify(config).getServletContext();
    }

    @SneakyThrows
    @Test
    void doGet() {
        doReturn(requestDispatcher).when(req).getRequestDispatcher(JSPHelper.getPath("admin"));

        adminServlet.doGet(req, resp);

        verify(req).setAttribute("genres", Genre.values());
        verify(req).setAttribute("personRoles", PersonRole.values());
        verify(requestDispatcher).forward(req, resp);
    }

    @SneakyThrows
    @Test
    void doPost() {
        doReturn(enumeration).when(req).getParameterNames();
        doReturn("dummy").when(enumeration).nextElement();
        doReturn(requestDispatcher).when(req).getRequestDispatcher(JSPHelper.getPath("admin"));

        adminServlet.doPost(req, resp);

        verify(req).setAttribute("dummy", "yes");
        verify(req).getParameterNames();
        verify(enumeration).nextElement();
    }
}