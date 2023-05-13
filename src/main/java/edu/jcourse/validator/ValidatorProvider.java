package edu.jcourse.validator;

import edu.jcourse.validator.impl.CreateUserValidator;
import edu.jcourse.validator.impl.LoginValidator;
import lombok.Getter;

public class ValidatorProvider {

    private static ValidatorProvider instance;
    @Getter
    private final CreateUserValidator createUserValidator;
    @Getter
    private final LoginValidator loginValidator;

    private ValidatorProvider() {
        createUserValidator = new CreateUserValidator();
        loginValidator = new LoginValidator();
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
