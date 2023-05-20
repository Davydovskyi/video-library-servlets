package edu.jcourse.validator;

import edu.jcourse.validator.impl.*;
import lombok.Getter;

public class ValidatorProvider {

    private static ValidatorProvider instance;
    @Getter
    private final CreateUserValidator createUserValidator;
    @Getter
    private final LoginValidator loginValidator;
    @Getter
    private final CreatePersonValidator createPersonValidator;
    @Getter
    private final CreateMovieValidator createMovieValidator;
    @Getter
    private final CreateMoviePersonValidator createMoviePersonValidator;

    private ValidatorProvider() {
        createUserValidator = new CreateUserValidator();
        loginValidator = new LoginValidator();
        createPersonValidator = new CreatePersonValidator();
        createMovieValidator = new CreateMovieValidator();
        createMoviePersonValidator = new CreateMoviePersonValidator();
    }

    public static ValidatorProvider getInstance() {
        if (instance != null) {
            return instance;
        }

        synchronized (ValidatorProvider.class) {
            if (instance == null) {
                instance = new ValidatorProvider();
            }
            return instance;
        }
    }
}
