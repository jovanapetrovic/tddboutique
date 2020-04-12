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

