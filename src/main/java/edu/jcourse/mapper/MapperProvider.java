package edu.jcourse.mapper;

public class MapperProvider {

    private static MapperProvider instance;

    private MapperProvider() {
    }

    public static MapperProvider getInstance() {
        if (instance != null) {
            return instance;
        }

        synchronized (MapperProvider.class) {
            if (instance == null) {
                instance = new MapperProvider();
            }
            return instance;
        }
    }
}
