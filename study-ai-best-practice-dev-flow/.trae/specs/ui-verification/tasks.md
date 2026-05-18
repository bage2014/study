# 家族管理系统 - UI功能验证执行计划

## [ ] Task 1: 验证服务状态
- **Priority**: P0
- **Depends On**: None
- **Description**: 
  - 检查后端服务是否运行在 http://localhost:8080
  - 检查前端服务是否运行在 http://localhost:5173
- **Acceptance Criteria Addressed**: 所有AC
- **Test Requirements**:
  - `human-judgement`: 确认后端服务启动日志显示"Started Application"
  - `human-judgement`: 确认前端服务显示"VITE ready"
- **Notes**: 服务必须正常运行才能进行后续测试

## [ ] Task 2: 验证登录功能
- **Priority**: P0
- **Depends On**: Task 1
- **Description**: 
  - 访问登录页面 http://localhost:5173
  - 验证页面显示正确（登录/注册标签、表单）
  - 测试正确登录（admin/admin123）
  - 测试错误密码登录
  - 测试登录态持久化（刷新页面）
- **Acceptance Criteria Addressed**: AC-1, AC-2
- **Test Requirements**:
  - `human-judgement`: 登录页面元素完整显示
  - `human-judgement`: 正确用户名密码成功登录并跳转首页
  - `human-judgement`: 错误密码显示"用户名或密码错误"提示
  - `human-judgement`: 登录后刷新页面保持登录状态
- **Notes**: 使用默认测试用户 admin/admin123

## [ ] Task 3: 验证注册功能
- **Priority**: P0
- **Depends On**: Task 2
- **Description**: 
  - 切换到注册标签
  - 测试注册新用户
  - 测试用户名已存在场景
  - 测试邮箱已存在场景
- **Acceptance Criteria Addressed**: AC-3
- **Test Requirements**:
  - `human-judgement`: 注册表单完整显示
  - `human-judgement`: 新用户注册成功并切换到登录页面
  - `human-judgement`: 用户名已存在显示重复提示
  - `human-judgement`: 邮箱已存在显示重复提示
- **Notes**: 注册后可使用新用户登录验证

## [ ] Task 4: 验证家族管理功能
- **Priority**: P0
- **Depends On**: Task 2
- **Description**: 
  - 进入家族管理页面
  - 创建新家族（输入名称和描述）
  - 编辑家族信息
  - 删除家族（确认操作）
- **Acceptance Criteria Addressed**: AC-4, AC-5, AC-6
- **Test Requirements**:
  - `human-judgement`: 家族列表页面显示正确
  - `human-judgement`: 创建家族成功，列表更新
  - `human-judgement`: 编辑家族成功，信息更新
  - `human-judgement`: 删除家族成功，列表更新
- **Notes**: 先创建家族才能进行后续成员和关系测试

## [ ] Task 5: 验证成员管理功能
- **Priority**: P0
- **Depends On**: Task 4
- **Description**: 
  - 进入成员管理页面
  - 添加新成员（姓名、性别、出生日期等）
  - 编辑成员信息
  - 删除成员（确认操作）
  - 测试成员搜索功能
- **Acceptance Criteria Addressed**: AC-7, AC-8, AC-9
- **Test Requirements**:
  - `human-judgement`: 成员列表页面显示正确
  - `human-judgement`: 添加成员成功，列表更新
  - `human-judgement`: 编辑成员成功，信息更新
  - `human-judgement`: 删除成员成功，列表更新
  - `human-judgement`: 搜索功能正确过滤结果
- **Notes**: 需要先创建家族，再添加成员

## [ ] Task 6: 验证关系管理功能
- **Priority**: P0
- **Depends On**: Task 5
- **Description**: 
  - 进入关系管理页面
  - 添加成员关系（选择两个成员，选择关系类型）
  - 删除成员关系
- **Acceptance Criteria Addressed**: AC-10
- **Test Requirements**:
  - `human-judgement`: 关系列表页面显示正确
  - `human-judgement`: 添加关系成功，列表更新
  - `human-judgement`: 删除关系成功，列表更新
