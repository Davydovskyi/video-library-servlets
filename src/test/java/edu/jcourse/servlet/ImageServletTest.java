package edu.jcourse.servlet;

import edu.jcourse.service.ImageService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageServletTest {

    @Mock
    private HttpServletRequest req;
    @Mock
    private HttpServletResponse resp;
    @Mock
    private ServletOutputStream outputStream;
    @Mock
    private ImageService imageService;
    @Mock
    private InputStream inputStream;
    @InjectMocks
    private ImageServlet imageServlet;

    @SneakyThrows
    @Test
    void doGet() {
        doReturn("/images/test.jpg").when(req).getRequestURI();
        doReturn(Optional.of(inputStream)).when(imageService).get(any());
        doReturn(outputStream).when(resp).getOutputStream();
        when(inputStream.read(any())).thenReturn(1).thenReturn(-1);

        imageServlet.doGet(req, resp);

        verify(req).getRequestURI();
        verify(imageService).get("/test.jpg");
        verify(resp).setContentType("application/octet-stream");
        verify(resp).getOutputStream();
        verify(inputStream, atLeastOnce()).read(any());
        verify(outputStream).write(any(), eq(0), eq(1));
        verify(resp, never()).setStatus(404);
    }

    @SneakyThrows
    @Test
    void doGetWhenImageNotFound() {
        doReturn("/images/test.jpg").when(req).getRequestURI();
        doReturn(Optional.empty()).when(imageService).get(any());

        imageServlet.doGet(req, resp);

        verify(req).getRequestURI();
        verify(imageService).get("/test.jpg");
        verify(resp).setStatus(404);
        verify(resp, never()).getOutputStream();
        verifyNoInteractions(inputStream, outputStream);
        verify(resp, never()).setContentType(anyString());
    }
}