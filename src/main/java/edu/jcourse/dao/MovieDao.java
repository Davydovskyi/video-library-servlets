package edu.jcourse.dao;

import edu.jcourse.dto.MovieFilterDto;
import edu.jcourse.entity.Genre;
import edu.jcourse.entity.Movie;
import edu.jcourse.exception.DAOException;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface MovieDao extends Dao<Long, Movie> {

    Optional<Movie> findByAllFields(String title, Integer releaseYear, String country, Genre genre) throws DAOException;

    List<Movie> findAll(MovieFilterDto movieFilterDTO) throws DAOException;

    List<Movie> findAllByPersonId(Long personId) throws DAOException;

    Optional<Movie> findById(Long id, Connection connection) throws DAOException;
}