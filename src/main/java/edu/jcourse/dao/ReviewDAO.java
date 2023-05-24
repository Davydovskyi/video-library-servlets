package edu.jcourse.dao;

import edu.jcourse.entity.Review;
import edu.jcourse.exception.DAOException;

import java.sql.Connection;
import java.util.List;

public interface ReviewDAO extends DAO<Long, Review> {

    List<Review> findByMovieId(Long movieId, Connection connection) throws DAOException;
}