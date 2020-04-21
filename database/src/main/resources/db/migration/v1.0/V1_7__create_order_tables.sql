--Create table for order_details (order is reserved word in MySQL)

CREATE TABLE order_details (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    user_id bigint(20) NOT NULL,
    shipping_address_id bigint(20) NOT NULL,
    note varchar(500) DEFAULT NULL,
    payment_status varchar(20) NOT NULL,
    coupon_id bigint(20) DEFAULT NULL,
    total_price decimal(10,2) NOT NULL,
    price_with_discount decimal(10,2) DEFAULT NULL,
    link_to_payment_receipt varchar(500) DEFAULT NULL,
    created_by varchar(30) NOT NULL,
    created_date timestamp NOT NULL,
    last_modified_by varchar(30) DEFAULT NULL,
    last_modified_date timestamp NULL DEFAULT NULL,
    PRIMARY KEY (id),
    KEY FK_ORDER_USER (user_id),
    KEY FK_ORDER_SHIPPING_ADDRESS (shipping_address_id),
    KEY FK_ORDER_COUPON (coupon_id),
    CONSTRAINT FK_ORDER_USER FOREIGN KEY (user_id) REFERENCES user (id),
    CONSTRAINT FK_ORDER_SHIPPING_ADDRESS FOREIGN KEY (shipping_address_id) REFERENCES shipping_address (id)
);

--Update order_item table with order_id column

ALTER TABLE order_item
ADD COLUMN order_id bigint(20) DEFAULT NULL AFTER order_state;
