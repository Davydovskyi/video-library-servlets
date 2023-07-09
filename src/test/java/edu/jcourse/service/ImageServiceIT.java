package edu.jcourse.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ImageServiceIT {

    private final ImageService imageService = ServiceProvider.getInstance().getImageService();

    @SneakyThrows
    @Test
    void get() {
        String imagePath = "appImage/image.PNG";

        Optional<InputStream> imageContent = imageService.get(imagePath);

        assertThat(imageContent).isPresent();
    }

    @SneakyThrows
    @Test
    void shouldNotFindIfImageDoesNotExist() {
        String imagePath = "dummyImage/dummy.PNG";

        Optional<InputStream> imageContent = imageService.get(imagePath);

        assertThat(imageContent).isEmpty();
    }
}