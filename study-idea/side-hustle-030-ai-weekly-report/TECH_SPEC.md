# AI周报生成器 — 技术实现指南

## 一、项目结构

```
ai-weekly-report/
├── frontend/                    # Vue 3 前端
│   ├── src/
│   │   ├── views/
│   │   │   ├── Home.vue        # 首页/输入页
│   │   │   ├── Generate.vue    # 生成结果页
│   │   │   ├── History.vue     # 历史记录
│   │   │   └── Member.vue      # 会员中心
│   │   ├── components/
│   │   │   ├── WorkInput.vue   # 工作内容输入组件
│   │   │   ├── TemplateSelect.vue # 模板选择
│   │   │   ├── ToneSelect.vue  # 语气选择
│   │   │   └── ReportPreview.vue # 周报预览
│   │   ├── api/
│   │   │   ├── report.js       # 周报相关API
│   │   │   ├── user.js         # 用户相关API
│   │   │   └── payment.js      # 支付相关API
│   │   └── utils/
│   │       └── request.js      # Axios封装
│   └── package.json
│
├── backend/                     # Spring Boot 后端
│   ├── src/main/java/com/weeklyreport/
│   │   ├── controller/
│   │   │   ├── ReportController.java
│   │   │   ├── UserController.java
│   │   │   └── PaymentController.java
│   │   ├── service/
│   │   │   ├── ReportService.java
│   │   │   ├── AIService.java
│   │   │   ├── UserService.java
│   │   │   └── PaymentService.java
│   │   ├── entity/
│   │   │   ├── User.java
│   │   │   ├── WeeklyReport.java
│   │   │   └── Membership.java
│   │   ├── mapper/
│   │   │   ├── UserMapper.java
│   │   │   └── ReportMapper.java
│   │   └── config/
│   │       ├── WechatConfig.java
│   │       └── AIConfig.java
│   └── pom.xml
│
├── database/                    # 数据库脚本
│   └── init.sql
│
└── deploy/                      # 部署配置
    ├── docker-compose.yml
    └── nginx.conf
```

---

## 二、数据库设计

```sql
-- 用户表
CREATE TABLE user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    openid VARCHAR(64) UNIQUE NOT NULL COMMENT '微信openid',
    nickname VARCHAR(64) COMMENT '昵称',
    avatar VARCHAR(255) COMMENT '头像',
    phone VARCHAR(20) COMMENT '手机号',
    free_count INT DEFAULT 3 COMMENT '今日免费次数',
    free_reset_date DATE COMMENT '免费次数重置日期',
    member_level TINYINT DEFAULT 0 COMMENT '会员等级: 0免费 1月度 2年度',
    member_expire_date DATETIME COMMENT '会员到期时间',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_openid (openid),
    INDEX idx_member (member_level, member_expire_date)
);

-- 周报表
CREATE TABLE weekly_report (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    title VARCHAR(100) COMMENT '周报标题',
    work_content TEXT NOT NULL COMMENT '本周工作内容',
    problems TEXT COMMENT '遇到的问题',
    next_week_plan TEXT COMMENT '下周计划',
    tone VARCHAR(20) COMMENT '语气: formal/casual/concise',
    template VARCHAR(50) COMMENT '模板类型',
    generated_content TEXT COMMENT 'AI生成的周报内容',
    word_count INT COMMENT '字数',
    is_starred TINYINT DEFAULT 0 COMMENT '是否收藏',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user (user_id, created_at),
    INDEX idx_starred (is_starred)
);

-- 会员订单表
CREATE TABLE membership_order (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    order_no VARCHAR(64) UNIQUE NOT NULL COMMENT '订单号',
    member_level TINYINT NOT NULL COMMENT '会员等级',
    amount DECIMAL(10,2) NOT NULL COMMENT '金额',
    pay_status TINYINT DEFAULT 0 COMMENT '支付状态: 0未支付 1已支付 2已退款',
    pay_time DATETIME COMMENT '支付时间',
    transaction_id VARCHAR(64) COMMENT '微信支付交易号',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user (user_id),
    INDEX idx_order_no (order_no)
);

-- AI调用日志表
CREATE TABLE ai_call_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT COMMENT '用户ID',
    model VARCHAR(50) NOT NULL COMMENT '调用的模型',
    prompt_tokens INT COMMENT '输入token数',
    completion_tokens INT COMMENT '输出token数',
    total_tokens INT COMMENT '总token数',
    cost DECIMAL(10,4) COMMENT '费用',
    response_time INT COMMENT '响应时间(ms)',
    status TINYINT DEFAULT 1 COMMENT '状态: 0失败 1成功',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user (user_id),
    INDEX idx_created (created_at)
);
```

