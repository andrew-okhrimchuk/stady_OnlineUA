DROP TABLE IF EXISTS USERS CASCADE;
DROP TABLE IF EXISTS PATIENT CASCADE;
DROP TABLE IF EXISTS DOCTOR CASCADE;
DROP TABLE IF EXISTS USER_ROLE CASCADE;
DROP TABLE IF EXISTS Speciality CASCADE;
DROP TABLE IF EXISTS Nurse CASCADE;


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
    id            SERIAL PRIMARY KEY references USERS ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Speciality
(
    user_id INTEGER UNIQUE ,
    FOREIGN KEY (user_id) REFERENCES DOCTOR (id) ON UPDATE CASCADE ON DELETE CASCADE,
    speciality              VARCHAR(255) NOT NULL
);


CREATE TABLE IF NOT EXISTS PATIENT
(
    id               SERIAL PRIMARY KEY references USERS ON UPDATE CASCADE ON DELETE CASCADE,
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

CREATE TABLE IF NOT EXISTS Nurse
(
    id            SERIAL PRIMARY KEY references USERS ON UPDATE CASCADE ON DELETE CASCADE
);


INSERT INTO USERS (id, username, password, accountNonExpired, accountNonLocked, credentialsNonExpired, enabled)
VALUES (1, 'Jon Dou', '$2a$10$iMPOwJjVRZR4IeWES4.vgO5eFA/7kTjrSFPhE701T9jlULoXq6EIC', true,true,true,true),
       (2, 'Amanta Smit', '$2a$10$iMPOwJjVRZR4IeWES4.vgO5eFA/7kTjrSFPhE701T9jlULoXq6EIC',true,true,true,true),
       (3, 'May Smit', '$2a$10$iMPOwJjVRZR4IeWES4.vgO5eFA/7kTjrSFPhE701T9jlULoXq6EIC',true,true,true,true),
       (4, 'Andrey Okhrim', '$2a$10$iMPOwJjVRZR4IeWES4.vgO5eFA/7kTjrSFPhE701T9jlULoXq6EIC',true,true,true,true),
       (5, 'Max Payne', '$2a$10$iMPOwJjVRZR4IeWES4.vgO5eFA/7kTjrSFPhE701T9jlULoXq6EIC',true,true,true,true),
       (6, 'Max Payne-2', '$2a$10$iMPOwJjVRZR4IeWES4.vgO5eFA/7kTjrSFPhE701T9jlULoXq6EIC',true,true,true,true),
       (7, 'Max Payne-3', '$2a$10$iMPOwJjVRZR4IeWES4.vgO5eFA/7kTjrSFPhE701T9jlULoXq6EIC',true,true,true,true),
       (8, 'Max Payne-4', '$2a$10$iMPOwJjVRZR4IeWES4.vgO5eFA/7kTjrSFPhE701T9jlULoXq6EIC',true,true,true,true),
       (9, 'Max Payne-5', '$2a$10$iMPOwJjVRZR4IeWES4.vgO5eFA/7kTjrSFPhE701T9jlULoXq6EIC',true,true,true,true),
       (10, 'Max Payne-6', '$2a$10$iMPOwJjVRZR4IeWES4.vgO5eFA/7kTjrSFPhE701T9jlULoXq6EIC',true,true,true,true),
       (11, 'Nurse Night', '$2a$10$iMPOwJjVRZR4IeWES4.vgO5eFA/7kTjrSFPhE701T9jlULoXq6EIC',true,true,true,true),
       (12, 'Nurse Day', '$2a$10$iMPOwJjVRZR4IeWES4.vgO5eFA/7kTjrSFPhE701T9jlULoXq6EIC',true,true,true,true),
       (13, 'Nurse Student', '$2a$10$iMPOwJjVRZR4IeWES4.vgO5eFA/7kTjrSFPhE701T9jlULoXq6EIC',true,true,true,true),
       (14, 'Nurse Profi', '$2a$10$iMPOwJjVRZR4IeWES4.vgO5eFA/7kTjrSFPhE701T9jlULoXq6EIC',true,true,true,true);

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

INSERT INTO Nurse (id)
VALUES (11),
       (12),
       (13),
       (14);