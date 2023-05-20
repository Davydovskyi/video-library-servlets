package edu.jcourse.dao.impl;

import edu.jcourse.dao.DAOProvider;
import edu.jcourse.dao.MovieDAO;
import edu.jcourse.dao.MoviePersonDAO;
import edu.jcourse.entity.Genre;
import edu.jcourse.entity.Movie;
import edu.jcourse.entity.MoviePerson;
import edu.jcourse.exception.DAOException;
import edu.jcourse.util.ConnectionBuilder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MovieDAOImpl implements MovieDAO {

    private static final String FIND_ALL_SQL = """
            SELECT movie_id,
            movie_title,
            release_year,
            movie_country,
            movie_genre,
            movie_description
            FROM movie
            """;
    private static final String FIND_BY_ALL_FIELDS_SQL = FIND_ALL_SQL + """
            WHERE movie_title = ?
            AND release_year = ?
            AND movie_country = ?
            AND movie_genre = ?
            """;

    private static final String SAVE_SQL = """
            INSERT INTO movie(movie_title, release_year, movie_country, movie_genre, movie_description)
            VALUES (?, ?, ?, ?, ?);
            """;

    @Override
    public boolean delete(Long id) throws DAOException {
        return false;
    }

    @Override
    public Movie save(Movie movie) throws DAOException {
        try (Connection connection = ConnectionBuilder.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {

            MoviePersonDAO moviePersonDAO = DAOProvider.getInstance().getMoviePersonDAO();

            connection.setAutoCommit(false);

            try {
                preparedStatement.setString(1, movie.getTitle());
                preparedStatement.setInt(2, movie.getReleaseYear());
                preparedStatement.setString(3, movie.getCountry());
                preparedStatement.setString(4, movie.getGenre().name());
                preparedStatement.setString(5, movie.getDescription());

                preparedStatement.executeUpdate();
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    movie.setId(generatedKeys.getLong("movie_id"));
                }

                for (MoviePerson moviePerson : movie.getMoviePersons()) {
                    moviePerson.setMovie(movie);
                    moviePersonDAO.save(moviePerson, connection);
                }

                connection.commit();
                return movie;
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public void update(Movie movie) throws DAOException {

    }

    @Override
    public List<Movie> findAll() throws DAOException {
        return new ArrayList<>();
    }

    @Override
    public Optional<Movie> findById(Long id) throws DAOException {
        return Optional.empty();
    }

    @Override
    public Optional<Movie> findByAllFields(String title, Integer releaseYear, String country, Genre genre) throws DAOException {
        try (Connection connection = ConnectionBuilder.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ALL_FIELDS_SQL)) {
            preparedStatement.setString(1, title);
            preparedStatement.setInt(2, releaseYear);
            preparedStatement.setString(3, country);
            preparedStatement.setString(4, genre.name());

            Movie movie = null;
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                movie = buildMovie(resultSet);
            }
            return Optional.ofNullable(movie);
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    private Movie buildMovie(ResultSet resultSet) throws SQLException {
        return Movie.builder()
                .id(resultSet.getLong("movie_id"))
                .title(resultSet.getString("movie_title"))
                .releaseYear(resultSet.getInt("release_year"))
                .country(resultSet.getString("movie_country"))
                .genre(Genre.valueOf(resultSet.getString("movie_genre")))
                .description(resultSet.getString("movie_description"))
                .build();
    }
}