package edu.jcourse.mapper;

import edu.jcourse.mapper.impl.CreateUserMapper;
import lombok.Getter;

public class MapperProvider {

    private static MapperProvider instance;
    @Getter
    private final CreateUserMapper createUserMapper;

    private MapperProvider() {
        createUserMapper = new CreateUserMapper();
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