---

## 三、核心代码实现

### 3.1 后端 - AI服务

```java
@Service
public class AIService {
    
    @Autowired
    private OpenAiClient openAiClient;
    
    @Autowired
    private QianWenClient qianWenClient;
    
    private static final Map<String, String> TONE_PROMPTS = Map.of(
        "formal", "请使用正式、专业的语言风格",
        "casual", "请使用轻松、友好的语言风格",
        "concise", "请使用简洁、精炼的语言风格"
    );
    
    public String generateReport(ReportRequest request) {
        String prompt = buildPrompt(request);
        
        // 优先使用通义千问（国内更稳定）
        try {
            return qianWenClient.chat(prompt);
        } catch (Exception e) {
            // 降级到OpenAI
            return openAiClient.chat(prompt);
        }
    }
    
    private String buildPrompt(ReportRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("你是一个专业的周报撰写助手。请根据用户输入的工作内容，生成一份结构清晰、语言专业的周报。\n\n");
        sb.append("要求：\n");
        sb.append("1. 使用Markdown格式\n");
        sb.append("2. 分为以下几个部分：\n");
        sb.append("   - 本周工作完成情况\n");
        sb.append("   - 遇到的问题与解决方案\n");
        sb.append("   - 下周工作计划\n");
        sb.append("   - 需要协调的事项（如有）\n");
        sb.append("3. ").append(TONE_PROMPTS.getOrDefault(request.getTone(), "")).append("\n");
        sb.append("4. 适当润色，让内容更有条理和逻辑\n");
        sb.append("5. 字数控制在400-600字\n\n");
        
        sb.append("用户输入的工作内容：\n");
        sb.append(request.getWorkContent());
        
        if (StringUtils.hasText(request.getProblems())) {
            sb.append("\n\n遇到的问题：\n").append(request.getProblems());
        }
        
        if (StringUtils.hasText(request.getNextWeekPlan())) {
            sb.append("\n\n下周计划：\n").append(request.getNextWeekPlan());
        }
        
        return sb.toString();
    }
}
```

### 3.2 后端 - 周报服务

```java
@Service
public class ReportService {
    
    @Autowired
    private ReportMapper reportMapper;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private AIService aiService;
    
    @Autowired
    private AICallLogMapper aiCallLogMapper;
    
    public WeeklyReport generateReport(Long userId, ReportRequest request) {
        // 1. 检查用户是否有剩余次数
        User user = userService.getById(userId);
        if (!userService.hasAvailableCount(user)) {
            throw new BusinessException("今日免费次数已用完，请升级会员");
        }
        
        // 2. 调用AI生成周报
        long startTime = System.currentTimeMillis();
        String generatedContent;
        int promptTokens = 0;
        int completionTokens = 0;
        
        try {
            generatedContent = aiService.generateReport(request);
            // 这里可以添加token统计逻辑
            promptTokens = estimateTokens(request.toString());
            completionTokens = estimateTokens(generatedContent);
        } catch (Exception e) {
            aiCallLogMapper.insert(buildCallLog(userId, "failed", 0, 0, 0, 
                System.currentTimeMillis() - startTime));
            throw new BusinessException("AI生成失败，请稍后重试");
        }
        
        // 3. 记录AI调用日志
        long responseTime = System.currentTimeMillis() - startTime;
        double cost = calculateCost(promptTokens, completionTokens);
        aiCallLogMapper.insert(buildCallLog(userId, "success", promptTokens, 
            completionTokens, cost, responseTime));
        
        // 4. 保存周报记录
        WeeklyReport report = new WeeklyReport();
        report.setUserId(userId);
        report.setWorkContent(request.getWorkContent());
        report.setProblems(request.getProblems());
        report.setNextWeekPlan(request.getNextWeekPlan());
        report.setTone(request.getTone());
        report.setTemplate(request.getTemplate());
        report.setGeneratedContent(generatedContent);
        report.setWordCount(generatedContent.length());
        reportMapper.insert(report);
        
        // 5. 扣减用户免费次数
        userService.decreaseFreeCount(userId);
        
        return report;
    }
    
    private int estimateTokens(String text) {
        // 简单估算：中文约1.5字/token，英文约4字符/token
        return (int) (text.length() / 1.5);
    }
    
    private double calculateCost(int promptTokens, int completionTokens) {
        // 通义千问定价：输入0.008元/千token，输出0.02元/千token
        return promptTokens * 0.000008 + completionTokens * 0.00002;
    }
}
```

### 3.3 后端 - 用户服务

