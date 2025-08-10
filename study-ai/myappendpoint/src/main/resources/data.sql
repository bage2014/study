-- 初始化用户 zhangsan/lisi123，仅当用户不存在时执行
INSERT INTO APP_USER (
    username, password, login_attempts, lock_time,
    email, gender, birth_date, avatar_url
)
SELECT 
    'zhangsan', 'zhangsan123', 0, NULL,
    'zhangsan@qq.com', 'male', '1990-01-01', 'https://avatars.githubusercontent.com/u/18094768?v=4'

WHERE NOT EXISTS (
    SELECT 1 FROM APP_USER WHERE username = 'zhangsan'
);