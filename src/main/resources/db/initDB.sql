DROP TABLE IF EXISTS user_level;
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS feedbacks;
DROP TABLE IF EXISTS appointment_items;
DROP TABLE IF EXISTS master_services;
DROP TABLE IF EXISTS base_services;
DROP TABLE IF EXISTS service_categories;
DROP TABLE IF EXISTS appointments;
DROP TABLE IF EXISTS working_days;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS consultations;
DROP TABLE IF EXISTS invitations;

CREATE TABLE users
(
    id       SERIAL PRIMARY KEY,
    name     VARCHAR            NOT NULL,
    surname  VARCHAR            NOT NULL,
    email    VARCHAR(50) UNIQUE NOT NULL,
    number   VARCHAR(15)        NOT NULL,
    birthday DATE               NOT NULL,
    password VARCHAR(500)       NOT NULL
);

CREATE UNIQUE INDEX users_unique_email_idx ON users (number);

CREATE TABLE user_level
(
    id       INTEGER NOT NULL,
    level    VARCHAR NOT NULL,
    about    VARCHAR NOT NULL,
    about_ua VARCHAR NOT NULL,
    active   BOOLEAN NOT NULL,
    CONSTRAINT user_level_idx UNIQUE (id),
    FOREIGN KEY (id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE user_roles
(
    user_id INTEGER NOT NULL,
    role    VARCHAR NOT NULL,
    CONSTRAINT user_roles_idx UNIQUE (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE service_categories
(
    id      SERIAL PRIMARY KEY,
    name    VARCHAR NOT NULL,
    name_ua VARCHAR NOT NULL
);

CREATE TABLE base_services
(
    id          SERIAL PRIMARY KEY,
    category_id INTEGER NOT NULL,
    service     VARCHAR NOT NULL,
    service_ua  VARCHAR NOT NULL,
    price       INTEGER NOT NULL,
    FOREIGN KEY (category_id) REFERENCES service_categories (id)
);

CREATE TABLE master_services
(
    id              SERIAL PRIMARY KEY,
    master_id       INTEGER NOT NULL,
    base_service_id INTEGER NOT NULL,
    continuance     INTEGER NOT NULL,
    FOREIGN KEY (master_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (base_service_id) REFERENCES base_services (id)
);

CREATE TABLE appointments
(
    id             SERIAL PRIMARY KEY,
    master_id      INTEGER             NOT NULL,
    client_id      INTEGER             NOT NULL,
    continuance    INTEGER             NOT NULL,
    date           TIMESTAMP           NOT NULL,
    price          INTEGER             NOT NULL,
    discount       INTEGER DEFAULT '1' NOT NULL,
    status         VARCHAR             NOT NULL,
    payment_status VARCHAR             NOT NULL,
    FOREIGN KEY (master_id) REFERENCES users (id),
    FOREIGN KEY (client_id) REFERENCES users (id)
);

CREATE TABLE appointment_items
(
    id             SERIAL PRIMARY KEY,
    appointment_id INTEGER NOT NULL,
    service_id     INTEGER NOT NULL,
    FOREIGN KEY (service_id) REFERENCES master_services (id),
    FOREIGN KEY (appointment_id) REFERENCES appointments (id) ON DELETE CASCADE
);


CREATE TABLE feedbacks
(
    id             SERIAL PRIMARY KEY,
    appointment_id INTEGER   NOT NULL,
    mark           INTEGER   NOT NULL,
    comment        VARCHAR   NOT NULL,
    date           TIMESTAMP NOT NULL,
    FOREIGN KEY (appointment_id) REFERENCES appointments (id) ON DELETE CASCADE
);

CREATE UNIQUE INDEX mark_unique_appointment_idx ON feedbacks (appointment_id);

CREATE TABLE consultations
(
    id     SERIAL PRIMARY KEY,
    name   VARCHAR NOT NULL,
    number VARCHAR NOT NULL,
    date   TIMESTAMP DEFAULT now()
);

CREATE TABLE working_days
(
    id         SERIAL PRIMARY KEY,
    user_id    INTEGER NOT NULL,
    date       DATE    NOT NULL,
    time_start TIME    NOT NULL,
    time_end   TIME    NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT user_working_day UNIQUE (user_id, date)
);

CREATE TABLE invitations
(
    id    SERIAL PRIMARY KEY,
    email VARCHAR   NOT NULL UNIQUE,
    role  VARCHAR   NOT NULL,
    key   VARCHAR   NOT NULL,
    date  TIMESTAMP NOT NULL DEFAULT now()
);