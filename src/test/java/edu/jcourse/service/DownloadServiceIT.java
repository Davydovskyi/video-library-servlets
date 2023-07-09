package edu.jcourse.service;

import edu.jcourse.dto.ReceiveMovieDto;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DownloadServiceIT {

    private final DownloadService downloadService = ServiceProvider.getInstance().getDownloadService();

    @SneakyThrows
    @Test
    void get() {
        List<ReceiveMovieDto> receiveMovieDtos = List.of(
                buildReceiveMovieDto(2000),
                buildReceiveMovieDto(2001));

        byte[] actualResult = downloadService.get(receiveMovieDtos).readAllBytes();

        assertThat(actualResult).isEqualTo(buildBytes(receiveMovieDtos));
    }

    @SneakyThrows
    @Test
    void shouldReturnEmptyBytesArrayIfListIsNull() {
        List<ReceiveMovieDto> receiveMovieDtos = null;

        byte[] actualResult = downloadService.get(receiveMovieDtos).readAllBytes();

        assertThat(actualResult).isEmpty();
    }

    private ReceiveMovieDto buildReceiveMovieDto(int releaseYear) {
        return ReceiveMovieDto.builder()
                .title("Title")
                .releaseYear(releaseYear)
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