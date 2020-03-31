-- Create tables for shipping address

CREATE TABLE shipping_address (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    user_id bigint(20) DEFAULT NULL,
    first_name varchar(30) DEFAULT NULL,
    last_name varchar(30) DEFAULT NULL,
    address varchar(30) DEFAULT NULL,
    zip_code bigint(20) DEFAULT NULL,
    city varchar(30) DEFAULT NULL,
    country varchar(30) DEFAULT NULL,
    phone_number varchar(30) DEFAULT NULL,
    created_by varchar(30) NOT NULL,
    created_date timestamp NOT NULL,
    last_modified_by varchar(30) DEFAULT NULL,
    last_modified_date timestamp NULL DEFAULT NULL,
    PRIMARY KEY (id),
    KEY FK_SHIPPING_ADDRESS_USER (user_id),
    CONSTRAINT FK_SHIPPING_ADDRESS_USER FOREIGN KEY (user_id) REFERENCES user (id)
);
