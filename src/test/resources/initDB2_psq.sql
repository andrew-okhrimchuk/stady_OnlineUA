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
       (5, 'Max Payne', 1),
       (6, 'Max Payne-2', 1),
       (7, 'Max Payne-3', 1),
       (8, 'Max Payne-4', 1),
       (9, 'Max Payne-5', 1),
       (10, 'Max Payne-6', 1);

INSERT INTO DOCTOR (id)
VALUES (7),
       (8);

INSERT INTO PATIENT (id, birthDate, doctor_id)
VALUES (1, TO_DATE('17/11/2018', 'DD/MM/YYYY'), 7),
       (2, TO_DATE('07/02/2000', 'DD/MM/YYYY'), 7),
       (3, TO_DATE('08/02/2001', 'DD/MM/YYYY'), 7),
       (4, TO_DATE('09/02/2002', 'DD/MM/YYYY'), 8),
       (5, TO_DATE('07/03/2003', 'DD/MM/YYYY'),8),
       (6, TO_DATE('07/04/2004', 'DD/MM/YYYY'), 8);



INSERT INTO Speciality (user_id, speciality)
VALUES (7, 'LORE'),
       (8, 'GYNECOLOGIST');


INSERT INTO USER_ROLE (user_id, authorities)
VALUES (1, 'PATIENT'),
       (2, 'PATIENT'),
       (7, 'DOCTOR'),
       (8, 'DOCTOR');