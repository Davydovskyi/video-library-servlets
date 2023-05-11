package edu.jcourse.service;

public class ServiceProvider {

    private static volatile ServiceProvider instance;

    private ServiceProvider() {
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
