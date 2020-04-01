-- Set up a few test users
-- Password is always "123456"

INSERT INTO user (id, first_name, last_name, email, username, password, created_by, created_date) VALUES
(10, 'firstname1', 'lastname1', 'testuser1@test.com', 'testuser1', '$2a$10$DI9yT93ik2gCJcJh1AH3PexczQWNO7nvVDndSMl/yRUzdKHvGo366', 'system', now());

INSERT INTO user_authority (user_id, authority_name) VALUES (10, 'ROLE_USER');

INSERT INTO user (id, first_name, last_name, email, username, password, created_by, created_date) VALUES
(11, 'firstname2', 'lastname2', 'testuser2@test.com', 'testuser2', '$2a$10$DI9yT93ik2gCJcJh1AH3PexczQWNO7nvVDndSMl/yRUzdKHvGo366', 'system', now());

INSERT INTO user_authority (user_id, authority_name) VALUES (11, 'ROLE_USER');

INSERT INTO user (id, first_name, last_name, email, username, password, created_by, created_date) VALUES
(12, 'firstname3', 'lastname3', 'testuser3@test.com', 'testuser3', '$2a$10$DI9yT93ik2gCJcJh1AH3PexczQWNO7nvVDndSMl/yRUzdKHvGo366', 'system', now());

INSERT INTO user_authority (user_id, authority_name) VALUES (12, 'ROLE_USER');
