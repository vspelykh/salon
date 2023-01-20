DROP TABLE IF EXISTS user_level;
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS marks;
DROP TABLE IF EXISTS orderings;
DROP TABLE IF EXISTS services;
DROP TABLE IF EXISTS base_services;
DROP TABLE IF EXISTS appointments;
DROP TABLE IF EXISTS working_days;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS consultations;

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
    id     INTEGER NOT NULL,
    level  VARCHAR NOT NULL,
    about  VARCHAR NOT NULL,
    active BOOLEAN NOT NULL,
    FOREIGN KEY (id) REFERENCES users (id) ON DELETE CASCADE
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


CREATE TABLE marks
(
    id             SERIAL PRIMARY KEY,
    appointment_id INTEGER   NOT NULL,
    mark           INTEGER   NOT NULL,
    comment        VARCHAR   NOT NULL,
    date           TIMESTAMP NOT NULL,
    FOREIGN KEY (appointment_id) REFERENCES appointments (id) ON DELETE CASCADE
);

CREATE TABLE consultations
(
    id     SERIAL PRIMARY KEY,
    name   VARCHAR NOT NULL,
    number VARCHAR NOT NULL,
    date   TIMESTAMP DEFAULT now()
);

CREATE TABLE working_days
(
    id      SERIAL PRIMARY KEY,
    user_id INTEGER   NOT NULL,
    date    DATE NOT NULL,
    time_start TIME NOT NULL,
    time_end TIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT user_working_day UNIQUE(user_id, date)
);

--POPULATION DB:

INSERT INTO users (name, surname, email, number, password)
VALUES ('Marina', 'Alkova', 'admin@gmail.com', '+380661239900', 'lm7vuxj9PYktix+xnLhQarMqbSws3die'),
       ('Anastasia', 'Semenova', 'master@gmail.com', '+380971239050', 'lm7vuxj9PYktix+xnLhQarMqbSws3die'),
       ('Alina', 'Ivanova', 'ivanovaa@gmail.com', '+380504561132', 'lm7vuxj9PYktix+xnLhQarMqbSws3die'),
       ('Galina', 'Shevchenko', 'random1@gmail.com', '+380970239050', 'lm7vuxj9PYktix+xnLhQarMqbSws3die'),
       ('Valentina', 'Glushko', 'random12@gmail.com', '+380972239050', 'lm7vuxj9PYktix+xnLhQarMqbSws3die'),
       ('Tatiana', 'Dubina', 'random13@gmail.com', '+380971235050', 'lm7vuxj9PYktix+xnLhQarMqbSws3die'),
       ('Yana', 'Lobotenko', 'random14@gmail.com', '+380971238050', 'lm7vuxj9PYktix+xnLhQarMqbSws3die'),
       ('Yulia', 'Smith', 'random15@gmail.com', '+380971239060', 'lm7vuxj9PYktix+xnLhQarMqbSws3die'),
       ('Karina', 'Mogrunova', 'random111@gmail.com', '+380971239150', 'lm7vuxj9PYktix+xnLhQarMqbSws3die'),
       ('Tatiana', 'Kosinska', 'random1s@gmail.com', '+380971239020', 'lm7vuxj9PYktix+xnLhQarMqbSws3die'),
       ('Yana', 'Demchenko', 'random1ad@gmail.com', '+380971239053', 'lm7vuxj9PYktix+xnLhQarMqbSws3die'),
       ('Anastasia', 'Shulgat', 'random1sdas@gmail.com', '+380971439050', 'lm7vuxj9PYktix+xnLhQarMqbSws3die'),
       ('Yana', 'Pivovarov', 'random1sdaq2@gmail.com', '+380971236050', 'lm7vuxj9PYktix+xnLhQarMqbSws3die'),
       ('Anastasia', 'Kirichenko', 'random1ww@gmail.com', '+380977239050', 'lm7vuxj9PYktix+xnLhQarMqbSws3die'),
       ('Karina', 'Random', 'emails12@gmail.com', '+380971239080', 'lm7vuxj9PYktix+xnLhQarMqbSws3die'),
       ('Anastasia', 'Liach', 'emails12z@gmail.com', '+380971279050', 'lm7vuxj9PYktix+xnLhQarMqbSws3die'),
       ('Tatiana', 'Kirsanova', 'emails12qmo@gmail.com', '+380571239050', 'lm7vuxj9PYktix+xnLhQarMqbSws3die'),
       ('Yulia', 'Levchenko', 'emails127@gmail.com', '+380971249050', 'lm7vuxj9PYktix+xnLhQarMqbSws3die'),
       ('Anastasia', 'Romanova', 'emails12fr@gmail.com', '+380371239050', 'lm7vuxj9PYktix+xnLhQarMqbSws3die'),
       ('Karina', 'Karpenko', 'bestis@gmail.com', '+380971239029', 'lm7vuxj9PYktix+xnLhQarMqbSws3die'),
       ('Anastasia', 'Semenova', 'bestis1@gmail.com', '+380971139050', 'lm7vuxj9PYktix+xnLhQarMqbSws3die'),
       ('Yulia', 'Lavamaris', 'bestis23@gmail.com', '+380971230050', 'lm7vuxj9PYktix+xnLhQarMqbSws3die');

