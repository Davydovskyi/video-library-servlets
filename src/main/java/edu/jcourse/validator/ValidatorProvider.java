package edu.jcourse.validator;

public class ValidatorProvider {

    private static ValidatorProvider instance;

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
