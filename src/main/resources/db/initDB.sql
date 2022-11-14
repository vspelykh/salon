DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS marks;
DROP TABLE IF EXISTS orderings;
DROP TABLE IF EXISTS services;
DROP TABLE IF EXISTS appointments;
DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    id       SERIAL PRIMARY KEY,
    name     VARCHAR            NOT NULL,
    surname  VARCHAR            NOT NULL,
    email    VARCHAR(50) UNIQUE NOT NULL,
    number   VARCHAR(15)        NOT NULL,
    password VARCHAR(500)       NOT NULL
);

CREATE UNIQUE INDEX users_unique_email_idx ON users (number);

CREATE TABLE user_roles
(
    user_id INTEGER NOT NULL,
    role    VARCHAR,
    CONSTRAINT user_roles_idx UNIQUE (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE services
(
    id          SERIAL PRIMARY KEY,
    master_id   INTEGER NOT NULL,
    service     VARCHAR NOT NULL,
    continuance INTEGER NOT NULL,
    FOREIGN KEY (master_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE appointments
(
    id          SERIAL PRIMARY KEY,
    master_id   INTEGER   NOT NULL,
    client_id   INTEGER   NOT NULL,
    continuance INTEGER   NOT NULL,
    date        TIMESTAMP NOT NULL,
    price       INTEGER   NOT NULL,
    discount    INTEGER   NOT NULL,
    FOREIGN KEY (master_id) REFERENCES users (id),
    FOREIGN KEY (client_id) REFERENCES users (id)
);

CREATE TABLE orderings
(
    id             SERIAL PRIMARY KEY,
    service_id     INTEGER NOT NULL,
    appointment_id INTEGER NOT NULL,
    FOREIGN KEY (service_id) REFERENCES services (id),
    FOREIGN KEY (appointment_id) REFERENCES appointments (id)
);

CREATE TABLE marks
(
    id             SERIAL PRIMARY KEY,
    appointment_id INTEGER NOT NULL,
    mark           INTEGER NOT NULL,
    comment        VARCHAR NOT NULL,
    FOREIGN KEY (appointment_id) REFERENCES appointments (id)
);