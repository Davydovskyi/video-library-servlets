package edu.jcourse.validator;

import edu.jcourse.validator.impl.CreatePersonValidator;
import edu.jcourse.validator.impl.CreateUserValidator;
import edu.jcourse.validator.impl.LoginValidator;
import lombok.Getter;

public class ValidatorProvider {

    private static ValidatorProvider instance;
    @Getter
    private final CreateUserValidator createUserValidator;
    @Getter
    private final LoginValidator loginValidator;
    @Getter
    private final CreatePersonValidator createPersonValidator;

    private ValidatorProvider() {
        createUserValidator = new CreateUserValidator();
        loginValidator = new LoginValidator();
        createPersonValidator = new CreatePersonValidator();
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
