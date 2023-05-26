package edu.jcourse.service;

import edu.jcourse.service.impl.*;
import lombok.Getter;

public class ServiceProvider {

    private static ServiceProvider instance;
    @Getter
    private final UserService userService;
    @Getter
    private final ImageService imageService;
    @Getter
    private final PersonService personService;
    @Getter
    private final MovieService movieService;
    @Getter
    private final ReviewService reviewService;
    @Getter
    private final DownloadService downloadService;

    private ServiceProvider() {
        userService = new UserServiceImpl();
        imageService = new ImageServiceImpl();
        personService = new PersonServiceImpl();
        movieService = new MovieServiceImpl();
        reviewService = new ReviewServiceImpl();
        downloadService = new DownloadServiceImpl();
    }

    public static ServiceProvider getInstance() {
        if (instance != null) {
            return instance;
        }

        synchronized (ServiceProvider.class) {
            if (instance == null) {
                instance = new ServiceProvider();
            }
            return instance;
        }
    }
}