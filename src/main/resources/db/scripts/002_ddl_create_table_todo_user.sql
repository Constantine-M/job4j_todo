CREATE TABLE todo_user (
        id SERIAL PRIMARY KEY,
        name VARCHAR(32) NOT NULL ,
        login VARCHAR(32) UNIQUE NOT NULL,
        password VARCHAR(64) NOT NULL
);