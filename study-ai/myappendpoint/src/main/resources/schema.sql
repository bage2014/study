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

CREATE TABLE IF NOT EXISTS family_member (
    id BIGINT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    gender VARCHAR(10),
    birth_date DATE,
    birth_place VARCHAR(200),
    occupation VARCHAR(100),
    education VARCHAR(100),
    contact_info VARCHAR(200),
    avatar VARCHAR(255),
    deceased BOOLEAN DEFAULT false,
    death_date DATE
);

CREATE TABLE IF NOT EXISTS family_relationship (
    id BIGINT PRIMARY KEY,
    member1_id BIGINT REFERENCES family_member(id),
    member2_id BIGINT REFERENCES family_member(id),
    relationship_type VARCHAR(20),
    start_date DATE,
    end_date DATE
);