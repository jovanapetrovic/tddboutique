-- Create tables for user and authorities

CREATE TABLE user (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    first_name varchar(30) NOT NULL,
    last_name varchar(30) NOT NULL,
    email varchar(50) NOT NULL,
    username varchar(30) NOT NULL,
    password varchar(60) DEFAULT NULL,
    created_by varchar(30) NOT NULL,
    created_date timestamp NOT NULL,
    last_modified_by varchar(30) DEFAULT NULL,
    last_modified_date timestamp NULL DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY username (username),
    UNIQUE KEY idx_user_username (username),
    UNIQUE KEY email (email),
    UNIQUE KEY idx_user_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE authority (
    name varchar(50) NOT NULL,
    PRIMARY KEY (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE user_authority (
    user_id bigint(20) NOT NULL,
    authority_name varchar(50) NOT NULL,
    PRIMARY KEY (user_id, authority_name),
    KEY fk_authority_name (authority_name),
    CONSTRAINT fk_authority_name FOREIGN KEY (authority_name) REFERENCES authority (name),
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES user (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- Insert authorities constants

INSERT INTO authority (name) VALUES ('ROLE_ADMIN'), ('ROLE_USER');

-- Create admin user
INSERT INTO user (id, first_name, last_name, email, username, password, created_by, created_date) VALUES
(1, 'admin', 'admin', 'tddboutique@mailinator.com', 'admin', '$2a$10$DI9yT93ik2gCJcJh1AH3PexczQWNO7nvVDndSMl/yRUzdKHvGo366', 'system', now());

INSERT INTO user_authority (user_id, authority_name) VALUES (1, 'ROLE_ADMIN');

-- Create test user
INSERT INTO user (id, first_name, last_name, email, username, password, created_by, created_date) VALUES
(2, 'test', 'test', 'tddboutiquetest@mailinator.com', 'test', '$2a$10$DI9yT93ik2gCJcJh1AH3PexczQWNO7nvVDndSMl/yRUzdKHvGo366', 'system', now());

INSERT INTO user_authority (user_id, authority_name) VALUES (2, 'ROLE_USER');
