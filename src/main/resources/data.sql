INSERT INTO roles (name) VALUES ('ROLE_USER');
INSERT INTO roles (name) VALUES ('ROLE_MODERATOR');
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');

INSERT INTO users  (username, email, password) VALUES ('rijas', 'rijas@gmail.com', '$2a$12$CtlD6ULcZijWepmdcQIfrOqOtuZ0qS/tVWzAsvZXFFzG5LLbF4AaK');

INSERT INTO user_roles (role_id, user_id) VALUES (3,1);