```java
@Service
public class UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    public boolean hasAvailableCount(User user) {
        // 会员用户无限次数
        if (user.getMemberLevel() > 0 && 
            user.getMemberExpireDate().isAfter(LocalDateTime.now())) {
            return true;
        }
        
        // 免费用户每天3次
        LocalDate today = LocalDate.now();
        if (!today.equals(user.getFreeResetDate())) {
            // 新的一天，重置次数
            user.setFreeCount(3);
            user.setFreeResetDate(today);
            userMapper.updateById(user);
        }
        
        return user.getFreeCount() > 0;
    }
    
    public void decreaseFreeCount(Long userId) {
        User user = userMapper.selectById(userId);
        if (user.getMemberLevel() == 0 || 
            user.getMemberExpireDate().isBefore(LocalDateTime.now())) {
            user.setFreeCount(user.getFreeCount() - 1);
            userMapper.updateById(user);
        }
    }
}
```

### 3.4 前端 - 输入页面

```vue
<template>
  <div class="home-container">
    <el-card class="input-card">
      <template #header>
        <div class="card-header">
          <span>AI周报生成器</span>
          <el-tag type="success">免费3次/天</el-tag>
        </div>
      </template>
      
      <el-form :model="form" label-width="100px">
        <el-form-item label="本周工作" required>
          <el-input
            v-model="form.workContent"
            type="textarea"
            :rows="5"
            placeholder="请输入本周完成的工作内容，例如：&#10;1. 完成了用户模块的开发&#10;2. 修复了3个线上bug&#10;3. 参加了需求评审会议"
          />
        </el-form-item>
        
        <el-form-item label="遇到的问题">
          <el-input
            v-model="form.problems"
            type="textarea"
            :rows="3"
            placeholder="可选：遇到的问题和解决方案"
          />
        </el-form-item>
        
        <el-form-item label="下周计划">
          <el-input
            v-model="form.nextWeekPlan"
            type="textarea"
            :rows="3"
            placeholder="可选：下周的工作计划"
          />
        </el-form-item>
        
        <el-form-item label="语气风格">
          <el-radio-group v-model="form.tone">
            <el-radio-button label="formal">正式专业</el-radio-button>
            <el-radio-button label="casual">轻松友好</el-radio-button>
            <el-radio-button label="concise">简洁精炼</el-radio-button>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item>
          <el-button 
            type="primary" 
            size="large"
            :loading="loading"
            @click="handleGenerate"
          >
            生成周报
          </el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { generateReport } from '@/api/report'
import { ElMessage } from 'element-plus'

const router = useRouter()
const loading = ref(false)

const form = reactive({
  workContent: '',
  problems: '',
  nextWeekPlan: '',
  tone: 'formal',
  template: 'standard'
})

const handleGenerate = async () => {
  if (!form.workContent.trim()) {
    ElMessage.warning('请输入本周工作内容')
    return
  }
  
  loading.value = true
  try {
    const res = await generateReport(form)
    if (res.code === 200) {
      router.push({
        path: '/generate',
        query: { id: res.data.id }
      })
    } else {
      ElMessage.error(res.message || '生成失败')
    }
  } catch (error) {
    ElMessage.error('网络错误，请稍后重试')
  } finally {
    loading.value = false
  }
}

const handleReset = () => {
  Object.assign(form, {
    workContent: '',
    problems: '',
    nextWeekPlan: '',
    tone: 'formal'
  })
}
</script>

<style scoped>
.home-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
```

### 3.5 前端 - 结果展示页面

```vue
<template>
  <div class="generate-container">
    <el-card v-loading="loading" class="result-card">
      <template #header>
        <div class="card-header">
          <span>生成的周报</span>
          <div class="actions">
            <el-button @click="handleCopy">复制</el-button>
            <el-button @click="handleDownload">下载</el-button>
            <el-button type="primary" @click="handleRegenerate">重新生成</el-button>
          </div>
        </div>
      </template>
      
      <div v-if="report" class="report-content">
        <MarkdownRenderer :content="report.generatedContent" />
      </div>
      
      <div class="report-meta" v-if="report">
        <el-divider />
        <div class="meta-info">
          <span>字数：{{ report.wordCount }}</span>
          <span>生成时间：{{ formatTime(report.createdAt) }}</span>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getReportById } from '@/api/report'
import MarkdownRenderer from '@/components/MarkdownRenderer.vue'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const loading = ref(true)
const report = ref(null)

onMounted(async () => {
  const id = route.query.id
  if (!id) {
    router.push('/')
    return
  }
  
  try {
    const res = await getReportById(id)
    if (res.code === 200) {
      report.value = res.data
    } else {
      ElMessage.error('周报不存在')
      router.push('/')
    }
  } catch (error) {
    ElMessage.error('加载失败')
  } finally {
    loading.value = false
  }
})

const handleCopy = async () => {
  try {
    await navigator.clipboard.writeText(report.value.generatedContent)
    ElMessage.success('已复制到剪贴板')
  } catch (error) {
    ElMessage.error('复制失败')
  }
}

const handleDownload = () => {
  const blob = new Blob([report.value.generatedContent], { type: 'text/markdown' })
  const url = URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url
  a.download = `周报_${formatDate(report.value.createdAt)}.md`
  a.click()
  URL.revokeObjectURL(url)
}

const handleRegenerate = () => {
  router.push('/')
}

const formatTime = (time) => {
  return new Date(time).toLocaleString()
}

const formatDate = (time) => {
  return new Date(time).toISOString().split('T')[0]
}
</script>
```

