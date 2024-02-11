ALTER TABLE tasks ADD COLUMN user_id INT;
ALTER TABLE tasks ADD CONSTRAINT user_fk FOREIGN KEY (user_id) REFERENCES todo_user (id);