-- Create tables for product

CREATE TABLE product (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    name varchar(30) NOT NULL,
    material varchar(100) NOT NULL,
    description varchar(100) DEFAULT NULL,
    price decimal DEFAULT NULL,
    deleted bit(1) DEFAULT FALSE,
    created_by varchar(30) NOT NULL,
    created_date timestamp NOT NULL,
    last_modified_by varchar(30) DEFAULT NULL,
    last_modified_date timestamp NULL DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY name (name),
    UNIQUE KEY idx_product_name (name)
);

CREATE TABLE product_size (
    product_id bigint(20) NOT NULL,
    size varchar(30) NOT NULL,
    KEY FK_PRODUCT_SIZE_ID (product_id),
    CONSTRAINT FK_PRODUCT_SIZE_ID FOREIGN KEY (product_id) REFERENCES product (id)
);

CREATE TABLE product_color (
    product_id bigint(20) NOT NULL,
    color varchar(30) NOT NULL,
    KEY FK_PRODUCT_COLOR_ID (product_id),
    CONSTRAINT FK_PRODUCT_COLOR_ID FOREIGN KEY (product_id) REFERENCES product (id)
);

CREATE TABLE stock (
    product_id bigint(20) NOT NULL,
    units bigint(20) NOT NULL DEFAULT 0,
    created_by varchar(30) NOT NULL,
    created_date timestamp NOT NULL,
    last_modified_by varchar(30) DEFAULT NULL,
    last_modified_date timestamp NULL DEFAULT NULL,
    PRIMARY KEY (product_id)
);
