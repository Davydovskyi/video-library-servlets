package edu.jcourse.servlet;

import edu.jcourse.dto.CreateMovieDto;
import edu.jcourse.dto.CreateMoviePersonDto;
import edu.jcourse.exception.ServiceException;
import edu.jcourse.exception.ValidationException;
import edu.jcourse.service.MovieService;
import edu.jcourse.service.ServiceProvider;
import edu.jcourse.util.CodeUtil;
import edu.jcourse.util.UrlPath;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@WebServlet(urlPatterns = UrlPath.ADD_MOVIE)
public class CreateMovieServlet extends HttpServlet {

    private final transient MovieService movieService = ServiceProvider.getInstance().getMovieService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Set<CreateMoviePersonDto> moviePersons = new HashSet<>();
        for (int i = 1; i <= 4; i++) {
            String personId = req.getParameter("member" + i);
            if (personId != null) {
                moviePersons.add(
                        CreateMoviePersonDto.builder()
                                .personId(personId)
                                .personRole(req.getParameter("movie_role" + i))
                                .build()
                );
            }
        }

        CreateMovieDto createMovieDTO = CreateMovieDto.builder()
                .title(req.getParameter("title"))
                .releaseYear(req.getParameter("release_year"))
                .country(req.getParameter("country"))
                .genre(req.getParameter("genre"))
                .description(req.getParameter("description"))
                .moviePersons(moviePersons)
                .build();

        try {
            movieService.create(createMovieDTO);
            req.setAttribute("success", CodeUtil.SUCCESS_ADD_CODE);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        } catch (ValidationException e) {
            req.setAttribute("errors", e.getErrors());
        }
        req.setAttribute("show_add_movie", "yes");
        req.getRequestDispatcher(UrlPath.ADMIN).forward(req, resp);
    }
}