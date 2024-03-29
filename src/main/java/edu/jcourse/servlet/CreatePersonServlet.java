package edu.jcourse.servlet;

import edu.jcourse.dto.CreatePersonDto;
import edu.jcourse.dto.ReceivePersonDto;
import edu.jcourse.entity.Person;
import edu.jcourse.exception.ServiceException;
import edu.jcourse.exception.ValidationException;
import edu.jcourse.mapper.MapperProvider;
import edu.jcourse.mapper.impl.PersonMapper;
import edu.jcourse.service.PersonService;
import edu.jcourse.service.ServiceProvider;
import edu.jcourse.util.CodeUtil;
import edu.jcourse.util.UrlPath;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Comparator;
import java.util.concurrent.CopyOnWriteArrayList;

@WebServlet(urlPatterns = UrlPath.ADD_PERSON)
public class CreatePersonServlet extends HttpServlet {

    private final transient PersonService personService;
    private final transient PersonMapper personMapper;

    public CreatePersonServlet() {
        this(ServiceProvider.getInstance().getPersonService(),
                MapperProvider.getInstance().getPersonMapper());
    }

    public CreatePersonServlet(PersonService personService, PersonMapper personMapper) {
        super();
        this.personService = personService;
        this.personMapper = personMapper;
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CreatePersonDto createPersonDTO = CreatePersonDto.builder()
                .name(req.getParameter("name"))
                .birthDate(req.getParameter("birthday"))
                .build();

        try {
            Person person = personService.create(createPersonDTO);
            req.setAttribute("success", CodeUtil.SUCCESS_ADD_CODE);
            CopyOnWriteArrayList<ReceivePersonDto> members = (CopyOnWriteArrayList<ReceivePersonDto>) req.getServletContext().getAttribute("filmMembers");
            members.add(personMapper.mapFrom(person));
            members.sort(Comparator.comparing(ReceivePersonDto::personData));
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        } catch (ValidationException e) {
            req.setAttribute("errors", e.getErrors());
        }
        req.setAttribute("show_add_person", "yes");
        req.getRequestDispatcher(UrlPath.ADMIN).forward(req, resp);
    }
}