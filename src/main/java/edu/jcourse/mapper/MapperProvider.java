package edu.jcourse.mapper;

import edu.jcourse.mapper.impl.CreateUserMapper;
import edu.jcourse.mapper.impl.UserMapper;
import lombok.Getter;

public class MapperProvider {

    private static MapperProvider instance;
    @Getter
    private final CreateUserMapper createUserMapper;
    @Getter
    private final UserMapper userMapper;

    private MapperProvider() {
        createUserMapper = new CreateUserMapper();
        userMapper = new UserMapper();
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
