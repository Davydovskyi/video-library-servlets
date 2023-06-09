package edu.jcourse.servlet;

import edu.jcourse.dto.CreateUserDto;
import edu.jcourse.dto.ReceiveUserDto;
import edu.jcourse.entity.Gender;
import edu.jcourse.entity.Role;
import edu.jcourse.exception.ServiceException;
import edu.jcourse.exception.ValidationException;
import edu.jcourse.service.ServiceProvider;
import edu.jcourse.service.UserService;
import edu.jcourse.util.CodeUtil;
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

    private final transient UserService userService = ServiceProvider.getInstance().getUserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("genders", Gender.values());
        req.setAttribute("roles", Role.values());
        req.getRequestDispatcher(JSPHelper.getPath("registration"))
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CreateUserDto createUserDTO = CreateUserDto.builder()
                .name(req.getParameter("user_name"))
                .birthDate(req.getParameter("birthday"))
                .partImage(req.getPart("image"))
                .email(req.getParameter("email"))
                .password(req.getParameter("password"))
                .gender(req.getParameter("gender"))
                .role(req.getParameter("role"))
                .build();

        try {
            userService.create(createUserDTO);
            ReceiveUserDto user = (ReceiveUserDto) req.getSession().getAttribute("user");

            if (user != null && user.role() == Role.SUPER_ADMIN) {
                req.setAttribute("success", CodeUtil.SUCCESS_ADD_CODE);
                doGet(req, resp);
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
}
