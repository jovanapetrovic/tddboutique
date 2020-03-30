INSERT INTO user (id, first_name, last_name, email, username, password, created_by, created_date) VALUES
(11, 'firstname3', 'lastname3', 'testuser3@test.com', 'testuser3', '$2a$10$DI9yT93ik2gCJcJh1AH3PexczQWNO7nvVDndSMl/yRUzdKHvGo366', 'system', now());

INSERT INTO user_authority (user_id, authority_name) VALUES (11, 'ROLE_USER');