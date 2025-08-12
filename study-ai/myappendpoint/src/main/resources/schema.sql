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

CREATE TABLE user_token (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    token VARCHAR(255) NOT NULL,
    refresh_token VARCHAR(255) NOT NULL,
    token_expire_time TIMESTAMP NULL,
    refresh_token_expire_time TIMESTAMP NULL,
    FOREIGN KEY (user_id) REFERENCES app_user(id)
);

CREATE TABLE IF NOT EXISTS family_member (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
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
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member1_id BIGINT REFERENCES family_member(id),
    member2_id BIGINT REFERENCES family_member(id),
    relationship_type VARCHAR(20),
    start_date DATE,
    end_date DATE
);

CREATE TABLE IF NOT EXISTS APP_VERSION (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    version VARCHAR(20),
    release_date DATE,
    release_notes VARCHAR(255),
    download_url VARCHAR(255),
    force_update BOOLEAN DEFAULT false
);

CREATE TABLE app_message (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sender_id BIGINT NOT NULL,
    receiver_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    email VARCHAR(255),
    is_email BOOLEAN DEFAULT FALSE,
    is_read BOOLEAN DEFAULT FALSE,
    is_deleted BOOLEAN DEFAULT FALSE,
    create_time DATETIME NOT NULL,
    read_time DATETIME
);

CREATE TABLE app_like (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    m3u_entry_id INT NOT NULL,
    create_time TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES app_user(id),
    UNIQUE KEY uk_user_m3u_entry (user_id, m3u_entry_id)
);

