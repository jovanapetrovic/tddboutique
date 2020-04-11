--Create table for image

CREATE TABLE image (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    product_id bigint(20) NOT NULL,
    name varchar(255) DEFAULT NULL,
    type varchar(30) NOT NULL,
    size bigint(20) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY UK_PRODUCT_NAME (product_id, name),
    KEY FK_IMAGE_PRODUCT (product_id),
    CONSTRAINT FK_IMAGE_PRODUCT FOREIGN KEY (product_id) REFERENCES product (id)
);