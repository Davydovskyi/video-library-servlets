package edu.jcourse.validator;

import edu.jcourse.validator.impl.CreateUserValidator;
import lombok.Getter;

public class ValidatorProvider {

    private static ValidatorProvider instance;
    @Getter
    private final CreateUserValidator createUserValidator;

    private ValidatorProvider() {
        createUserValidator = new CreateUserValidator();
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
