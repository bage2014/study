# 副业Idea：JetBrains Marketplace 插件开发

## Idea概述

为IntelliJ IDEA / WebStorm / PyCharm等JetBrains IDE开发付费插件,上架官方Marketplace,通过订阅制获得全球开发者用户付费。Java背景天然契合(你每天用IDEA)。

> 核心定位:**做IDE里的"App Store应用开发者"**。Marketplace官方收10%佣金,90%归你,Stripe自动结算,全球用户。

## 与其他idea的区别

| 维度 | 024 GitHub开源 | 025 GitHub Action | 027 JetBrains插件 |
| :--- | :--- | :--- | :--- |
| 生态 | GitHub | GitHub Marketplace | JetBrains Marketplace |
| 用户 | 开发者(泛) | GitHub项目 | IDEA用户(Java为主) |
| 形态 | 代码仓库 | Action/App | IDE插件 |
| 触发 | 被动使用 | CI/CD触发 | 用户编码时实时 |
| 收入节奏 | 6-12月起步 | 3月见钱 | 2-4周上架即卖 |

---

## 核心价值

- **天然匹配Java背景**:你天天用IDEA,痛点你最清楚
- **官方Marketplace**:自带全球流量,SEO+JetBrains推荐
- **付费习惯成熟**:IDEA用户已习惯付费( Ultimate版$89/年)
- **支付自动**:Stripe集成,90%佣金归你
- **订阅制复利**:月付/年付,用户粘性极强
- **技术护城河**:IntelliJ Platform SDK有学习曲线,挡住一波人

## 目标用户

| 客户 | 痛点 | 付费意愿 |
| :--- | :--- | :--- |
| IntelliJ IDEA Ultimate用户 | 想提升编码效率 | ★★★★★ |
| Java企业团队 | 团队规范/效率统一 | ★★★★★ |
| Spring Boot开发者 | 配置/调试/部署繁琐 | ★★★★☆ |
| 学生/个人开发者 | 预算有限 | ★★☆☆☆(量大人多) |

---

## 产品方向(10个具体idea)

### 方向一:代码生成类 ⭐首选

| 插件名 | 功能 | 定价参考 |
| :--- | :--- | :--- |
| **Spring CRUD Master** | 选实体→一键生成Controller/Service/Mapper/DTO/SQL | $4.9/月 或 $49永久 |
| **JPA Query Helper** | 自动生成JPA Specification/QueryDSL | $3.9/月 |
| **DTO Mapper Generator** | 实体↔DTO自动映射,MapStruct增强 | $4.9/月 |
| **Mock Data Faker** | 给Java类一键填充测试数据 | $2.9/月 |

### 方向二:AI增强类(红利方向)

| 插件名 | 功能 | 定价参考 |
| :--- | :--- | :--- |
| **Local LLM Coder** | 接本地Ollama,IDE内AI补全(不传云) | $9.9/月 |
| **AI Commit Message** | 根据diff自动生成规范commit message | $2.9/月 |
| **Java Doc AI** | 自动给方法/类生成中文Javadoc | $3.9/月 |
| **Refactor Suggest** | AI建议重构方案+一键应用 | $6.9/月 |

### 方向三:效率工具类

| 插件名 | 功能 | 定价参考 |
| :--- | :--- | :--- |
| **Git Branch Story** | 分支树可视化+一键切换/对比 | $2.9/月 |
| **Quick Switch** | 类似Vim的快捷键提示+学习 | $1.9/月 |
| **Chinese Java Doc** | 翻译JDK/Spring官方Javadoc为中文 | $4.9/月 |
| **Code Snapshot** | 代码段保存+分享(Gist图片) | 免费+Pro版$2.9 |

### 方向四:Spring生态专项

| 插件名 | 功能 | 定价参考 |
| :--- | :--- | :--- |
| **Spring Boot Config Autocomplete** | application.yml智能补全+校验 | $4.9/月 |
| **MyBatis SQL Inspector** | Mapper方法→SQL实时查看+执行计划 | $6.9/月 |
| **Actuator Dashboard** | IDE内查看Spring Actuator指标 | $4.9/月 |

---

## 技术栈建议

### 核心技术栈
```
语言:Kotlin(首选) 或 Java
SDK:IntelliJ Platform SDK (Gradle IntelliJ Plugin)
构建:Gradle (Kotlin DSL)
兼容:支持2023.2+ 多个IDEA版本
测试:JUnit5 + IDEA测试容器
CI:GitHub Actions自动build + verifyPlugin
```

### project 标准结构
```
my-plugin/
├── build.gradle.kts         # 含intellij plugin配置
├── src/main/kotlin/         # 插件代码
├── src/main/resources/
│   ├── META-INF/plugin.xml  # 插件元数据
│   └── icons/               # 插件图标
├── CHANGELOG.md
├── README.md
└── .github/workflows/       # build+release自动化
```

### build.gradle.kts核心配置
```kotlin
plugins {
  id("org.jetbrains.intellij") version "1.17.0"
  kotlin("jvm") version "1.9.22"
}

intellij {
  version.set("2023.2")           // 基线版本
  type.set("IC")                   // Community版兼容
  plugins.set(listOf("java", "git4idea"))
}

tasks {
  patchPluginXml {
    sinceBuild.set("232")
    untilBuild.set("242.*")
  }
  publishPlugin {
    token.set(System.getenv("PUBLISH_TOKEN"))
    channels.set(listOf("stable"))
  }
}
```

