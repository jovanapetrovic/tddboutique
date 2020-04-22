INSERT INTO shipping_address (id, user_id, first_name, last_name, address, zip_code, city, country, phone_number, created_by, created_date) VALUES
(10, 10, 'firstname1', 'lastname1', 'Test address 1', '18000', 'Nis', 'Serbia', '+38164123456', 'system', now());

INSERT INTO shipping_address (id, user_id, first_name, last_name, address, zip_code, city, country, phone_number, created_by, created_date) VALUES
(11, 11, 'firstname2', 'lastname2', 'Test address 2', '18000', 'Nis', 'Serbia', '+38164123456', 'system', now());

INSERT INTO shipping_address (id, user_id, first_name, last_name, address, zip_code, city, country, phone_number, created_by, created_date) VALUES
(12, 12, 'firstname3', 'lastname3', 'Test address 3', '18000', 'Nis', 'Serbia', '+38164123456', 'system', now());

------------------------------------------------------------------------------------------------------------------------

INSERT INTO coupon (id, user_id, code, value, status, expiry_date, created_by, created_date) VALUES
(10, 10, '1234aBaB56CD', 'COUPON_10', 'REDEEMED', now() + 5, 'system', now());

INSERT INTO coupon (id, user_id, code, value, status, expiry_date, created_by, created_date) VALUES
(11, 10, 'ASDF5678asdf', 'COUPON_20', 'ACTIVE', now() + 5, 'system', now());

INSERT INTO coupon (id, user_id, code, value, status, expiry_date, created_by, created_date) VALUES
(12, 11, 'qwerty042020', 'COUPON_10', 'ACTIVE', now() + 7, 'system', now());

------------------------------------------------------------------------------------------------------------------------

INSERT INTO product (id, name, material, description, price, created_by, created_date) VALUES
(10, 'Casual dress', 'viscose, cotton, polyester', 'A beautiful everyday dress', '20.00', 'system', now());

INSERT INTO product_color (product_id, color) VALUES
(10, 'BLACK'),
(10, 'RED'),
(10, 'BLUE'),
(10, 'PINK'),
(10, 'WHITE');

INSERT INTO product_size (product_id, size) VALUES
(10, 'S'),
(10, 'M'),
(10, 'L'),
(10, 'XL');

INSERT INTO stock (product_id, units, created_by, created_date) VALUES
(10, 100, 'system', now());

INSERT INTO image (id, product_id, name, type, size) VALUES
(1, 10, '10_black.png', 'PNG', '1234'),
(2, 10, '10_red.png', 'PNG', '1234');

------------------------------------------------------------------------------------------------------------------------

INSERT INTO product (id, name, material, description, price, created_by, created_date) VALUES
(11, 'Maxi summer dress', 'cotton, polyester', 'Amazing maxi summer dress', '39.99', 'system', now());

INSERT INTO product_color (product_id, color) VALUES
(11, 'WHITE'),
(11, 'PINK'),
(11, 'YELLOW'),
(11, 'BLUE'),
(11, 'COLORFUL');

INSERT INTO product_size (product_id, size) VALUES
(11, 'S'),
(11, 'M'),
(11, 'L'),
(11, 'XL');

INSERT INTO stock (product_id, units, created_by, created_date) VALUES
(11, 70, 'system', now());

INSERT INTO image (id, product_id, name, type, size) VALUES
(3, 11, '11_pink.png', 'PNG', 1234),
(4, 11, '11_colorful.png', 'PNG', 1234);

------------------------------------------------------------------------------------------------------------------------

INSERT INTO product (id, name, material, description, price, created_by, created_date) VALUES
(12, 'Evening dress', 'cotton, polyester', 'A beautiful evening dress', '45.00', 'system', now());

INSERT INTO product_color (product_id, color) VALUES
(12, 'BLACK'),
(12, 'BEIGE'),
(12, 'RED');

INSERT INTO product_size (product_id, size) VALUES
(12, 'S'),
(12, 'M');

INSERT INTO stock (product_id, units, created_by, created_date) VALUES
(12, 20, 'system', now());

INSERT INTO image (id, product_id, name, type, size) VALUES
(5, 12, '12_black.png', 'PNG', 1234),
(6, 12, '12_beige.png', 'PNG', 1234),
(7, 12, '12_red.png', 'PNG', 1234);

------------------------------------------------------------------------------------------------------------------------

INSERT INTO order_item (id, user_id, product_id, product_size, product_color, quantity, order_state, created_by, created_date) VALUES
(10, 10, 10, 'M', 'RED', 2, 'CART', 'system', now());

INSERT INTO order_item (id, user_id, product_id, product_size, product_color, quantity, order_state, created_by, created_date) VALUES
(11, 10, 10, 'L', 'BLACK', 3, 'CART', 'system', now());

INSERT INTO order_item (id, user_id, product_id, product_size, product_color, quantity, order_state, created_by, created_date) VALUES
(12, 12, 11, 'S', 'PINK', 1, 'CART', 'system', now());

INSERT INTO order_item (id, user_id, product_id, product_size, product_color, quantity, order_state, created_by, created_date) VALUES
(13, 10, 12, 'S', 'BEIGE', 5, 'CART', 'system', now());

INSERT INTO order_item (id, user_id, product_id, product_size, product_color, quantity, order_state, created_by, created_date) VALUES
(14, 10, 12, 'M', 'RED', 1, 'CART', 'system', now());

INSERT INTO order_item (id, user_id, product_id, product_size, product_color, quantity, order_state, created_by, created_date) VALUES
(15, 12, 11, 'M', 'COLORFUL', 3, 'CART', 'system', now());
