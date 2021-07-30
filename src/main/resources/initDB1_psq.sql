DROP TABLE IF EXISTS USERS CASCADE;


CREATE TABLE IF NOT EXISTS USERS
(
    id   SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) ,
    accountNonExpired boolean NOT NULL default true,
    accountNonLocked boolean NOT NULL default true,
    credentialsNonExpired boolean NOT NULL default true,
    enabled boolean NOT NULL default true
);
CREATE UNIQUE INDEX COURSE_UNIQUE_NAME ON USERS (username);

CREATE TABLE IF NOT EXISTS USER_ROLE
(
    user_id SERIAL ,
    role VARCHAR(255) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES USERS (id) ON UPDATE CASCADE ON DELETE CASCADE

);

INSERT INTO USERS (username, password)
VALUES ('admin', '1');



