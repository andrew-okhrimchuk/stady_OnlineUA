DROP TABLE IF EXISTS USERS CASCADE;
DROP TABLE IF EXISTS PATIENT CASCADE;
DROP TABLE IF EXISTS USER_ROLE CASCADE;


CREATE TABLE IF NOT EXISTS USERS
(
    id   SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    accountNonExpired boolean NOT NULL default true,
    accountNonLocked boolean NOT NULL default true,
    credentialsNonExpired boolean NOT NULL default true,
    enabled boolean NOT NULL default true
);
CREATE UNIQUE INDEX USERS_UNIQUE_NAME ON USERS (username);

CREATE TABLE IF NOT EXISTS PATIENT
(
    id   SERIAL PRIMARY KEY references USERS,
    iscurrentpatient boolean default false,
    birthDate date not null
);
CREATE INDEX birthDate_index ON PATIENT (birthDate);


CREATE TABLE IF NOT EXISTS USER_ROLE
(
    user_id INTEGER,
    FOREIGN KEY (user_id) REFERENCES USERS (id) ON UPDATE CASCADE ON DELETE CASCADE
);

INSERT INTO USERS (id, username, password)
VALUES (1, 'Jon Dou', '$2a$10$ImeH8oWiZpV/cBDXL14ILO8QLWL5Qf1qDmrey8lC1UfMgL9MFv06K'),
 (2, 'Amanta Smit', 1);

INSERT INTO PATIENT (id, birthDate)
VALUES (1, TO_DATE( '17/11/2018', 'DD/MM/YYYY' )),
       (2,  TO_DATE( '07/02/2000', 'DD/MM/YYYY' ));

