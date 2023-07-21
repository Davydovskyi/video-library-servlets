package edu.jcourse.servlet;

import edu.jcourse.dto.ReceiveMovieDto;
import edu.jcourse.dto.ReceivePersonDto;
import edu.jcourse.exception.ServiceException;
import edu.jcourse.service.MovieService;
import edu.jcourse.service.PersonService;
import edu.jcourse.service.ServiceProvider;
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

    private final transient PersonService personService;
    private final transient MovieService movieService;

    public PersonServlet() {
        this(ServiceProvider.getInstance().getPersonService(),
                ServiceProvider.getInstance().getMovieService());
    }

    public PersonServlet(PersonService personService, MovieService movieService) {
        this.personService = personService;
        this.movieService = movieService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long personId = Long.parseLong(req.getPathInfo().split("/")[1]);
        try {
            Optional<ReceivePersonDto> personDTO = personService.findById(personId);
            personDTO.ifPresentOrElse(person ->
                            findMovies(person, req),
                    () -> resp.setStatus(404));
            req.getRequestDispatcher(JSPHelper.getPath("person")).
                    forward(req, resp);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    private void findMovies(ReceivePersonDto personDTO, HttpServletRequest req) {
        req.setAttribute("person", personDTO);
        List<ReceiveMovieDto> movies = movieService.findByPersonId(personDTO.id());
        req.setAttribute("movies", movies);
        req.getSession().setAttribute("person-movies", movies);
    }
}