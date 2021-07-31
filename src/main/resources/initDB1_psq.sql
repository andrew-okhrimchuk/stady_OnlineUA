DROP TABLE IF EXISTS USERS CASCADE;
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

CREATE TABLE IF NOT EXISTS USER_ROLE
(
    user_id INTEGER,
    FOREIGN KEY (user_id) REFERENCES USERS (id) ON UPDATE CASCADE ON DELETE CASCADE
);

INSERT INTO USERS (username, password)
VALUES ('admin', '$2a$10$bBgceO1fsM7oXkTiyOcSLOWwfVpq6WB6L7ZeEpjuFzU9Aob');



