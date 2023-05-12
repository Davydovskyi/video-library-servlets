package edu.jcourse.servlet;

import edu.jcourse.dto.CreateUserDTO;
import edu.jcourse.entity.Gender;
import edu.jcourse.entity.Role;
import edu.jcourse.entity.User;
import edu.jcourse.exception.ServiceException;
import edu.jcourse.exception.ValidationException;
import edu.jcourse.service.ServiceProvider;
import edu.jcourse.service.UserService;
import edu.jcourse.util.ConnectionBuilder;
import edu.jcourse.util.JSPHelper;
import edu.jcourse.util.UrlPath;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@MultipartConfig(fileSizeThreshold = 1024 * 1024)
@WebServlet(urlPatterns = UrlPath.REGISTRATION)
public class RegistrationServlet extends HttpServlet {

    private final UserService userService = ServiceProvider.getInstance().getUserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("genders", Gender.values());
        req.getRequestDispatcher(JSPHelper.getPath("registration"))
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CreateUserDTO createUserDTO = CreateUserDTO.builder()
                .name(req.getParameter("userName"))
                .birthDate(req.getParameter("birthday"))
                .partImage(req.getPart("image"))
                .email(req.getParameter("email"))
                .password(req.getParameter("password"))
                .gender(req.getParameter("gender"))
                .role(req.getParameter("role"))
                .build();

        try {
            userService.create(createUserDTO);
            User user = (User) req.getSession().getAttribute("user");

            if (user != null && user.getRole() == Role.SUPER_ADMIN) {
                String previousPage = req.getHeader("referer");
                String page = previousPage != null ? previousPage : UrlPath.LOGIN;
                resp.sendRedirect(page);
            } else {
                resp.sendRedirect(UrlPath.LOGIN);
            }
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        } catch (ValidationException e) {
            req.setAttribute("errors", e.getErrors());
            doGet(req, resp);
        }
    }

    @Override
    public void destroy() {
        if (ConnectionBuilder.isPoolOpened()) {
            ConnectionBuilder.closePool();
        }
        super.destroy();
    }
}
