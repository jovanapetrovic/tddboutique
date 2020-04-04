INSERT INTO user (id, first_name, last_name, email, username, password, created_by, created_date) VALUES
(13, 'firstname4', 'lastname4', 'testuser4@test.com', 'testuser4', '$2a$10$DI9yT93ik2gCJcJh1AH3PexczQWNO7nvVDndSMl/yRUzdKHvGo366', 'system', now());

INSERT INTO user_authority (user_id, authority_name) VALUES (13, 'ROLE_USER');

INSERT INTO user (id, first_name, last_name, email, username, password, created_by, created_date) VALUES
(14, 'firstname5', 'lastname5', 'testuser5@test.com', 'testuser5', '$2a$10$DI9yT93ik2gCJcJh1AH3PexczQWNO7nvVDndSMl/yRUzdKHvGo366', 'system', now());

INSERT INTO user_authority (user_id, authority_name) VALUES (14, 'ROLE_USER');

INSERT INTO user (id, first_name, last_name, email, username, password, created_by, created_date) VALUES
(15, 'firstname6', 'lastname6', 'testuser6@test.com', 'testuser6', '$2a$10$DI9yT93ik2gCJcJh1AH3PexczQWNO7nvVDndSMl/yRUzdKHvGo366', 'system', now());

INSERT INTO user_authority (user_id, authority_name) VALUES (15, 'ROLE_USER');