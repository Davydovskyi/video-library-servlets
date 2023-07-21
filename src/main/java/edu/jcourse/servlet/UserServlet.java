package edu.jcourse.servlet;

import edu.jcourse.dto.ReceiveReviewDto;
import edu.jcourse.dto.ReceiveUserDto;
import edu.jcourse.exception.ServiceException;
import edu.jcourse.service.ReviewService;
import edu.jcourse.service.ServiceProvider;
import edu.jcourse.service.UserService;
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

@WebServlet(urlPatterns = UrlPath.USER + "/*")
public class UserServlet extends HttpServlet {

    private final transient UserService userService;
    private final transient ReviewService reviewService;

    public UserServlet() {
        this(ServiceProvider.getInstance().getUserService(),
                ServiceProvider.getInstance().getReviewService());
    }

    public UserServlet(UserService userService, ReviewService reviewService) {
        this.userService = userService;
        this.reviewService = reviewService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long userId = Long.parseLong(req.getPathInfo().split("/")[1]);

        try {
            Optional<ReceiveUserDto> userDTO = userService.findById(userId);
            userDTO.ifPresentOrElse(user ->
                            userExists(user, req),
                    () -> resp.setStatus(404));
            req.getRequestDispatcher(JSPHelper.getPath("user")).
                    forward(req, resp);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    private void userExists(ReceiveUserDto userDTO, HttpServletRequest request) {
        request.setAttribute("user", userDTO);
        List<ReceiveReviewDto> reviews = reviewService.findAllByUserId(userDTO.id());
        request.setAttribute("reviews", reviews);
    }
}