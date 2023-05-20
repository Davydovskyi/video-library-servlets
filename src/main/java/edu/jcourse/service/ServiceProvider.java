package edu.jcourse.service;

import edu.jcourse.service.impl.ImageServiceImpl;
import edu.jcourse.service.impl.MovieServiceImpl;
import edu.jcourse.service.impl.PersonServiceImpl;
import edu.jcourse.service.impl.UserServiceImpl;
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

    private ServiceProvider() {
        userService = new UserServiceImpl();
        imageService = new ImageServiceImpl();
        personService = new PersonServiceImpl();
        movieService = new MovieServiceImpl();
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
