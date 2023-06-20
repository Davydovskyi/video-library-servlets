package edu.jcourse.service;

import edu.jcourse.dto.ReceiveMovieDto;
import edu.jcourse.mapper.impl.CSVMovieMapper;
import edu.jcourse.service.impl.DownloadServiceImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DownloadServiceTest {

    @Mock
    private CSVMovieMapper csvMovieMapper;
    @InjectMocks
    private DownloadServiceImpl downloadService;

    @SneakyThrows
    @Test
    void testDownload() {
        String string1 = "Title,2020,Action,US,Description";
        String string2 = "Title2,2021,Action,US,Description";

        when(csvMovieMapper.mapFrom(any())).thenReturn(string1).thenReturn(string2);

        List<ReceiveMovieDto> receiveMovieDtos = List.of(
                buildReceiveMovieDto("Title", 2020),
                buildReceiveMovieDto("Title2", 2021)
        );

        byte[] actualResult = downloadService.get(receiveMovieDtos).readAllBytes();

        byte[] expectedResult = (string1 + "\n" + string2).getBytes();

        assertThat(actualResult).isEqualTo(expectedResult);
        verify(csvMovieMapper, times(2)).mapFrom(any());
    }

    @SneakyThrows
    @Test
    void testDownloadWithEmptyList() {
        List<ReceiveMovieDto> receiveMovieDtos = List.of();

        byte[] actualResult = downloadService.get(receiveMovieDtos).readAllBytes();

        assertThat(actualResult).isEmpty();
    }

    @SneakyThrows
    @Test
    void testDownloadWithNullList() {
        byte[] actualResult = downloadService.get(null).readAllBytes();

        assertThat(actualResult).isEmpty();
    }

    private ReceiveMovieDto buildReceiveMovieDto(String title, int releaseYear) {
        return ReceiveMovieDto.builder()
                .title(title)
                .releaseYear(releaseYear)
                .genre("Action")
                .country("US")
                .description("Description")
                .build();
    }
}