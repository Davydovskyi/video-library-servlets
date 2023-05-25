package edu.jcourse.dao.impl;

import edu.jcourse.dao.DAOProvider;
import edu.jcourse.dao.MovieDAO;
import edu.jcourse.dao.MoviePersonDAO;
import edu.jcourse.dao.ReviewDAO;
import edu.jcourse.dto.MovieFilterDTO;
import edu.jcourse.entity.Genre;
import edu.jcourse.entity.Movie;
import edu.jcourse.entity.MoviePerson;
import edu.jcourse.entity.Review;
import edu.jcourse.exception.DAOException;
import edu.jcourse.util.ConnectionBuilder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MovieDAOImpl implements MovieDAO {

    private static final String FIND_ALL_SQL = """
            SELECT DISTINCT m.movie_id,
            m.movie_title,
            m.release_year,
            m.movie_country,
            m.movie_genre,
            m.movie_description
            FROM movie m
            """;
    private static final String FIND_BY_ALL_FIELDS_SQL = FIND_ALL_SQL + """
            WHERE m.movie_title = ?
            AND m.release_year = ?
            AND m.movie_country = ?
            AND m.movie_genre = ?
            """;

    private static final String SAVE_SQL = """
            INSERT INTO movie(movie_title, release_year, movie_country, movie_genre, movie_description)
            VALUES (?, ?, ?, ?, ?);
            """;

    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE m.movie_id = ?;
            """;

    private static final String FIND_ALL_BY_PERSON_ID_SQL = FIND_ALL_SQL + """
            JOIN movie_person mp on m.movie_id = mp.movie_id
            JOIN person p on p.person_id = mp.person_id
            WHERE p.person_id = ?;
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
        try (Connection connection = ConnectionBuilder.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);

            Movie movie = null;
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                movie = buildMovie(resultSet);
                setMoviePersons(movie, resultSet);
                setReviews(movie, resultSet);
            }

            return Optional.ofNullable(movie);
        } catch (SQLException e) {
            throw new DAOException(e);
        }
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

    @Override
    public List<Movie> findAll(MovieFilterDTO movieFilterDTO) throws DAOException {
        List<Movie> movies = new ArrayList<>();

        List<Object> params = new ArrayList<>();
        List<String> whereSQL = new ArrayList<>();

        if (!movieFilterDTO.title().isBlank()) {
            whereSQL.add("upper(movie_title) LIKE upper(?)");
            params.add("%" + movieFilterDTO.title() + "%");
        }
        if (!movieFilterDTO.releaseYear().isBlank()) {
            whereSQL.add("release_year = ?");
            params.add(Integer.parseInt(movieFilterDTO.releaseYear()));
        }
        if (!movieFilterDTO.country().isBlank()) {
            whereSQL.add("upper(movie_country) LIKE upper(?)");
            params.add("%" + movieFilterDTO.country() + "%");
        }
        if (movieFilterDTO.genre() != null) {
            whereSQL.add("movie_genre = ?");
            params.add(movieFilterDTO.genre());
        }

        params.add(movieFilterDTO.limit());
        params.add(movieFilterDTO.offset());

        String where = " ORDER BY movie_title LIMIT ? OFFSET ? ";
        if (!whereSQL.isEmpty()) {
            where = whereSQL.stream().collect(Collectors.joining(" AND ", " WHERE ", where));
        }

        String sql = FIND_ALL_SQL + where;

        try (Connection connection = ConnectionBuilder.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i));
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                movies.add(buildMovie(resultSet));
            }
            return movies;
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
                .moviePersons(new ArrayList<>())
                .reviews(new ArrayList<>())
                .build();
    }

    @Override
    public List<Movie> findAllByPersonId(Long personId) throws DAOException {
        List<Movie> movies = new ArrayList<>();
        try (Connection connection = ConnectionBuilder.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_BY_PERSON_ID_SQL)) {
            preparedStatement.setLong(1, personId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                movies.add(buildMovie(resultSet));
            }
            return movies;
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    private void setMoviePersons(Movie movie, ResultSet resultSet) throws SQLException, DAOException {
        MoviePersonDAO moviePersonDAO = DAOProvider.getInstance().getMoviePersonDAO();
        List<MoviePerson> moviePeople = moviePersonDAO.findAllByMovieId(movie.getId(), resultSet.getStatement().getConnection());
        movie.setMoviePersons(moviePeople);
        moviePeople.forEach(moviePerson -> moviePerson.setMovie(movie));
    }

    private void setReviews(Movie movie, ResultSet resultSet) throws SQLException, DAOException {
        ReviewDAO reviewDAO = DAOProvider.getInstance().getReviewDAO();
        List<Review> reviews = reviewDAO.findAllByMovieId(movie.getId(), resultSet.getStatement().getConnection());
        movie.setReviews(reviews);
        reviews.forEach(review -> review.setMovie(movie));
    }
}