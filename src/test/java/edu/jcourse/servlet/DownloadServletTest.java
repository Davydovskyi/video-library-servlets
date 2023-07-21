package edu.jcourse.servlet;

import edu.jcourse.dto.ReceiveMovieDto;
import edu.jcourse.service.DownloadService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DownloadServletTest {

    @Mock
    private DownloadService downloadService;
    @Mock
    private HttpServletRequest req;
    @Mock
    private HttpServletResponse resp;
    @Mock
    private HttpSession session;
    @Mock
    private ServletOutputStream outputStream;
    @Mock
    private InputStream inputStream;
    @InjectMocks
    private DownloadServlet downloadServlet;

    @SneakyThrows
    @Test
    void doGet() {
        doReturn("/movies").when(req).getPathInfo();
        doReturn(session).when(req).getSession();
        List<ReceiveMovieDto> receiveMovieDtos = List.of(buildReceiveMovieDto("Title1"),
                buildReceiveMovieDto("Title2"));
        doReturn(receiveMovieDtos).when(session).getAttribute("movies");
        doReturn(outputStream).when(resp).getOutputStream();
        doReturn(inputStream).when(downloadService).get(any());
        byte[] bytes = buildBytes(receiveMovieDtos);
        doReturn(bytes).when(inputStream).readAllBytes();

        downloadServlet.doGet(req, resp);

        verify(req).getPathInfo();
        verify(resp).setHeader("Content-Disposition", "attachment; filename=\"result.csv\"");
        verify(resp).setContentType("text/csv");
        verify(req, times(2)).getSession();
        verify(session).getAttribute("movies");
        verify(resp).getOutputStream();
        verify(downloadService).get(receiveMovieDtos);
        verify(inputStream).readAllBytes();
        verify(resp).setContentLength(bytes.length);
        outputStream.write(bytes);
        session.removeAttribute("movies");
    }

    private ReceiveMovieDto buildReceiveMovieDto(String title) {
        return ReceiveMovieDto.builder()
                .title(title)
                .releaseYear(2020)
                .genre("Action")
                .country("US")
                .description("Description")
                .build();
    }

    private byte[] buildBytes(List<ReceiveMovieDto> movieDtos) {
        StringBuilder stringBuilder = new StringBuilder();
        for (ReceiveMovieDto movieDto : movieDtos) {
            stringBuilder.append(movieDto.title()).append(",");
            stringBuilder.append(movieDto.releaseYear()).append(",");
            stringBuilder.append(movieDto.genre()).append(",");
            stringBuilder.append(movieDto.country()).append(",");
            stringBuilder.append(movieDto.description()).append("\n");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString().getBytes();
    }

}