---

## 四、部署方案

### 4.1 Docker Compose

```yaml
version: '3.8'

services:
  frontend:
    build: ./frontend
    ports:
      - "80:80"
    depends_on:
      - backend
  
  backend:
    build: ./backend
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - DB_HOST=mysql
      - DB_PORT=3306
      - DB_NAME=weekly_report
      - DB_USER=root
      - DB_PASSWORD=${DB_PASSWORD}
      - REDIS_HOST=redis
      - REDIS_PORT=6379
      - WECHAT_APPID=${WECHAT_APPID}
      - WECHAT_SECRET=${WECHAT_SECRET}
      - AI_API_KEY=${AI_API_KEY}
    depends_on:
      - mysql
      - redis
  
  mysql:
    image: mysql:8.0
    ports:
      - "3306:3306"
    environment:
      - MYSQL_ROOT_PASSWORD=${DB_PASSWORD}
      - MYSQL_DATABASE=weekly_report
    volumes:
      - mysql_data:/var/lib/mysql
      - ./database/init.sql:/docker-entrypoint-initdb.d/init.sql
  
  redis:
    image: redis:7-alpine
    ports:
      - "6379:6379"
    volumes:
      - redis_data:/data

volumes:
  mysql_data:
  redis_data:
```

### 4.2 Nginx配置

```nginx
server {
    listen 80;
    server_name your-domain.com;
    
    # 前端静态文件
    location / {
        root /usr/share/nginx/html;
        index index.html;
        try_files $uri $uri/ /index.html;
    }
    
    # 后端API代理
    location /api/ {
        proxy_pass http://backend:8080/api/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }
}
```

---

## 五、成本估算

### 5.1 服务器成本
| 配置 | 价格 | 说明 |
|-----|------|-----|
| 2核4G ECS | 100元/月 | 阿里云/腾讯云 |
| 50G SSD | 20元/月 | 系统盘 |
| MySQL RDS | 50元/月 | 基础版（可选，初期可用ECS自建） |
| Redis | 30元/月 | 1G（可选，初期可用ECS自建） |
| **合计** | **约200元/月** | 初期可压缩到100元/月 |

### 5.2 AI API成本
| 模型 | 输入价格 | 输出价格 | 单次成本 |
|-----|---------|---------|---------|
| 通义千问 | 0.008元/千token | 0.02元/千token | 约0.01元/次 |
| OpenAI GPT-3.5 | 0.01元/千token | 0.02元/千token | 约0.015元/次 |

**假设：**
- 日均1000次调用
- 单次成本0.01元
- 月成本：1000 × 0.01 × 30 = 300元

### 5.3 总成本
| 阶段 | 月成本 |
|-----|-------|
| 初期（日均100次） | 130元 |
| 成长期（日均1000次） | 400元 |
| 稳定期（日均10000次） | 4000元 |

---

## 六、快速启动命令

```bash
# 1. 创建项目目录
mkdir ai-weekly-report && cd ai-weekly-report

# 2. 初始化前端
npm create vue@latest frontend
cd frontend
npm install element-plus axios marked
cd ..

# 3. 初始化后端（使用Spring Initializr）
# 访问 https://start.spring.io/
# 选择: Spring Boot 3.x, Java 17
# 依赖: Spring Web, MyBatis Plus, MySQL, Redis, Lombok

# 4. 创建数据库
mysql -u root -p
CREATE DATABASE weekly_report CHARACTER SET utf8mb4;

# 5. 启动开发
cd frontend && npm run dev &
cd backend && mvn spring-boot:run
```

---

*文档版本：v1.0*
*最后更新：2026年11月*
