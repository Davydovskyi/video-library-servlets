package edu.jcourse.dao;

import edu.jcourse.dao.impl.PersonDAOImpl;
import edu.jcourse.dao.impl.UserDAOImpl;
import lombok.Getter;

public class DAOProvider {

    private static DAOProvider instance;
    @Getter
    private final UserDAO userDAO;
    @Getter
    private final PersonDAO personDAO;

    private DAOProvider() {
        userDAO = new UserDAOImpl();
        personDAO = new PersonDAOImpl();
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
