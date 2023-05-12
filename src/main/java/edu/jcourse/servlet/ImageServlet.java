package edu.jcourse.servlet;

import edu.jcourse.exception.ServiceException;
import edu.jcourse.service.ImageService;
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

@WebServlet(urlPatterns = UrlPath.IMAGES + "/*")
public class ImageServlet extends HttpServlet {

    private final transient ImageService imageService = ServiceProvider.getInstance().getImageService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String imagePath = req.getRequestURI().replace("/images", "");

        try {
            imageService.get(imagePath)
                    .ifPresentOrElse(inputStream -> {
                        resp.setContentType("application/octet-stream");
                        writeImage(inputStream, resp);
                    }, () -> resp.setStatus(404));
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeImage(InputStream inputStream, HttpServletResponse resp) {
        try (inputStream;
             ServletOutputStream outputStream = resp.getOutputStream()) {
            byte[] buffer = new byte[1024];
            int currentBytes;
            while ((currentBytes = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, currentBytes);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
