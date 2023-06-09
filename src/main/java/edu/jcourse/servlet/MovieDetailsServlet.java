package edu.jcourse.servlet;

import edu.jcourse.dto.ReceiveMovieDto;
import edu.jcourse.dto.ReceiveReviewDto;
import edu.jcourse.dto.ReceiveUserDto;
import edu.jcourse.exception.ServiceException;
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
import java.util.Optional;

@WebServlet(urlPatterns = UrlPath.MOVIE + "/*")
public class MovieDetailsServlet extends HttpServlet {

    private final transient MovieService movieService = ServiceProvider.getInstance().getMovieService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long movieId = Long.parseLong(req.getPathInfo().split("/")[1]);
        req.setAttribute("movieId", movieId);
        try {
            Optional<ReceiveMovieDto> movieDTO = movieService.findById(movieId);
            movieDTO.ifPresentOrElse(receiveMovieDTO ->
                            setAttributes(req, receiveMovieDTO),
                    () -> resp.setStatus(404));
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
        req.getRequestDispatcher(JSPHelper.getPath("movie")).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("show_add_review", "yes");
        doGet(req, resp);
    }

    private boolean checkIfUserReviewExists(Long userId, List<ReceiveReviewDto> reviews) {
        return reviews.stream()
                .anyMatch(review -> review.user().id().equals(userId));
    }

    private void setAttributes(HttpServletRequest req, ReceiveMovieDto movieDTO) {
        req.setAttribute("movie", movieDTO);
        boolean reviewExists = checkIfUserReviewExists(((ReceiveUserDto) req.getSession().getAttribute("user")).id(), movieDTO.reviews());
        if (reviewExists) {
            req.setAttribute("review_exists", "yes");
        }
    }
}