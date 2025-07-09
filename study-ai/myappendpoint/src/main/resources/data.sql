-- 初始化用户 zhangsan/lisi123，仅当用户不存在时执行
INSERT INTO APP_USER (username, password, login_attempts, lock_time)
SELECT 'zhangsan', 'lisi123', 0, NULL
WHERE NOT EXISTS (
    SELECT 1 FROM APP_USER WHERE username = 'zhangsan'
);