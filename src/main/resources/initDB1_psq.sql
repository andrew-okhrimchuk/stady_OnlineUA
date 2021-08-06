DROP TABLE IF EXISTS USERS CASCADE;
DROP TABLE IF EXISTS PATIENT CASCADE;
DROP TABLE IF EXISTS DOCTOR CASCADE;
DROP TABLE IF EXISTS USER_ROLE CASCADE;
DROP TABLE IF EXISTS Speciality CASCADE;


CREATE TABLE IF NOT EXISTS USERS
(
    id                    SERIAL PRIMARY KEY,
    username              VARCHAR(255) NOT NULL,
    password              VARCHAR(255) NOT NULL,
    accountNonExpired     boolean      NOT NULL default true,
    accountNonLocked      boolean      NOT NULL default true,
    credentialsNonExpired boolean      NOT NULL default true,
    enabled               boolean      NOT NULL default true
);
CREATE UNIQUE INDEX USERS_UNIQUE_NAME ON USERS (username);



CREATE TABLE IF NOT EXISTS DOCTOR
(
    id            SERIAL PRIMARY KEY references USERS
);

CREATE TABLE IF NOT EXISTS Speciality
(
    user_id INTEGER UNIQUE ,
    FOREIGN KEY (user_id) REFERENCES DOCTOR (id) ON UPDATE CASCADE ON DELETE CASCADE,
    speciality              VARCHAR(255) NOT NULL
);


CREATE TABLE IF NOT EXISTS PATIENT
(
    id               SERIAL PRIMARY KEY references USERS,
    iscurrentpatient boolean default false,
    birthDate        date not null,
    doctor_id        integer,
    FOREIGN KEY (doctor_id) REFERENCES DOCTOR (id)
);
CREATE INDEX birthDate_index ON PATIENT (birthDate);

CREATE TABLE IF NOT EXISTS USER_ROLE
(
    user_id INTEGER,
    FOREIGN KEY (user_id) REFERENCES USERS (id) ON UPDATE CASCADE ON DELETE CASCADE,
    authorities VARCHAR(255) NOT NULL
);


INSERT INTO USERS (id, username, password)
VALUES (1, 'Jon Dou', '$2a$10$ImeH8oWiZpV/cBDXL14ILO8QLWL5Qf1qDmrey8lC1UfMgL9MFv06K'),
       (2, 'Amanta Smit', 1),
       (3, 'May Smit', 1),
       (4, 'Andrey Okhrim', 1),
       (5, 'Max Pain', 1);



INSERT INTO PATIENT (id, birthDate)
VALUES (1, TO_DATE('17/11/2018', 'DD/MM/YYYY')),
       (2, TO_DATE('07/02/2000', 'DD/MM/YYYY'));

INSERT INTO DOCTOR (id)
VALUES (3),
       (4);

INSERT INTO Speciality (user_id, speciality)
VALUES (3, 'LORE'),
       (4, 'GYNECOLOGIST');


INSERT INTO USER_ROLE (user_id, authorities)
VALUES (1, 'PATIENT'),
       (2, 'PATIENT'),
       (3, 'DOCTOR'),
       (4, 'DOCTOR'),
       (5, 'ADMIN');