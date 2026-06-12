INSERT INTO PRODUCT (NAME, MANUFACTURER, SIZE, PRICE, CURRENT_STOCK, LOW_STOCK_THRESHOLD) VALUES
('Liquid yogurt XXL', 'Pilos', '1.5kg', 1.69, 500, 180),
('Crunchy peanut butter XXL', 'McKennedy', '1kg', 5.79, 80, 15),
('Homemade burek with cheese', 'Store bakery', '220g', 0.85, 30, 5),
('Mayo', 'STAR', '165g', 1.89, 90, 30),
('Long-life milk XXL', 'Ducat', '8x1L', 6.99, 300, 100);

INSERT INTO USERS(USERNAME, PASSWORD, ENABLED)
VALUES('user', '{noop}password', 1);

INSERT INTO USERS(USERNAME, PASSWORD, ENABLED)
VALUES('admin', '{noop}password', 1);

INSERT INTO USERS(USERNAME, PASSWORD, ENABLED)
VALUES('superadmin', '{noop}password', 1);

INSERT INTO USERS(USERNAME, PASSWORD, ENABLED)
VALUES('customer1','{noop}password',1);

INSERT INTO USERS(USERNAME, PASSWORD, ENABLED)
VALUES('customer2','{noop}password',1);

INSERT INTO ORDERS (DATE, USERNAME, SHIPPING_STATUS) VALUES ('2026-05-29T09:15:22', 'customer1', 'PENDING');

INSERT INTO ORDERS (DATE, USERNAME, SHIPPING_STATUS) VALUES ('2026-05-29T14:42:11', 'customer2', 'SHIPPED');

INSERT INTO ORDERS (DATE, USERNAME, SHIPPING_STATUS) VALUES ('2026-05-30T08:30:45', 'customer1', 'DELIVERED');

INSERT INTO ORDERS (DATE, USERNAME, SHIPPING_STATUS) VALUES ('2026-05-30T17:05:19', 'customer2', 'CANCELLED');

INSERT INTO ORDERS (DATE, USERNAME, SHIPPING_STATUS) VALUES ('2026-05-31T10:21:33', 'customer1', 'PENDING');

INSERT INTO ORDERS (DATE, USERNAME, SHIPPING_STATUS) VALUES ('2026-05-31T15:54:07', 'customer2', 'SHIPPED');

INSERT INTO ORDERS (DATE, USERNAME, SHIPPING_STATUS) VALUES ('2026-06-01T11:12:58', 'customer1', 'DELIVERED');

INSERT INTO ORDERS (DATE, USERNAME, SHIPPING_STATUS) VALUES ('2026-06-01T18:47:24', 'customer2', 'PENDING');

INSERT INTO ORDERS (DATE, USERNAME, SHIPPING_STATUS) VALUES ('2026-06-02T07:55:16', 'customer1', 'SHIPPED');

INSERT INTO ORDERS (DATE, USERNAME, SHIPPING_STATUS) VALUES ('2026-06-02T13:28:40', 'customer2', 'DELIVERED');

INSERT INTO ORDERS (DATE, USERNAME, SHIPPING_STATUS) VALUES ('2026-06-03T09:40:12', 'customer1', 'CANCELLED');

INSERT INTO ORDERS (DATE, USERNAME, SHIPPING_STATUS) VALUES ('2026-06-12T09:27:41', 'customer1', 'SHIPPED');

INSERT INTO ORDERS (DATE, USERNAME, SHIPPING_STATUS) VALUES ('2026-06-12T16:02:18', 'customer2', 'DELIVERED');

INSERT INTO ORDER_ITEM (QUANTITY, ORDER_ID, PRODUCT_ID) VALUES
                                                            (12, 1, 1),
                                                            (5, 1, 3),
                                                            (28, 2, 2),
                                                            (14, 2, 5),
                                                            (67, 3, 4),
                                                            (3, 4, 1),
                                                            (22, 4, 2),
                                                            (9, 4, 5),
                                                            (41, 5, 3),
                                                            (18, 6, 1),
                                                            (76, 6, 4),
                                                            (7, 7, 2),
                                                            (35, 7, 5),
                                                            (50, 8, 3),
                                                            (11, 8, 4),
                                                            (94, 9, 1),
                                                            (26, 10, 2),
                                                            (13, 10, 3),
                                                            (8, 10, 5),
                                                            (59, 11, 4),
                                                            (21, 12, 1),
                                                            (88, 12, 5),
                                                            (15, 13, 2),
                                                            (33, 13, 3),
                                                            (72, 13, 4);

INSERT INTO AUTHORITIES(USERNAME, AUTHORITY)
VALUES('user', 'ROLE_USER');

INSERT INTO AUTHORITIES(USERNAME, AUTHORITY)
VALUES('customer1','ROLE_USER');

INSERT INTO AUTHORITIES(USERNAME, AUTHORITY)
VALUES('customer2','ROLE_USER');

INSERT INTO AUTHORITIES(USERNAME, AUTHORITY)
VALUES('admin', 'ROLE_USER');

INSERT INTO AUTHORITIES(USERNAME, AUTHORITY)
VALUES('admin', 'ROLE_ADMIN');

INSERT INTO AUTHORITIES(USERNAME, AUTHORITY)
VALUES('superadmin', 'ROLE_USER');

INSERT INTO AUTHORITIES(USERNAME, AUTHORITY)
VALUES('superadmin', 'ROLE_ADMIN');