package edu.jcourse.integration;

import edu.jcourse.util.ConnectionBuilder;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;
import java.sql.Statement;

public abstract class IntegrationTestBase {
    private static final String CLEAN_REVIEW_SQL = """
            DROP TABLE IF EXISTS review;
            """;
    private static final String CLEAN_MOVIE_PERSON_SQL = """
            DROP TABLE IF EXISTS movie_person;
            """;
    private static final String CLEAN_MOVIE_SQL = """
            DROP TABLE IF EXISTS movie;
            """;
    private static final String CLEAN_PERSON_SQL = """
            DROP TABLE IF EXISTS person;
            """;
    private static final String CLEAN_USERS_SQL = """
            DROP TABLE IF EXISTS users;
            """;
    private static final String CREATE_USERS_SQL = """
            CREATE TABLE IF NOT EXISTS users
            (
                user_id         int AUTO_INCREMENT,
                user_name       varchar(124) not null,
                user_birth_date date         not null,
                user_image      varchar(124) not null,
                user_email      varchar(124) not null,
                user_password   varchar(32)  not null,
                role            varchar(32)  not null,
                gender          varchar(16)  not null,
                PRIMARY KEY (user_id),
                UNIQUE (user_email)
            );
            """;
    private static final String CREATE_PERSON_SQL = """
            CREATE TABLE IF NOT EXISTS person
            (
                person_id         int AUTO_INCREMENT,
                person_name       varchar(256) not null,
                person_birth_date date         not null,
                PRIMARY KEY (person_id),
                UNIQUE (person_name, person_birth_date)
            );
            """;
    private static final String CREATE_MOVIE_SQL = """
            CREATE TABLE IF NOT EXISTS movie
            (
                movie_id          int AUTO_INCREMENT,
                movie_title       varchar(256),
                release_year      smallint     not null,
                movie_country     varchar(124) not null,
                movie_genre       varchar(124) not null,
                movie_description text         not null,
                PRIMARY KEY (movie_id)
            );
            """;
    private static final String CREATE_MOVIE_PERSON_SQL = """
            CREATE TABLE IF NOT EXISTS movie_person
            (
                id          int AUTO_INCREMENT,
                movie_id    int not null,
                person_id   int not null,
                person_role varchar(124) not null,
                PRIMARY KEY (id),
                FOREIGN KEY (movie_id) REFERENCES movie (movie_id) ON DELETE RESTRICT,
                FOREIGN KEY (person_id) REFERENCES person (person_id) ON DELETE RESTRICT
            );
            """;
    private static final String CREATE_REVIEW_SQL = """
            CREATE TABLE IF NOT EXISTS review
            (
                review_id   int AUTO_INCREMENT,
                movie_id    int not null,
                user_id     int not null,
                review_text varchar(256) not null,
                rate        smallint not null,
                PRIMARY KEY (review_id),
                UNIQUE (movie_id, user_id),
                FOREIGN KEY (movie_id) REFERENCES movie (movie_id) ON DELETE RESTRICT,
                FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE RESTRICT
            );
            """;
    private static final String INSERT_PERSON_SQL = """
            INSERT INTO person (person_name, person_birth_date)
            VALUES ('Person1', '1990-01-01');
            """;
    private static final String INSERT_MOVIE_SQL = """
                    INSERT INTO movie (movie_title,
                    release_year,
                    movie_country,
                    movie_genre,
                    movie_description)
                    VALUES ('Movie1', 1990, 'US', 'ACTION', 'Description');
            """;
    private static final String INSERT_USER_SQL = """
            INSERT INTO users (user_name,
            user_birth_date,
            user_image,
            user_email,
            user_password,
            role,
            gender)
            VALUES ('User1', '1990-01-01', 'image', 'email', 'password', 'USER', 'MALE');
            """;

    @SneakyThrows
    @BeforeEach
    void prepareDatabase() {
        try (Connection connection = ConnectionBuilder.getConnection();
             Statement statement = connection.createStatement()) {
            statement.addBatch(CLEAN_REVIEW_SQL);
            statement.addBatch(CLEAN_MOVIE_PERSON_SQL);
            statement.addBatch(CLEAN_MOVIE_SQL);
            statement.addBatch(CLEAN_PERSON_SQL);
            statement.addBatch(CLEAN_USERS_SQL);
            statement.addBatch(CREATE_USERS_SQL);
            statement.addBatch(CREATE_PERSON_SQL);
            statement.addBatch(CREATE_MOVIE_SQL);
            statement.addBatch(CREATE_MOVIE_PERSON_SQL);
            statement.addBatch(CREATE_REVIEW_SQL);
            statement.addBatch(INSERT_PERSON_SQL);
            statement.addBatch(INSERT_MOVIE_SQL);
            statement.addBatch(INSERT_USER_SQL);
            statement.executeBatch();
        }
    }
}