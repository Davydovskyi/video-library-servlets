package edu.jcourse.servlet;

import edu.jcourse.dto.ReceiveMovieDTO;
import edu.jcourse.dto.ReceivePersonDTO;
import edu.jcourse.exception.ServiceException;
import edu.jcourse.service.MovieService;
import edu.jcourse.service.PersonService;
import edu.jcourse.service.ServiceProvider;
import edu.jcourse.util.ConnectionBuilder;
import edu.jcourse.util.JSPHelper;
import edu.jcourse.util.UrlPath;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet(urlPatterns = UrlPath.PERSON + "/*")
public class PersonServlet extends HttpServlet {

    private final transient PersonService personService = ServiceProvider.getInstance().getPersonService();
    private final transient MovieService movieService = ServiceProvider.getInstance().getMovieService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long personId = Long.parseLong(req.getPathInfo().split("/")[1]);
        try {
            Optional<ReceivePersonDTO> personDTO = personService.findById(personId);
            personDTO.ifPresentOrElse(person ->
                            personExists(person, req),
                    () -> resp.setStatus(404));
            req.getRequestDispatcher(JSPHelper.getPath("person")).
                    forward(req, resp);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    private void personExists(ReceivePersonDTO personDTO, HttpServletRequest req) {
        req.setAttribute("person", personDTO);
        List<ReceiveMovieDTO> movies = movieService.findByPersonId(personDTO.id());
        req.setAttribute("movies", movies);
    }

    @Override
    public void destroy() {
        if (ConnectionBuilder.isPoolOpened()) {
            ConnectionBuilder.closePool();
        }
        super.destroy();
    }
}