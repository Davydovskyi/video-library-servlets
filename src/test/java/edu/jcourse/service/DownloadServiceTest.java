package edu.jcourse.service;

import edu.jcourse.dto.ReceiveMovieDto;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DownloadServiceTest {

    private final DownloadService downloadService = ServiceProvider.getInstance().getDownloadService();

    @SneakyThrows
    @Test
    void testDownload() {
        List<ReceiveMovieDto> receiveMovieDtos = List.of(
                buildReceiveMovieDto("Title", 2020),
                buildReceiveMovieDto("Title2", 2021));

        byte[] actualResult = downloadService.get(receiveMovieDtos).readAllBytes();

        String stringBuilder = "Title(Action, 2020, US)" + "\n" +
                               "Title2(Action, 2021, US)";
        byte[] expectedResult = stringBuilder.getBytes();

        assertThat(actualResult).isEqualTo(expectedResult);
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
                .movieData("%s(%s, %d, %s)".formatted(
                        title,
                        "Action",
                        releaseYear,
                        "US"
                ))
                .build();
    }
}