package edu.jcourse.servlet;

import edu.jcourse.dto.CreatePersonDTO;
import edu.jcourse.exception.ServiceException;
import edu.jcourse.exception.ValidationException;
import edu.jcourse.service.PersonService;
import edu.jcourse.service.ServiceProvider;
import edu.jcourse.util.CodeUtil;
import edu.jcourse.util.ConnectionBuilder;
import edu.jcourse.util.UrlPath;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = UrlPath.PERSON)
public class PersonServlet extends HttpServlet {

    private final transient PersonService personService = ServiceProvider.getInstance().getPersonService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CreatePersonDTO createPersonDTO = CreatePersonDTO.builder()
                .name(req.getParameter("name"))
                .birthDate(req.getParameter("birthday"))
                .build();

        try {
            personService.create(createPersonDTO);
            req.setAttribute("success", CodeUtil.SUCCESS_ADD_CODE);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        } catch (ValidationException e) {
            req.setAttribute("errors", e.getErrors());
        }
        req.setAttribute("addPerson", "yes");
        req.getRequestDispatcher(UrlPath.ADMIN).forward(req, resp);
    }

    @Override
    public void destroy() {
        if (ConnectionBuilder.isPoolOpened()) {
            ConnectionBuilder.closePool();
        }
        super.destroy();
    }
}
