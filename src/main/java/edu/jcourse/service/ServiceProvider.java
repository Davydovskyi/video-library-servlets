package edu.jcourse.service;

import edu.jcourse.service.impl.UserServiceImpl;

public class ServiceProvider {

    private static volatile ServiceProvider instance;

    private final UserService userService;

    private ServiceProvider() {
        userService = new UserServiceImpl();
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

    public UserService getUserService() {
        return userService;
    }
}
