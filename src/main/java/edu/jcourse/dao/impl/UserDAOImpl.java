package edu.jcourse.dao.impl;

import edu.jcourse.dao.UserDAO;
import edu.jcourse.entity.User;
import edu.jcourse.exception.DAOException;

import java.util.List;
import java.util.Optional;

public class UserDAOImpl implements UserDAO {
    @Override
    public boolean delete(Long aLong) throws DAOException {
        return false;
    }

    @Override
    public User save(User user) throws DAOException {
        return null;
    }

    @Override
    public void update(User user) throws DAOException {

    }

    @Override
    public List<User> findAll() throws DAOException {
        return null;
    }

    @Override
    public Optional<User> findById(Long aLong) throws DAOException {
        return Optional.empty();
    }
}
