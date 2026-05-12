# pre-commit 钩子配置
# 提交前自动执行检查

## 功能描述
在 Git 提交前自动执行代码格式化、Lint 和单元测试，确保代码质量。

## 自动执行的检查

### 1. 代码格式化检查

**后端（Java）**
```bash
# 检查 Maven 代码格式
mvn formatter:format
```

**前端（JavaScript/Vue）**
```bash
# 检查 ESLint
npm run lint
```

### 2. 单元测试执行

**后端**
```bash
# 运行后端单元测试
mvn test
```

**前端**
```bash
# 运行前端单元测试
npm run test:unit
```

### 3. 覆盖率检查

**后端**
- 覆盖率必须 ≥ 85%

**前端**
- 覆盖率必须 ≥ 80%

### 4. 文档完整性检查

**检查内容**
- 功能模块文档是否完整（需求、交互、契约、测试、进度）
- API 契约文档是否与代码同步
- 测试文档是否与测试用例同步

**检查命令**
```bash
# 检查功能模块文档完整性
find docs/features -type d -exec sh -c '
  dir="$1"
  required="需求文档 交互文档 契约文档 测试文档 进度文档"
  for doc in $required; do
    if [ ! -f "$dir/*-$doc-*.md" ]; then
      echo "❌ 缺失文档: $dir 需要 $doc"
      exit 1
    fi
  done
' _ {} \;
```

### 5. 安全检查

**检查内容**
- 是否存在 SQL 注入风险
- 是否存在硬编码密钥
- 是否存在敏感信息泄露

**检查命令**
```bash
# 检查硬编码密钥
grep -rn "sk-[A-Za-z0-9]\{48\}" --include="*.java" --include="*.js" --include="*.vue" .
grep -rn "password\|secret\|key\|token" --include="*.properties" --include="*.yml" --include="*.yaml" .
```

## 检查流程

```
Git Pre-Commit Hook
        │
        ├── 1. 代码格式化
        │         │
        │         ├── 后端：mvn formatter:format
        │         └── 前端：npm run format
        │
        ├── 2. Lint 检查
        │         │
        │         ├── 后端：mvn checkstyle:check
        │         └── 前端：npm run lint
        │
        ├── 3. 单元测试
        │         │
        │         ├── 后端：mvn test
        │         └── 前端：npm run test:unit
        │
        ├── 4. 覆盖率检查
        │         │
        │         ├── 后端：JaCoCo 覆盖率报告 (≥85%)
        │         └── 前端：Clover 覆盖率报告 (≥80%)
        │
        ├── 5. 文档完整性检查
        │         │
        │         ├── 功能模块文档完整性
        │         ├── API 契约同步检查
        │         └── 测试文档同步检查
        │
        └── 6. 安全检查
                  │
                  ├── SQL 注入风险检查
                  ├── 硬编码密钥检查
                  └── 敏感信息泄露检查
```

## 失败处理

如果任何检查失败：
1. 显示错误信息
2. 列出需要修复的问题
3. 拒绝提交代码

## 跳过钩子

如需跳过 pre-commit 钩子（不推荐）：

```bash
git commit --no-verify -m "提交信息"
```

## 配置说明

将此文件复制到 `.git/hooks/pre-commit` 并添加执行权限：

```bash
chmod +x .git/hooks/pre-commit
```

或在项目中配置 husky（推荐）：

```bash
npm install husky --save-dev
npx husky install
```