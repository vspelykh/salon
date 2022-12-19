DROP TABLE IF EXISTS user_level;
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS marks;
DROP TABLE IF EXISTS orderings;
DROP TABLE IF EXISTS services;
DROP TABLE IF EXISTS base_services;
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

CREATE TABLE user_level
(
    user_id INTEGER NOT NULL,
    level   VARCHAR NOT NULL,
    active  BOOLEAN NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE user_roles
(
    user_id INTEGER NOT NULL,
    role    VARCHAR NOT NULL,
    CONSTRAINT user_roles_idx UNIQUE (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE base_services
(
    id      SERIAL PRIMARY KEY,
    service VARCHAR NOT NULL,
    price   INTEGER NOT NULL
);

CREATE TABLE services
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
    id          SERIAL PRIMARY KEY,
    master_id   INTEGER             NOT NULL,
    client_id   INTEGER             NOT NULL,
    continuance INTEGER             NOT NULL,
    date        TIMESTAMP           NOT NULL,
    price       INTEGER             NOT NULL,
    discount    INTEGER DEFAULT '1' NOT NULL,
    FOREIGN KEY (master_id) REFERENCES users (id),
    FOREIGN KEY (client_id) REFERENCES users (id)
);

CREATE TABLE orderings
(
    id             SERIAL PRIMARY KEY,
    appointment_id INTEGER NOT NULL,
    service_id     INTEGER NOT NULL,
    FOREIGN KEY (service_id) REFERENCES services (id),
    FOREIGN KEY (appointment_id) REFERENCES appointments (id) ON DELETE CASCADE
);

--POPULATION DB:

CREATE TABLE marks
(
    id             SERIAL PRIMARY KEY,
    appointment_id INTEGER   NOT NULL,
    mark           INTEGER   NOT NULL,
    comment        VARCHAR   NOT NULL,
    date           TIMESTAMP NOT NULL,
    FOREIGN KEY (appointment_id) REFERENCES appointments (id) ON DELETE CASCADE
);

INSERT INTO users (name, surname, email, number, password)
VALUES ('Marina', 'Alkova', 'alkova@gmail.com', '+380661239900', 'lm7vuxj9PYktix+xnLhQarMqbSws3die'),
       ('Anastasia', 'Alkova', 'nastya22@gmail.com', '+380971239050', 'lm7vuxj9PYktix+xnLhQarMqbSws3die'),
       ('Alina', 'Ivanova', 'ivanovaa@gmail.com', '+380504561132', 'lm7vuxj9PYktix+xnLhQarMqbSws3die');

INSERT INTO user_roles
VALUES (1, 'ADMINISTRATOR'),
       (1, 'CLIENT'),
       (2, 'HAIRDRESSER'),
       (3, 'CLIENT');

INSERT INTO base_services (service, price)
VALUES ('men''s haircut 1st group', 180),
       ('men''s haircut 2nd group', 200),
       ('men''s haircut 3rd group', 220),
       ('women''s haircut 1st group', 250),
       ('women''s haircut 2nd group', 300),
       ('women''s haircut 3rd group', 350),
       ('head washing', 25),
       ('coloring', 400),
       ('highlighting', 120),
       ('alignment (straightening)', 60),
       ('styling', 50),
       ('lamination', 100),
       ('wave', 50),
       ('hair treatment', 100);

INSERT INTO user_level (user_id, level, active)
VALUES (2, 'TOP', true);

INSERT INTO services (master_id, base_service_id, continuance)
VALUES (2, 1, 20),
       (2, 2, 25),
       (2, 3, 30),
       (2, 4, 30),
       (2, 5, 35),
       (2, 6, 40),
       (2, 7, 5),
       (2, 8, 120),
       (2, 9, 30),
       (2, 10, 30),
       (2, 11, 30),
       (2, 12, 20),
       (2, 13, 10),
       (2, 14, 60);

INSERT INTO appointments (master_id, client_id, continuance, date, price, discount)
VALUES (2, 3, 180, '13.11.2022 16:00:00', '650', '-1');

INSERT INTO orderings (appointment_id, service_id)
VALUES (1, 4),
       (1, 8);
INSERT INTO marks (appointment_id, mark, comment, date)
VALUES (1, 5, 'Super, Awesome coloring', '14.11.2022 18:38:00')