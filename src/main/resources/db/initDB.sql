DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS feedbacks;
DROP TABLE IF EXISTS appointment_items;
DROP TABLE IF EXISTS master_services;
DROP TABLE IF EXISTS base_service_translation;
DROP TABLE IF EXISTS base_services;
DROP TABLE IF EXISTS service_category_translation;
DROP TABLE IF EXISTS service_categories;
DROP TABLE IF EXISTS appointments;
DROP TABLE IF EXISTS working_days;
DROP TABLE IF EXISTS user_level_translation;
DROP TABLE IF EXISTS user_level;
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

CREATE UNIQUE INDEX users_unique_email_idx ON users (email);
CREATE INDEX idx_users_name_surname ON users (name, surname);

CREATE TABLE roles
(
    id   SERIAL PRIMARY KEY,
    role VARCHAR NOT NULL
);

CREATE TABLE user_roles
(
    user_id INTEGER NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    role_id INTEGER NOT NULL REFERENCES roles (id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT user_roles_idx UNIQUE (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE user_level
(
    id     INTEGER PRIMARY KEY,
    level  VARCHAR NOT NULL,
    about  VARCHAR NOT NULL,
    active BOOLEAN NOT NULL
);

CREATE TABLE user_level_translation
(
    id            SERIAL PRIMARY KEY,
    user_level_id INTEGER    NOT NULL REFERENCES user_level (id) ON DELETE CASCADE,
    language_code VARCHAR(2) NOT NULL,
    about         VARCHAR    NOT NULL
);

CREATE TABLE service_categories
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL
);

CREATE TABLE service_category_translation
(
    service_category_id INTEGER    NOT NULL REFERENCES service_categories (id) ON DELETE CASCADE,
    language_code       VARCHAR(2) NOT NULL,
    translated_name     VARCHAR    NOT NULL,
    PRIMARY KEY (service_category_id, language_code)
);

CREATE TABLE base_services
(
    id          SERIAL PRIMARY KEY,
    category_id INTEGER NOT NULL REFERENCES service_categories (id),
    service     VARCHAR NOT NULL,
    price       INTEGER NOT NULL
);

CREATE TABLE base_service_translation
(
    id                 SERIAL PRIMARY KEY,
    base_service_id    INTEGER    NOT NULL REFERENCES base_services (id) ON DELETE CASCADE,
    language_code      VARCHAR(2) NOT NULL,
    translated_service VARCHAR    NOT NULL
);

CREATE TABLE master_services
(
    id              SERIAL PRIMARY KEY,
    master_id       INTEGER NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    base_service_id INTEGER NOT NULL REFERENCES base_services (id),
    continuance     INTEGER NOT NULL
);

CREATE TABLE appointments
(
    id             SERIAL PRIMARY KEY,
    master_id      INTEGER           NOT NULL REFERENCES users (id),
    client_id      INTEGER           NOT NULL REFERENCES users (id),
    continuance    INTEGER           NOT NULL,
    date           TIMESTAMP         NOT NULL,
    price          INTEGER           NOT NULL,
    discount       INTEGER DEFAULT 1 NOT NULL,
    status         VARCHAR           NOT NULL,
    payment_status VARCHAR           NOT NULL
);

CREATE TABLE appointment_items
(
    id                SERIAL PRIMARY KEY,
    appointment_id    INTEGER NOT NULL REFERENCES appointments (id) ON DELETE CASCADE,
    master_service_id INTEGER NOT NULL REFERENCES master_services (id)
);

CREATE TABLE feedbacks
(
    id             SERIAL PRIMARY KEY,
    appointment_id INTEGER   NOT NULL REFERENCES appointments (id) ON DELETE CASCADE,
    mark           INTEGER   NOT NULL,
    comment        VARCHAR   NOT NULL,
    date           TIMESTAMP NOT NULL
);

CREATE UNIQUE INDEX mark_unique_appointment_idx ON feedbacks (appointment_id);

CREATE TABLE consultations
(
    id      SERIAL PRIMARY KEY,
    name    VARCHAR NOT NULL,
    number  VARCHAR NOT NULL,
    date    TIMESTAMP DEFAULT now(),
    is_read BOOLEAN   DEFAULT FALSE
);

CREATE TABLE working_days
(
    id         SERIAL PRIMARY KEY,
    user_id    INTEGER NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    date       DATE    NOT NULL,
    time_start TIME    NOT NULL,
    time_end   TIME    NOT NULL
);

CREATE INDEX idx_master_services_master_id ON master_services (master_id);
CREATE INDEX idx_working_days_user_id ON working_days (user_id);
CREATE INDEX idx_appointments_master_id ON appointments (master_id);
CREATE INDEX idx_feedbacks_appointment_id ON feedbacks (appointment_id);
CREATE INDEX idx_appointment_items_appointment_id ON appointment_items (appointment_id);

CREATE TABLE invitations
(
    id    SERIAL PRIMARY KEY,
    email VARCHAR   NOT NULL UNIQUE,
    role  VARCHAR   NOT NULL,
    key   VARCHAR   NOT NULL,
    date  TIMESTAMP NOT NULL DEFAULT now()
);