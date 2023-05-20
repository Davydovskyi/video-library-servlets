package edu.jcourse.dao;

import edu.jcourse.dao.impl.MovieDAOImpl;
import edu.jcourse.dao.impl.MoviePersonDAOImpl;
import edu.jcourse.dao.impl.PersonDAOImpl;
import edu.jcourse.dao.impl.UserDAOImpl;
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

    private DAOProvider() {
        userDAO = new UserDAOImpl();
        personDAO = new PersonDAOImpl();
        movieDAO = new MovieDAOImpl();
        moviePersonDAO = new MoviePersonDAOImpl();
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
