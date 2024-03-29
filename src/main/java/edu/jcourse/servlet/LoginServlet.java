package edu.jcourse.servlet;

import edu.jcourse.dto.LoginUserDto;
import edu.jcourse.dto.ReceiveUserDto;
import edu.jcourse.entity.Role;
import edu.jcourse.exception.ServiceException;
import edu.jcourse.exception.ValidationException;
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

@WebServlet(urlPatterns = UrlPath.LOGIN)
public class LoginServlet extends HttpServlet {

    private final transient UserService userService;

    public LoginServlet() {
        this(ServiceProvider.getInstance().getUserService());
    }

    public LoginServlet(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(JSPHelper.getPath("login"))
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LoginUserDto loginUserDTO = LoginUserDto.builder()
                .email(req.getParameter("email"))
                .password(req.getParameter("password"))
                .build();
        try {
            userService.login(loginUserDTO)
                    .ifPresentOrElse(
                            userDTO -> onLoginSuccess(userDTO, req, resp),
                            () -> onLoginFail(req, resp));
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        } catch (ValidationException e) {
            req.setAttribute("errors", e.getErrors());
            doGet(req, resp);
        }
    }

    @SneakyThrows
    private void onLoginFail(HttpServletRequest req, HttpServletResponse resp) {
        resp.sendRedirect(UrlPath.LOGIN + "?error&email=" + req.getParameter("email"));
    }

    @SneakyThrows
    private void onLoginSuccess(ReceiveUserDto receiveUserDTO, HttpServletRequest req, HttpServletResponse resp) {
        req.getSession().setAttribute("user", receiveUserDTO);
        if (receiveUserDTO.role() == Role.USER) {
            resp.sendRedirect(UrlPath.MOVIES);
        } else {
            resp.sendRedirect(UrlPath.ADMIN);
        }
    }
}