--Create table for order item

CREATE TABLE order_item (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    user_id bigint(20) NOT NULL,
    product_id bigint(20) NOT NULL,
    product_size varchar(20) NOT NULL,
    product_color varchar(20) NOT NULL,
    quantity bigint(20) NOT NULL,
    order_state varchar(20) NOT NULL,
    created_by varchar(30) NOT NULL,
    created_date timestamp NOT NULL,
    last_modified_by varchar(30) DEFAULT NULL,
    last_modified_date timestamp NULL DEFAULT NULL,
    PRIMARY KEY (id),
    KEY FK_ORDER_ITEM_USER (user_id),
    KEY FK_ORDER_ITEM_PRODUCT (product_id),
    CONSTRAINT FK_ORDER_ITEM_USER FOREIGN KEY (user_id) REFERENCES user (id),
    CONSTRAINT FK_ORDER_ITEM_PRODUCT FOREIGN KEY (product_id) REFERENCES product (id)
);