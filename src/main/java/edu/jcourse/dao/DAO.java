package edu.jcourse.dao;

import edu.jcourse.exception.DAOException;

import java.util.List;
import java.util.Optional;

public interface DAO<K, E> {

    boolean delete(K k) throws DAOException;

    E save(E e) throws DAOException;

    void update(E e) throws DAOException;

    List<E> findAll() throws DAOException;

    Optional<E> findById(K k) throws DAOException;
}
