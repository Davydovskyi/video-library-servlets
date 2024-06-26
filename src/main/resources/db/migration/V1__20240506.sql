DROP TABLE IF EXISTS review;
DROP TABLE IF EXISTS movie_person;
DROP TABLE IF EXISTS movie;
DROP TABLE IF EXISTS person;
DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users
(
    user_id         SERIAL,
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

CREATE TABLE IF NOT EXISTS person
(
    person_id         SERIAL,
    person_name       varchar(256) not null,
    person_birth_date date         not null,
    PRIMARY KEY (person_id),
    UNIQUE (person_name, person_birth_date)
);

CREATE TABLE IF NOT EXISTS movie
(
    movie_id          SERIAL,
    movie_title       varchar(256),
    release_year      smallint     not null,
    movie_country     varchar(124) not null,
    movie_genre       varchar(124) not null,
    movie_description text         not null,
    PRIMARY KEY (movie_id)
);

CREATE INDEX movie_title_idx ON movie (movie_title);
CREATE INDEX movie_release_year_idx ON movie (release_year);

CREATE TABLE IF NOT EXISTS movie_person
(
    id          SERIAL,
    movie_id    integer      not null,
    person_id   integer      not null,
    person_role varchar(124) not null,
    PRIMARY KEY (id),
    FOREIGN KEY (movie_id) REFERENCES movie (movie_id) ON DELETE RESTRICT,
    FOREIGN KEY (person_id) REFERENCES person (person_id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS review
(
    review_id   SERIAL,
    movie_id    integer      not null,
    user_id     integer      not null,
    review_text varchar(256) not null,
    rate        smallint     not null,
    PRIMARY KEY (review_id),
    UNIQUE (movie_id, user_id),
    FOREIGN KEY (movie_id) REFERENCES movie (movie_id) ON DELETE RESTRICT,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE RESTRICT
);