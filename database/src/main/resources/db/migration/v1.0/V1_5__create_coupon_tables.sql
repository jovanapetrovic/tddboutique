--Create table for coupon

CREATE TABLE coupon (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    user_id bigint(20) NOT NULL,
    code varchar(12) DEFAULT NULL,
    value varchar(20) NOT NULL,
    status varchar(20) NOT NULL,
    expiry_date timestamp NOT NULL,
    created_by varchar(30) NOT NULL,
    created_date timestamp NOT NULL,
    last_modified_by varchar(30) DEFAULT NULL,
    last_modified_date timestamp NULL DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY UK_USER_COUPON_CODE (user_id, code),
    KEY FK_COUPON_USER (user_id),
    CONSTRAINT FK_COUPON_USER FOREIGN KEY (user_id) REFERENCES user (id)
);