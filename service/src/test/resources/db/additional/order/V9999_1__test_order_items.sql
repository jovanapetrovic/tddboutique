INSERT INTO product (id, name, material, description, price, created_by, created_date) VALUES
('10', 'Casual dress', 'viscose, cotton, polyester', 'A beautiful everyday dress', '20.00', 'system', now());

INSERT INTO product_color (product_id, color) VALUES
('10', 'BLACK'),
('10', 'RED');

INSERT INTO product_size (product_id, size) VALUES
('10', 'S'),
('10', 'M'),
('10', 'L'),
('10', 'XL');

INSERT INTO stock (product_id, units, created_by, created_date) VALUES
('10', 50, 'system', now());

------------------------------------------------------------------------------------------------------------------------

INSERT INTO order_item (id, user_id, product_id, product_size, product_color, quantity, order_state, created_by, created_date) VALUES
(10, 11, 10, 'L', 'BLACK', 2, 'CART', 'system', now());