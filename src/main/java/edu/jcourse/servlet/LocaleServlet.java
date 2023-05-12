package edu.jcourse.servlet;

import edu.jcourse.util.UrlPath;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = UrlPath.LOCALE)
public class LocaleServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String locale = req.getParameter("locale");
        req.getSession().setAttribute("locale", locale);
        String previousPage = req.getHeader("referer");
        String page = previousPage != null ? previousPage : UrlPath.LOGIN;
        resp.sendRedirect(page + "?locale=" + locale);
    }
}
