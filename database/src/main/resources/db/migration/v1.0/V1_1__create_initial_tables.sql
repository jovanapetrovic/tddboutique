-- Create tables for user and authorities

CREATE TABLE user (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  username varchar(50) NOT NULL,
  password varchar(60) DEFAULT NULL,
  created_by varchar(50) NOT NULL,
  created_date timestamp NOT NULL,
  reset_date timestamp NULL DEFAULT NULL,
  last_modified_by varchar(50) DEFAULT NULL,
  last_modified_date timestamp NULL DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY username (username),
  UNIQUE KEY idx_user_username (username)
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
