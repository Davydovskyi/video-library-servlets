package edu.jcourse.dao;

import edu.jcourse.entity.MoviePerson;
import edu.jcourse.exception.DAOException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface MoviePersonDao extends Dao<Long, MoviePerson> {

    MoviePerson save(MoviePerson moviePerson, Connection connection) throws SQLException;

    List<MoviePerson> findAllByMovieId(Long movieId, Connection connection) throws DAOException;
}
