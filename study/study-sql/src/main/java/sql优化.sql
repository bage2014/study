

# select count(1) from customers

SELECT * FROM customers
 -- EXPLAIN SELECT * FROM customers WHERE id IS NULL;

SELECT * FROM customers WHERE id = '209803' OR id = '209799';
SELECT * FROM customers WHERE id = '209803' UNION  SELECT * FROM customers WHERE id = '209799';

SELECT * FROM customers WHERE id / 2 = 209803;
SELECT * FROM customers WHERE id  = 2 * 209803;


SELECT * FROM customers WHERE id IN('209803' ,'209799');

