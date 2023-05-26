package edu.jcourse.dao;

import edu.jcourse.dto.MovieFilterDTO;
import edu.jcourse.entity.Genre;
import edu.jcourse.entity.Movie;
import edu.jcourse.exception.DAOException;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface MovieDAO extends DAO<Long, Movie> {

    Optional<Movie> findByAllFields(String title, Integer releaseYear, String country, Genre genre) throws DAOException;

    List<Movie> findAll(MovieFilterDTO movieFilterDTO) throws DAOException;

    List<Movie> findAllByPersonId(Long personId) throws DAOException;

    Optional<Movie> findById(Long id, Connection connection) throws DAOException;
}