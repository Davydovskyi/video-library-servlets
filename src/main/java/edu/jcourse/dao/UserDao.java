package edu.jcourse.dao;

import edu.jcourse.entity.User;
import edu.jcourse.exception.DAOException;

import java.sql.Connection;
import java.util.Optional;

public interface UserDao extends Dao<Long, User> {

    Optional<User> findByEmail(String email) throws DAOException;

    Optional<User> findByEmailAndPassword(String email, String password) throws DAOException;

    Optional<User> findById(Long id, Connection connection) throws DAOException;
}