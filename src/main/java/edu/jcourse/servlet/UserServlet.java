package edu.jcourse.servlet;

import edu.jcourse.dto.ReceiveReviewDTO;
import edu.jcourse.dto.ReceiveUserDTO;
import edu.jcourse.exception.ServiceException;
import edu.jcourse.service.ReviewService;
import edu.jcourse.service.ServiceProvider;
import edu.jcourse.service.UserService;
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

@WebServlet(urlPatterns = UrlPath.USER + "/*")
public class UserServlet extends HttpServlet {

    private final UserService userService = ServiceProvider.getInstance().getUserService();
    private final ReviewService reviewService = ServiceProvider.getInstance().getReviewService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long userId = Long.parseLong(req.getPathInfo().split("/")[1]);

        try {
            Optional<ReceiveUserDTO> userDTO = userService.findById(userId);
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
    private void userExists(ReceiveUserDTO userDTO, HttpServletRequest request) {
        request.setAttribute("user", userDTO);
        List<ReceiveReviewDTO> reviews = reviewService.findAllByUserId(userDTO.id());
        request.setAttribute("reviews", reviews);
    }

    @Override
    public void destroy() {
        if (ConnectionBuilder.isPoolOpened()) {
            ConnectionBuilder.closePool();
        }
        super.destroy();
    }
}