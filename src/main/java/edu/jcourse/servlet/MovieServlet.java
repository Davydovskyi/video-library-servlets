package edu.jcourse.servlet;

import edu.jcourse.dto.MovieFilterDTO;
import edu.jcourse.dto.ReceiveMovieDTO;
import edu.jcourse.entity.Genre;
import edu.jcourse.exception.ServiceException;
import edu.jcourse.exception.ValidationException;
import edu.jcourse.service.MovieService;
import edu.jcourse.service.ServiceProvider;
import edu.jcourse.util.ConnectionBuilder;
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

    private final transient MovieService movieService = ServiceProvider.getInstance().getMovieService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("genres", Genre.values());
        req.getRequestDispatcher(JSPHelper.getPath("movies"))
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        MovieFilterDTO movieFilterDTO = MovieFilterDTO.builder()
                .title(req.getParameter("title"))
                .releaseYear(req.getParameter("release_year"))
                .country(req.getParameter("country"))
                .genre(req.getParameter("genre"))
                .limit(Integer.parseInt(req.getParameter("limit")))
                .offset(Integer.parseInt(req.getParameter("offset")))
                .build();

        try {
            List<ReceiveMovieDTO> movieDTOS = movieService.findMovies(movieFilterDTO);
            req.setAttribute("movies", movieDTOS);
            req.getSession().setAttribute("movies", movieDTOS);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        } catch (ValidationException e) {
            req.setAttribute("errors", e.getErrors());
        }
        doGet(req, resp);
    }
}