CREATE TABLE message (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    content VARCHAR(5000) NOT NULL COMMENT '消息内容',
    sender VARCHAR(100) NOT NULL COMMENT '发送者标识',
    receiver VARCHAR(100) NOT NULL COMMENT '接收者标识',
    status VARCHAR(20) NOT NULL DEFAULT 'UNREAD' COMMENT '消息状态: UNREAD-未读, READ-已读, DELETED-已删除',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_sender (sender),
    INDEX idx_receiver (receiver),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='消息表';