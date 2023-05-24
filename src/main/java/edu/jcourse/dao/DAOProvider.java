package edu.jcourse.dao;

import edu.jcourse.dao.impl.*;
import lombok.Getter;

public class DAOProvider {

    private static DAOProvider instance;
    @Getter
    private final UserDAO userDAO;
    @Getter
    private final PersonDAO personDAO;
    @Getter
    private final MovieDAO movieDAO;
    @Getter
    private final MoviePersonDAO moviePersonDAO;
    @Getter
    private final ReviewDAO reviewDAO;

    private DAOProvider() {
        userDAO = new UserDAOImpl();
        personDAO = new PersonDAOImpl();
        movieDAO = new MovieDAOImpl();
        moviePersonDAO = new MoviePersonDAOImpl();
        reviewDAO = new ReviewDAOImpl();
    }

    public static DAOProvider getInstance() {
        if (instance != null) {
            return instance;
        }

        synchronized (DAOProvider.class) {
            if (instance == null) {
                instance = new DAOProvider();
            }
        }
        return instance;
    }
}