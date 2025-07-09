CREATE TABLE IF NOT EXISTS APP_USER (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    login_attempts INT DEFAULT 0,
    lock_time TIMESTAMP NULL,
    -- 新增字段
    email VARCHAR(100),
    gender VARCHAR(10),
    birth_date DATE,
    avatar_url VARCHAR(255)
);