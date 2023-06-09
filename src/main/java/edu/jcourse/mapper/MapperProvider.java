package edu.jcourse.mapper;

import edu.jcourse.mapper.impl.*;
import lombok.Getter;

public class MapperProvider {

    private static MapperProvider instance;
    @Getter
    private final CreateUserMapper createUserMapper;
    @Getter
    private final UserMapper userMapper;
    @Getter
    private final CreatePersonMapper createPersonMapper;
    @Getter
    private final PersonMapper personMapper;
    @Getter
    private final CreateMovieMapper createMovieMapper;
    @Getter
    private final CreateMoviePersonMapper createMoviePersonMapper;
    @Getter
    private final MovieMapper movieMapper;
    @Getter
    private final MoviePersonMapper moviePersonMapper;
    @Getter
    private final ReviewMapper reviewMapper;
    @Getter
    private final CreateReviewMapper createReviewMapper;
    @Getter
    private final MovieReviewMapper movieReviewMapper;
    @Getter
    private final CSVMovieMapper csvMovieMapper;


    private MapperProvider() {
        createUserMapper = new CreateUserMapper();
        userMapper = new UserMapper();
        createPersonMapper = new CreatePersonMapper();
        personMapper = new PersonMapper();
        createMovieMapper = new CreateMovieMapper();
        createMoviePersonMapper = new CreateMoviePersonMapper();
        movieMapper = new MovieMapper();
        moviePersonMapper = new MoviePersonMapper();
        reviewMapper = new ReviewMapper();
        createReviewMapper = new CreateReviewMapper();
        movieReviewMapper = new MovieReviewMapper();
        csvMovieMapper = new CSVMovieMapper();
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