- **Notes**: 需要至少两个成员才能添加关系

## [ ] Task 7: 验证家族树功能
- **Priority**: P0
- **Depends On**: Task 6
- **Description**: 
  - 进入家族树页面
  - 选择家族查看家族树
  - 验证家族树布局和关系显示
- **Acceptance Criteria Addressed**: AC-11
- **Test Requirements**:
  - `human-judgement`: 家族树页面显示正确
  - `human-judgement`: 家族树正确渲染成员关系
  - `human-judgement`: 父子关系层级清晰
- **Notes**: 需要有成员和关系数据才能展示家族树

## [ ] Task 8: 验证历史事件功能
- **Priority**: P1
- **Depends On**: Task 4
- **Description**: 
  - 进入历史事件页面
  - 创建新事件（标题、描述、日期）
  - 编辑事件信息
  - 删除事件
  - 验证时间轴展示
- **Acceptance Criteria Addressed**: AC-12
- **Test Requirements**:
  - `human-judgement`: 事件页面显示正确
  - `human-judgement`: 创建事件成功，时间轴更新
  - `human-judgement`: 编辑事件成功
  - `human-judgement`: 删除事件成功
- **Notes**: 需要先选择家族

## [ ] Task 9: 验证多媒体库功能
- **Priority**: P1
- **Depends On**: Task 4
- **Description**: 
  - 进入多媒体库页面
  - 上传图片文件
  - 预览上传的文件
  - 删除文件
- **Acceptance Criteria Addressed**: AC-13
- **Test Requirements**:
  - `human-judgement`: 媒体库页面显示正确
  - `human-judgement`: 文件上传成功，列表更新
  - `human-judgement`: 文件预览正常
  - `human-judgement`: 删除文件成功
- **Notes**: 需要先选择家族

## [ ] Task 10: 验证轨迹追踪功能
- **Priority**: P1
- **Depends On**: Task 2
- **Description**: 
  - 进入轨迹追踪页面
  - 点击地图创建轨迹点
  - 编辑轨迹点信息
  - 删除轨迹点
  - 查看时间线展示
- **Acceptance Criteria Addressed**: AC-14
- **Test Requirements**:
  - `human-judgement`: 轨迹页面显示正确（地图+列表）
  - `human-judgement`: 创建轨迹点成功，地图标记更新
  - `human-judgement`: 编辑轨迹点成功
  - `human-judgement`: 删除轨迹点成功
  - `human-judgement`: 时间线按时间顺序展示
- **Notes**: 需要浏览器支持地图加载

## [ ] Task 11: 验证AI功能
- **Priority**: P1
- **Depends On**: Task 5
- **Description**: 
  - 进入AI功能页面
  - 测试关系预测功能
  - 测试故事生成功能
- **Acceptance Criteria Addressed**: AC-15, AC-16
- **Test Requirements**:
  - `human-judgement`: AI页面显示正确
  - `human-judgement`: 关系预测显示结果和置信度
  - `human-judgement`: 故事生成成功
- **Notes**: 需要有家族成员数据

## [ ] Task 12: 验证界面布局和导航
- **Priority**: P1
- **Depends On**: Task 2
- **Description**: 
  - 验证侧边栏导航功能
  - 验证家族切换功能
  - 验证页面布局正确性
- **Acceptance Criteria Addressed**: 所有AC
- **Test Requirements**:
  - `human-judgement`: 侧边栏菜单完整显示
  - `human-judgement`: 菜单点击正确跳转
  - `human-judgement`: 家族切换正常
  - `human-judgement`: 页面布局无错乱
- **Notes**: 这是贯穿所有页面的验证项

## [ ] Task 13: 记录测试结果和问题
- **Priority**: P0
- **Depends On**: 所有Task
- **Description**: 
  - 记录每个功能的测试结果
  - 记录发现的问题和异常
  - 生成测试报告
- **Acceptance Criteria Addressed**: 所有AC
- **Test Requirements**:
  - `human-judgement`: 测试报告完整记录所有验证结果
  - `human-judgement`: 问题描述清晰，包含复现步骤
- **Notes**: 使用checklist.md记录验证状态