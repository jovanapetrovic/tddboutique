-- Set up a few initial users
-- Password is always "123456"

-- Create test user
INSERT INTO user (id, first_name, last_name, email, username, password, created_by, created_date) VALUES
(2, 'firstname', 'lastname', 'testuser@test.com', 'testuser', '$2a$10$DI9yT93ik2gCJcJh1AH3PexczQWNO7nvVDndSMl/yRUzdKHvGo366', 'system', now());

INSERT INTO user_authority (user_id, authority_name) VALUES (2, 'ROLE_USER');
