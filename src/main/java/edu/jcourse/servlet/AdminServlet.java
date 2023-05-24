package edu.jcourse.servlet;

import edu.jcourse.dto.ReceivePersonDTO;
import edu.jcourse.entity.Genre;
import edu.jcourse.entity.PersonRole;
import edu.jcourse.exception.ServiceException;
import edu.jcourse.service.PersonService;
import edu.jcourse.service.ServiceProvider;
import edu.jcourse.util.ConnectionBuilder;
import edu.jcourse.util.JSPHelper;
import edu.jcourse.util.UrlPath;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.concurrent.CopyOnWriteArrayList;

@WebServlet(urlPatterns = UrlPath.ADMIN)
public class AdminServlet extends HttpServlet {

    private final transient PersonService personService = ServiceProvider.getInstance().getPersonService();

    @Override
    public void init(ServletConfig config) throws ServletException {
        try {
            CopyOnWriteArrayList<ReceivePersonDTO> members = new CopyOnWriteArrayList<>(personService.findAll());
            members.sort(Comparator.comparing(ReceivePersonDTO::personData));
            config.getServletContext().setAttribute("filmMembers", members);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("genres", Genre.values());
        req.setAttribute("personRoles", PersonRole.values());
        req.getRequestDispatcher(JSPHelper.getPath("admin"))
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Enumeration<String> parameterNames = req.getParameterNames();
        req.setAttribute(parameterNames.nextElement(), "yes");
        doGet(req, resp);
    }

    @Override
    public void destroy() {
        if (ConnectionBuilder.isPoolOpened()) {
            ConnectionBuilder.closePool();
        }
        super.destroy();
    }
}