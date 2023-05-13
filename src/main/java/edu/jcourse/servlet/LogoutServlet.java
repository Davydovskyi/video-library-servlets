package edu.jcourse.servlet;

import edu.jcourse.util.UrlPath;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

@WebServlet(urlPatterns = UrlPath.LOGOUT)
public class LogoutServlet extends HttpServlet {
}
