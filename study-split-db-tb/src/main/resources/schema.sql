
CREATE TABLE IF NOT EXISTS t_order_2020 (
    id BIGINT NOT NULL AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    user_id INT NOT NULL,
    status VARCHAR(16),
    create_time timestamp,
    channel VARCHAR(16),
    supplier VARCHAR(16), PRIMARY KEY (id)
);