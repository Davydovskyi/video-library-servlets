package edu.jcourse.dao;

import edu.jcourse.dao.impl.UserDAOImpl;

public class DAOProvider {

    private static volatile DAOProvider instance;

    private final UserDAO userDAO;

    private DAOProvider() {
        userDAO = new UserDAOImpl();
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

    public UserDAO getUserDAO() {
        return userDAO;
    }
}
