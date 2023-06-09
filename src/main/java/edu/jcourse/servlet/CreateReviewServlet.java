package edu.jcourse.servlet;

import edu.jcourse.dto.CreateReviewDto;
import edu.jcourse.dto.ReceiveUserDto;
import edu.jcourse.exception.ServiceException;
import edu.jcourse.exception.ValidationException;
import edu.jcourse.service.ReviewService;
import edu.jcourse.service.ServiceProvider;
import edu.jcourse.util.UrlPath;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = UrlPath.ADD_REVIEW + "/*")
public class CreateReviewServlet extends HttpServlet {

    private final transient ReviewService reviewService = ServiceProvider.getInstance().getReviewService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String movieId = req.getPathInfo().split("/")[1];
        CreateReviewDto createReviewDTO = CreateReviewDto.builder()
                .moveId(movieId)
                .userId(((ReceiveUserDto) req.getSession().getAttribute("user")).id())
                .reviewText(req.getParameter("review"))
                .rate(req.getParameter("rate"))
                .build();
        try {
            reviewService.create(createReviewDTO);
            resp.sendRedirect(UrlPath.MOVIE + "/" + movieId);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        } catch (ValidationException e) {
            req.setAttribute("errors", e.getErrors());
            req.getRequestDispatcher(UrlPath.MOVIE + "/" + movieId).forward(req, resp);
        }
    }
}