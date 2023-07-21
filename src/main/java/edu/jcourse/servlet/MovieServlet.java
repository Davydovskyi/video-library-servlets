package edu.jcourse.servlet;

import edu.jcourse.dto.MovieFilterDto;
import edu.jcourse.dto.ReceiveMovieDto;
import edu.jcourse.entity.Genre;
import edu.jcourse.exception.ServiceException;
import edu.jcourse.exception.ValidationException;
import edu.jcourse.service.MovieService;
import edu.jcourse.service.ServiceProvider;
import edu.jcourse.util.JSPHelper;
import edu.jcourse.util.UrlPath;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = UrlPath.MOVIES)
public class MovieServlet extends HttpServlet {

    private static final String MOVIES_ATTRIBUTE_NAME = "movies";

    private final transient MovieService movieService;

    public MovieServlet() {
        this(ServiceProvider.getInstance().getMovieService());
    }

    public MovieServlet(MovieService movieService) {
        this.movieService = movieService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("genres", Genre.values());
        req.getRequestDispatcher(JSPHelper.getPath(MOVIES_ATTRIBUTE_NAME))
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        MovieFilterDto movieFilterDTO = MovieFilterDto.builder()
                .title(req.getParameter("title"))
                .releaseYear(req.getParameter("release_year"))
                .country(req.getParameter("country"))
                .genre(req.getParameter("genre"))
                .limit(Integer.parseInt(req.getParameter("limit")))
                .offset(Integer.parseInt(req.getParameter("offset")))
                .build();

        try {
            List<ReceiveMovieDto> movieDTOS = movieService.findMovies(movieFilterDTO);
            req.setAttribute(MOVIES_ATTRIBUTE_NAME, movieDTOS);
            req.getSession().setAttribute(MOVIES_ATTRIBUTE_NAME, movieDTOS);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        } catch (ValidationException e) {
            req.setAttribute("errors", e.getErrors());
        }
        doGet(req, resp);
    }
}