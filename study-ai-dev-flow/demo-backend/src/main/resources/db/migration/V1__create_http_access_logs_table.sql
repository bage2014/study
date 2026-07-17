CREATE TABLE http_access_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    timestamp DATETIME NOT NULL,
    method VARCHAR(10) NOT NULL,
    url VARCHAR(2048) NOT NULL,
    status_code INT NOT NULL,
    client_ip VARCHAR(45) NOT NULL,
    user_agent VARCHAR(512),
    duration BIGINT NOT NULL,
    INDEX idx_timestamp (timestamp),
    INDEX idx_client_ip (client_ip)
);