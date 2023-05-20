package edu.jcourse.dao;

import edu.jcourse.entity.MoviePerson;

import java.sql.Connection;
import java.sql.SQLException;

public interface MoviePersonDAO extends DAO<Long, MoviePerson> {

    MoviePerson save(MoviePerson moviePerson, Connection connection) throws SQLException;
}
