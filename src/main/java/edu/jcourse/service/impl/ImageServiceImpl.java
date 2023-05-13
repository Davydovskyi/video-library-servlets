package edu.jcourse.service.impl;

import edu.jcourse.exception.ServiceException;
import edu.jcourse.service.ImageService;
import edu.jcourse.util.Config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

public class ImageServiceImpl implements ImageService {

    private final String basePath = Config.getProperty(Config.IMAGE_BASE_URL);

    @Override
    public void upload(String imagePath, InputStream imageContent) throws ServiceException {
        Path path = Path.of(basePath, imagePath);

        try (imageContent) {
            Files.createDirectories(path.getParent());
            Files.write(path, imageContent.readAllBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Optional<InputStream> get(String imagePath) throws ServiceException {
        Path path = Path.of(basePath, imagePath);

        try {
            return Files.exists(path)
                    ? Optional.of(Files.newInputStream(path))
                    : Optional.empty();
        } catch (IOException e) {
            throw new ServiceException(e);
        }
    }
}
