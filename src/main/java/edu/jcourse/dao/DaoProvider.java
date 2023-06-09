package edu.jcourse.dao;

import edu.jcourse.dao.impl.*;
import lombok.Getter;

public class DaoProvider {

    private static DaoProvider instance;
    @Getter
    private final UserDao userDao;
    @Getter
    private final PersonDao personDao;
    @Getter
    private final MovieDao movieDao;
    @Getter
    private final MoviePersonDao moviePersonDao;
    @Getter
    private final ReviewDao reviewDao;

    private DaoProvider() {
        userDao = new UserDaoImpl();
        personDao = new PersonDaoImpl();
        movieDao = new MovieDaoImpl();
        moviePersonDao = new MoviePersonDaoImpl();
        reviewDao = new ReviewDaoImpl();
    }

    public static DaoProvider getInstance() {
        if (instance != null) {
            return instance;
        }

        synchronized (DaoProvider.class) {
            if (instance == null) {
                instance = new DaoProvider();
            }
        }
        return instance;
    }
}