---

## 盈利模式

### Marketplace标准定价
| 模式 | 价格区间 | 说明 |
| :--- | :--- | :--- |
| Free | 0 | 引流,内嵌Pro升级提示 |
| Personal订阅 | $1.9-9.9/月 | 个人开发者 |
| Personal永久 | $29-99 | 一次买断(老用户喜欢) |
| Commercial订阅 | $9.9-49/月 | 公司团队 |
| Custom | 议价 | 企业定制功能 |

### 推荐组合
- **免费版**:核心功能+启动广告位(宣传Pro)
- **Pro版**:高级功能+无广告+优先支持
- **团队版**:批量授权+管理后台

### 收入预期
| 阶段 | 时间 | 付费用户 | 月收入 |
| :--- | :--- | :--- | :--- |
| 起步期 | 0-3个月 | 0-30 | 0-1500元 |
| 成长期 | 3-9个月 | 30-300 | 1500-15000元 |
| 稳定期 | 9-18个月 | 300-2000 | 15000-100000元 |
| 矩阵期 | 18月+ | 多插件合计 | 100000元+ |

> 案例:Rainbow Brackets插件作者,Izhangzhihai,年订阅收入约$8-15万(参考其公开数据)

---

## 实施步骤

### 第1周:环境与第一个插件
1. 安装IntelliJ IDEA Community + JDK 21
2. 用官方模板生成项目(New Project → IDE Plugin)
3. 跑通"Hello Action"插件
4. 阅读[IntelliJ Platform SDK](https://plugins.jetbrains.com/docs/intellij/overview.html)前3章

### 第2-3周:开发第一个产品
1. 选定方向:**Spring CRUD Master**(Java背景刚需)
2. 实现核心:右键实体类→Generate→Spring CRUD→生成5个文件
3. 接入Marketplace发布token
4. 编写README+截图+演示GIF

### 第4周:上架Marketplace
1. 提交插件到[JetBrains Plugin Repository](https://plugins.jetbrains.com/)
2. 审核:1-7天(代码安全审查+功能验证)
3. 通过后设置定价(走Stripe账户)
4. 在掘金/Reddit r/javaidea发推广文

### 第5-12周:增长与迭代
1. 收集首批用户反馈(issue)
2. 每周1次小版本更新
3. 推出Pro版本功能
4. 启动第二个插件(复用基础设施)

### 第3-6月:产品矩阵
1. 上线3-5个插件,形成"Java开发者插件包"
2. 推出Team License(团队授权)
3. 接入JetBrains Subscription Program(官方统一计费)
4. 与JetBrains官方合作(Great Plugin Award申请)

---

## 成功案例参考

| 插件 | 收入级别 | 启示 |
| :--- | :--- | :--- |
| **Rainbow Brackets** | $5万+/年 | 视觉类小痛点也能赚大钱 |
| **.ignore** | $3万+/年 | 高频刚需 |
| **Key Promoter X** | $1万+/年 | 学习类工具 |
| **Lombok插件** | 已被JetBrains收购 | 工具类天花板 |
| **Chinese Plugin** | 数十万用户 | 中文市场刚需 |

---

## 可行性评估

| 维度 | 评分 | 说明 |
| :--- | :--- | :--- |
| 技术难度 | ★★★☆☆ | IntelliJ SDK有曲线,但Java背景顺 |
| 资金需求 | ★☆☆☆☆ | 零成本(IDEA Community免费) |
| 上架速度 | ★★★★★ | 1-2周可上架 |
| 收入启动 | ★★★★☆ | 1个月内首笔收入 |
| 复利/被动 | ★★★★★ | 订阅制,老用户持续付费 |
| Java背景契合 | ★★★★★ | 完美匹配 |
| 长期可持续 | ★★★★☆ | IDE不会死,Java不会死 |
| **综合评分** | **★★★★★** | **Java背景副业最佳选择之一** |

---

## 风险与应对

| 风险 | 应对 |
| :--- | :--- |
| IDEA官方做相同功能 | 选官方不会做的细分(如中文Javadoc、Spring专项) |
| 用户破解/盗版 | 持续迭代+License校验+付费社群做增值 |
| 跨版本兼容 | 用`sinceBuild/untilBuild`控制范围,版本分渠道 |
| 流量不足 | 免费版引流+主站+社群+公众号矩阵 |
| 维护精力 | 矩阵复用,新插件用旧插件基础设施 |

---

## 为什么适合Java背景

1. **你天天用IDEA**,痛点感受最真,产品最接地气
2. **Kotlin/Java同源**,SDK上手快
3. **Spring Boot生态大**,相关插件缺口大
4. **付费习惯成熟**:IDEA用户已经习惯付$89/年,再付$5/月插件无障碍
5. **企业客户多**:JetBrains Ultimate企业版用户是中国一二线互联网公司主力
6. **全球市场**:Marketplace不区分地域,英文README即可全球卖

---

*最后更新：2026年8月*

**一句话总结**:你天天抱怨IDEA哪个功能不好用,那就是你的下一个付费插件。
