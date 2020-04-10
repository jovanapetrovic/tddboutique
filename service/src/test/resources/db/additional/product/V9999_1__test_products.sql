INSERT INTO product (id, name, material, description, price, created_by, created_date) VALUES
('10', 'Flower dress', 'cotton, polyester', 'A beautiful spring dress with flower print', '20.00', 'system', now());

INSERT INTO product_color (product_id, color) VALUES
('10', 'COLORFUL');

INSERT INTO product_size (product_id, size) VALUES
('10', 'S'),
('10', 'M'),
('10', 'L'),
('10', 'XL');

INSERT INTO stock (product_id, units, created_by, created_date) VALUES
('10', 50, 'system', now());

------------------------------------------------------------------------------------------------------------------------

INSERT INTO product (id, name, material, description, price, created_by, created_date) VALUES
('11', 'Maxi summer dress', 'cotton, polyester', 'Amazing maxi summer dress', '39.99', 'system', now());

INSERT INTO product_color (product_id, color) VALUES
('11', 'WHITE'),
('11', 'PINK'),
('11', 'YELLOW'),
('11', 'BLUE');

INSERT INTO product_size (product_id, size) VALUES
('11', 'S'),
('11', 'M'),
('11', 'L'),
('11', 'XL');

INSERT INTO stock (product_id, units, created_by, created_date) VALUES
('11', 1, 'system', now());

------------------------------------------------------------------------------------------------------------------------

