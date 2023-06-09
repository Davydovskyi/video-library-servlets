package edu.jcourse.servlet;

import edu.jcourse.dto.ReceiveMovieDto;
import edu.jcourse.service.DownloadService;
import edu.jcourse.service.ServiceProvider;
import edu.jcourse.util.UrlPath;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@WebServlet(urlPatterns = UrlPath.DOWNLOAD + "/*")
public class DownloadServlet extends HttpServlet {

    private final transient DownloadService downloadService = ServiceProvider.getInstance().getDownloadService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String attributeName = req.getPathInfo().split("/")[1];

        resp.setHeader("Content-Disposition", "attachment; filename=\"result.csv\"");
        resp.setContentType("text/csv");

        List<ReceiveMovieDto> movies = (List<ReceiveMovieDto>) req.getSession().getAttribute(attributeName);

        try (ServletOutputStream stream = resp.getOutputStream();
             InputStream inputStream = downloadService.get(movies)) {
            byte[] bytes = inputStream.readAllBytes();
            resp.setContentLength(bytes.length);
            stream.write(bytes);
        } finally {
            req.getSession().removeAttribute(attributeName);
        }
    }
}