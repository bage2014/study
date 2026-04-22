-- 创建用户表
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建家族表
CREATE TABLE IF NOT EXISTS families (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    administrator_id BIGINT REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建成员表
CREATE TABLE IF NOT EXISTS members (
    id SERIAL PRIMARY KEY,
    family_id BIGINT REFERENCES families(id),
    name VARCHAR(100) NOT NULL,
    gender VARCHAR(10),
    birth_date DATE,
    death_date DATE,
    birth_place VARCHAR(255),
    death_place VARCHAR(255),
    biography TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建关系表
CREATE TABLE IF NOT EXISTS relationships (
    id SERIAL PRIMARY KEY,
    family_id BIGINT REFERENCES families(id),
    member_id1 BIGINT REFERENCES members(id),
    member_id2 BIGINT REFERENCES members(id),
    relationship_type VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建事件表
CREATE TABLE IF NOT EXISTS events (
    id SERIAL PRIMARY KEY,
    family_id BIGINT REFERENCES families(id),
    title VARCHAR(255) NOT NULL,
    description TEXT,
    event_date DATE,
    location VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建媒体表
CREATE TABLE IF NOT EXISTS media (
    id SERIAL PRIMARY KEY,
    family_id BIGINT REFERENCES families(id),
    member_id BIGINT REFERENCES members(id),
    event_id BIGINT REFERENCES events(id),
    file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(255) NOT NULL,
    file_type VARCHAR(50),
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建地点表
CREATE TABLE IF NOT EXISTS locations (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建里程碑表
CREATE TABLE IF NOT EXISTS milestones (
    id SERIAL PRIMARY KEY,
    family_id BIGINT REFERENCES families(id),
    member_id BIGINT REFERENCES members(id),
    title VARCHAR(255) NOT NULL,
    description TEXT,
    milestone_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建权限表
CREATE TABLE IF NOT EXISTS permissions (
    id SERIAL PRIMARY KEY,
    family_id BIGINT REFERENCES families(id),
    user_id BIGINT REFERENCES users(id),
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建操作日志表
CREATE TABLE IF NOT EXISTS operation_logs (
    id SERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id),
    operation_type VARCHAR(50) NOT NULL,
    operation_description TEXT,
    target_type VARCHAR(50),
    target_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 插入初始数据
-- 管理员用户
INSERT INTO users (username, email, password) VALUES 
('admin', 'admin@example.com', '$2a$12$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW');

-- 测试用户
INSERT INTO users (username, email, password) VALUES 
('user1', 'user1@example.com', '$2a$12$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW'),
('user2', 'user2@example.com', '$2a$12$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW');

-- 初始家族
INSERT INTO families (name, description, administrator_id) VALUES 
('测试家族', '测试家族描述', 1);

-- 初始成员
INSERT INTO members (family_id, name, gender, birth_date) VALUES 
(1, '张三', '男', '1980-01-01'),
(1, '李四', '女', '1982-02-02'),
(1, '张小明', '男', '2005-03-03');

-- 初始关系
INSERT INTO relationships (family_id, member_id1, member_id2, relationship_type) VALUES 
(1, 1, 2, '配偶'),
(1, 1, 3, '父子'),
(1, 2, 3, '母子');

-- 初始事件
INSERT INTO events (family_id, title, description, event_date, location) VALUES 
(1, '家族聚会', '2024年家族聚会', '2024-01-01', '北京');

-- 初始权限
INSERT INTO permissions (family_id, user_id, role) VALUES 
(1, 1, 'ADMIN'),
(1, 2, 'MEMBER');

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_members_family_id ON members(family_id);
CREATE INDEX IF NOT EXISTS idx_relationships_family_id ON relationships(family_id);
CREATE INDEX IF NOT EXISTS idx_events_family_id ON events(family_id);
CREATE INDEX IF NOT EXISTS idx_media_family_id ON media(family_id);
CREATE INDEX IF NOT EXISTS idx_permissions_family_id ON permissions(family_id);
CREATE INDEX IF NOT EXISTS idx_permissions_user_id ON permissions(user_id);