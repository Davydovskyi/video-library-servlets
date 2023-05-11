package edu.jcourse.dao;

import edu.jcourse.entity.User;
import edu.jcourse.exception.DAOException;

import java.util.Optional;

public interface UserDAO extends DAO<Long, User> {

    Optional<User> findByEmail(String email) throws DAOException;

    Optional<User> findByEmailAndPassword(String email, String password) throws DAOException;
}
