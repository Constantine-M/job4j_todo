CREATE TABLE tasks (
        id SERIAL PRIMARY KEY,
        created TIMESTAMP,
        title text,
        description TEXT,
        done BOOLEAN
);