INSERT INTO user_roles
VALUES (1, 'ADMINISTRATOR'),
       (1, 'CLIENT'),
       (2, 'HAIRDRESSER'),
       (3, 'CLIENT'),
       (4, 'HAIRDRESSER'),
       (5, 'HAIRDRESSER'),
       (6, 'HAIRDRESSER'),
       (7, 'HAIRDRESSER'),
       (8, 'HAIRDRESSER'),
       (9, 'HAIRDRESSER'),
       (10, 'HAIRDRESSER'),
       (11, 'HAIRDRESSER'),
       (12, 'CLIENT'),
       (13, 'CLIENT'),
       (14, 'CLIENT'),
       (15, 'CLIENT'),
       (16, 'CLIENT'),
       (17, 'CLIENT'),
       (18, 'CLIENT'),
       (19, 'CLIENT'),
       (20, 'CLIENT'),
       (21, 'CLIENT'),
       (5, 'CLIENT');

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

INSERT INTO user_level (id, level, active, about)
VALUES (2, 'TOP', true, 'hairdresser-modeler, colorist, bio-perm specialist'),
       (4, 'YOUNG', true,
        'hairdresser-fashion designer, designer of hairstyles, men''s and children''s haircuts, eyebrow artist'),
       (5, 'PRO', true,
        'hairdresser-fashion designer, designer of hairstyles, men''s and children''s haircuts, eyebrow artist'),
       (6, 'TOP', true,
        'hairdresser-fashion designer, designer of hairstyles, men''s and children''s haircuts, eyebrow artist'),
       (7, 'YOUNG', true, 'hairdresser-modeler, colorist, bio-perm specialist'),
       (8, 'TOP', true,
        'hairdresser-fashion designer, designer of hairstyles, men''s and children''s haircuts, eyebrow artist'),
       (9, 'TOP', true,
        'hairdresser-fashion designer, designer of hairstyles, men''s and children''s haircuts, eyebrow artist'),
       (10, 'YOUNG', true, 'hairdresser-modeler, colorist, bio-perm specialist'),
       (11, 'PRO', true, 'hairdresser-modeler, colorist');

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
       (2, 14, 60),
       (4, 1, 20),
       (4, 2, 25),
       (4, 3, 30),
       (4, 4, 30),
       (4, 5, 35),
       (4, 6, 40),
       (4, 7, 5),
       (4, 8, 120),
       (4, 9, 30),
       (4, 10, 30),
       (4, 11, 30),
       (4, 12, 20),
       (4, 13, 10),
       (4, 14, 60);

INSERT INTO appointments (master_id, client_id, continuance, date, price, discount)
VALUES (2, 3, 180, '13.11.2022 16:00:00', '650', '-1');

INSERT INTO orderings (appointment_id, service_id)
VALUES (1, 4),
       (1, 8);
INSERT INTO marks (appointment_id, mark, comment, date)
VALUES (1, 5, 'Super, Awesome coloring', '14.11.2022 18:38:00');

INSERT INTO working_days (user_id, date, time_start, time_end)
VALUES (2, '20.01.2023', '8:00', '20:00'),
       (2, '13.01.2023','8:00', '20:00'),
       (2, '21.01.2023', '8:00', '20:00'),
       (2, '24.01.2023', '8:00', '20:00'),
       (2, '25.01.2023', '8:00', '20:00'),
       (2, '28.01.2023', '8:00', '20:00'),
       (2, '29.01.2023', '8:00', '20:00'),
       (2, '30.01.2023', '8:00', '20:00'),
       (2, '31.01.2023', '8:00', '20:00'),
       (2, '2.02.2023', '8:00', '20:00'),
       (2, '3.02.2023', '8:00', '20:00'),
       (2, '5.02.2023', '8:00', '20:00'),
       (2, '6.02.2023', '8:00', '20:00'),
       (2, '9.02.2023', '8:00', '20:00'),
       (2, '10.02.2023', '8:00', '20:00'),
       (2, '12.02.2023', '8:00', '20:00');