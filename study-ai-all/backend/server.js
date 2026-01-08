const express = require('express');
const cors = require('cors');

const app = express();
const PORT = 5001;

// 启用CORS
app.use(cors());

// 模拟用户数据
const users = [
  { id: 1, name: '张三', email: 'zhangsan@example.com' },
  { id: 2, name: '李四', email: 'lisi@example.com' },
  { id: 3, name: '王五', email: 'wangwu@example.com' },
  { id: 4, name: '赵六', email: 'zhaoliu@example.com' },
  { id: 5, name: '孙七', email: 'sunqi@example.com' }
];

// 根路径 - Hello World
app.get('/', (req, res) => {
  res.send('Hello World! Welcome to the User List API.');
});

// 用户列表接口
app.get('/api/users', (req, res) => {
  res.json(users);
});

// 启动服务器
app.listen(PORT, () => {
  console.log(`Server is running on http://localhost:${PORT}`);
  console.log(`API endpoints:`);
  console.log(`  GET http://localhost:${PORT}/`);
  console.log(`  GET http://localhost:${PORT}/api/users`);
});