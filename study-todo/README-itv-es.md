# Elasticsearch 技术文档

## 第一部分：Elasticsearch 基础概念

### 1.1 什么是 Elasticsearch

Elasticsearch 是一个开源的分布式搜索和分析引擎，它基于 Lucene 构建，提供了 RESTful API 接口，支持实时搜索、分析和存储数据。Elasticsearch 被广泛应用于全文搜索、日志分析、数据可视化等场景。

### 1.2 Elasticsearch 核心概念

| Elasticsearch | 关系型数据库 |
| :------------ | :----------- |
| Document      | 行           |
| Type          | 表           |
| Index         | 库           |
| Shard         | 分区         |
| Replica       | 副本         |

#### 1.2.1 Document（文档）

可搜索的最小单位，我们向 Elasticsearch 存储的一条数据，就是一个文档。每个文档都有一个 id，可以自己指定，也可以让 Elasticsearch 生成。

#### 1.2.2 Index（索引）

索引是文档的容器，一类文档的集合。可以对文档元数据进行定义，比如名称、类型等。在物理上，索引的数据分布在分片上。

#### 1.2.3 Type（类型）

在 7.0 以前，一个索引可以定义多个 type，7.0 版本后，type 废除了，只有一个 type 为 "_doc"。

#### 1.2.4 Shard（分片）

分片是索引的物理分区，每个索引可以分为多个分片，每个分片是一个独立的 Lucene 索引。分片可以分布在不同的节点上，提高了系统的可扩展性和可用性。

#### 1.2.5 Replica（副本）

副本是分片的备份，用于提高系统的可用性和搜索性能。副本可以在不同的节点上，当主分片不可用时，副本可以升级为主分片。

### 1.3 Elasticsearch 架构

Elasticsearch 采用分布式架构，由多个节点组成，每个节点可以扮演不同的角色。

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/4016a5cd5b8c4b07aa3eb6b478a4588f~tplv-k3u1fbpfcp-zoom-in-crop-mark:1512:0:0:0.awebp?)

## 第二部分：Elasticsearch 核心技术

### 2.1 倒排搜索原理

倒排搜索是 Elasticsearch 的核心搜索机制，基于倒排索引数据结构实现，它将文档中的词项映射到文档 ID，使得搜索变得高效。

#### 2.1.1 倒排搜索的基本概念

**倒排索引**（Inverted Index）是一种将词项映射到包含该词项的文档的数据结构，与传统的正向索引（文档到词项的映射）相反，因此得名"倒排"。

**核心思想**：通过预先对文档进行分词处理，建立词项到文档的映射关系，当用户搜索时，直接查找包含搜索词的文档，而不是遍历所有文档。

#### 2.1.2 倒排索引的结构

倒排索引主要由以下几部分组成：

1. **词项索引（Term Index）** - 用于快速定位词项在词项字典中的位置，使用稀疏索引结构
2. **词项字典（Term Dictionary）** - 存储所有唯一词项，使用 FST（Finite State Transducer）数据结构实现
3. **倒排列表（Posting List）** - 存储每个词项对应的文档 ID 列表，可包含词频、位置等信息
4. **文档值（Doc Values）** - 列式存储的文档字段值，用于聚合和排序操作

#### 2.1.3 倒排搜索的工作原理

**倒排搜索的基本流程**：

1. **分词处理**：
   - 接收用户搜索查询
   - 使用指定的分词器对查询文本进行分词，得到搜索词项

2. **词项查找**：
   - 通过词项索引快速定位词项在词项字典中的位置
   - 在词项字典中查找对应的词项
   - 获取词项对应的倒排列表

3. **文档过滤与评分**：
   - 遍历倒排列表中的文档 ID
   - 应用查询过滤器（如范围过滤、布尔过滤等）
   - 根据词频、逆文档频率等计算文档相关性评分

4. **结果排序与返回**：
   - 根据评分对文档进行排序
   - 截取指定数量的结果
   - 返回搜索结果给用户

#### 2.1.4 倒排搜索的优势

- **快速全文搜索**：通过词项到文档的直接映射，避免了遍历所有文档
- **支持复杂查询**：可以轻松实现布尔查询、短语查询、范围查询等
- **相关性评分**：内置 TF-IDF、BM25 等评分算法，提供相关度排序
- **实时搜索**：支持近实时索引和搜索，满足实时数据分析需求
- **分布式支持**：倒排索引可以分片存储，支持水平扩展

#### 2.1.5 倒排索引的构建过程

**倒排索引的构建流程**：

1. **文档分析**：
   - 接收待索引的文档
   - 对文档进行分析，包括分词、停用词过滤、词干提取等

2. **词项处理**：
   - 对分析后的词项进行规范化处理
   - 去除重复词项
   - 记录词项在文档中的位置和频率

3. **倒排列表构建**：
   - 为每个词项创建或更新倒排列表
   - 将文档 ID、词频、位置等信息添加到倒排列表中

4. **词项字典构建**：
   - 将词项添加到词项字典中
   - 使用 FST 数据结构压缩存储词项

5. **词项索引构建**：
   - 为词项字典创建稀疏索引
   - 优化词项查找性能

#### 2.1.6 倒排搜索流程图

```
┌─────────────┐     分词处理     ┌─────────────┐
│ 用户查询    │──────────────────>│ 搜索词项    │
└─────────────┘                   └─────────────┘
        ^                              │
        │                              │
        │                              ▼
        │                    ┌─────────────┐
        │                    │ 词项查找    │
        │                    └─────────────┘
        │                              │
        │                              ▼
        │                    ┌─────────────┐
        │                    │ 倒排列表    │
        │                    └─────────────┘
        │                              │
        │                              ▼
        │                    ┌─────────────┐
        │                    │ 文档过滤    │
        │                    └─────────────┘
        │                              │
        │                              ▼
        │                    ┌─────────────┐
        │                    │ 相关性评分  │
        │                    └─────────────┘
        │                              │
        │                              ▼
        │                    ┌─────────────┐
        └────────────────────│ 结果排序    │
                             └─────────────┘
                                    │
                                    ▼
                             ┌─────────────┐
                             │ 返回结果    │
                             └─────────────┘
```

#### 2.1.7 倒排索引与正排索引的比较

| 特性 | 倒排索引 | 正排索引 |
|------|---------|----------|
| 映射关系 | 词项 → 文档 | 文档 → 词项 |
| 适用场景 | 全文搜索、相关性排序 | 文档获取、字段检索 |
| 存储空间 | 通常较小（压缩存储） | 通常较大（存储所有字段） |
| 查询性能 | 全文搜索速度快 | 文档获取速度快 |
| 构建开销 | 构建时开销较大 | 构建时开销较小 |

倒排搜索是 Elasticsearch 实现高效全文搜索的核心机制，通过精心设计的数据结构和算法，使得 Elasticsearch 能够在海量数据中快速定位相关文档，为用户提供实时、准确的搜索结果。

### 2.2 Term Index 详解

#### 2.2.1 Term Index 的基本概念

**Term Index**（词项索引）是 Elasticsearch 中用于加速词项查找的稀疏索引结构，它位于词项字典（Term Dictionary）之上，帮助快速定位词项在词项字典中的位置。

**核心作用**：当搜索词项时，Term Index 可以快速缩小搜索范围，避免线性扫描整个词项字典，从而提高搜索性能。

#### 2.2.2 Term Index 的基本原理

**工作原理**：

1. **稀疏索引**：Term Index 不是为每个词项创建索引，而是为词项字典中的部分词项创建索引点
2. **二分查找**：Term Index 内部使用二分查找结构，支持快速定位
3. **指针映射**：每个索引项存储词项的前缀和指向词项字典中对应位置的指针
4. **层级结构**：对于大型词项字典，Term Index 可能采用多层级结构

**Term Index 的查询流程**：

1. 接收搜索词项
2. 在 Term Index 中使用二分查找找到包含该词项的索引区间
3. 获取该区间在词项字典中的起始位置
4. 从词项字典的起始位置开始顺序查找，直到找到目标词项

#### 2.2.3 Term Index 的存储过程

**存储过程**：

1. **词项字典构建**：首先构建完整的词项字典，按字典序排序
2. **索引点选择**：
   - 按照一定间隔（如每 8KB 或每 1000 个词项）选择索引点
   - 为每个索引点记录词项前缀和在词项字典中的偏移量
3. **数据结构构建**：
   - 使用二分查找树或类似结构构建 Term Index
   - 优化索引结构，减少内存占用
4. **持久化存储**：
   - 将 Term Index 写入磁盘
   - 支持内存映射，提高访问速度

**存储样例**：

假设有以下词项字典：

```
apple
application
banana
book
computer
```

对应的 Term Index 可能如下：

```
Index Entry 1: "a" -> 指向词项字典中 "apple" 的位置
Index Entry 2: "b" -> 指向词项字典中 "banana" 的位置
Index Entry 3: "c" -> 指向词项字典中 "computer" 的位置
```

当搜索 "application" 时：
1. 在 Term Index 中查找，找到 "a" 索引项
2. 跳转到词项字典中 "apple" 的位置
3. 从该位置开始顺序查找，找到 "application"

#### 2.2.4 Term Index 的流程图

```
┌─────────────┐     接收搜索词项   ┌─────────────┐
│ 搜索请求    │──────────────────>│ Term Index  │
└─────────────┘                   └─────────────┘
        ^                              │
        │                              │
        │                              │ 二分查找
        │                              ▼
        │                    ┌─────────────┐
        │                    │ 查找索引点  │
        │                    └─────────────┘
        │                              │
        │                              ▼
        │                    ┌─────────────┐
        │                    │ 获取偏移量  │
        │                    └─────────────┘
        │                              │
        │                              ▼
        │                    ┌─────────────┐
        │                    │ Term Dict   │
        │                    └─────────────┘
        │                              │
        │                              ▼
        │                    ┌─────────────┐
        │                    │ 顺序查找    │
        │                    └─────────────┘
        │                              │
        │                              ▼
        │                    ┌─────────────┐
        └────────────────────│ 返回词项    │
                             └─────────────┘
```

#### 2.2.5 Term Index 的性能影响

**优势**：
- 显著减少词项查找的时间复杂度
- 支持快速的前缀查询和范围查询
- 内存占用相对较小，适合大规模词项字典

**权衡**：
- Term Index 越密集，查找速度越快，但内存占用越大
- Term Index 越稀疏，内存占用越小，但查找速度可能下降
- Elasticsearch 会根据词项字典的大小自动调整 Term Index 的密度

### 2.3 FST 数据结构详解

#### 2.3.1 FST 的基本概念

**FST**（Finite State Transducer，有限状态转换器）是一种高效的数据结构，它可以将词项字典压缩存储，同时支持快速的词项查找和前缀匹配。

**核心特点**：FST 不仅存储词项，还可以将词项映射到其他值（如词频、文档频率等），因此被称为"转换器"。

#### 2.3.2 FST 的基本原理

**工作原理**：

1. **状态机模型**：FST 是一种状态机，每个状态代表词项的一个前缀
2. **共享前缀**：相同前缀的词项共享状态，减少存储空间
3. **共享后缀**：相同后缀的词项也可以共享状态
4. **边标签**：状态之间的边用字符标签，代表词项的一个字符
5. **输出值**：每个状态可以关联一个输出值，用于存储词项的附加信息

**FST 的构建过程**：

1. **排序词项**：首先对词项按字典序排序
2. **增量构建**：逐个添加词项，共享已有的前缀和后缀
3. **状态合并**：当发现相同的状态路径时，合并状态
4. **值计算**：计算每个状态的输出值

#### 2.3.3 FST 的结构示例

假设有词项："cat"、"car"、"dog"、"dot"

对应的 FST 结构：

```
State 0 (初始状态)
├─ 'c' → State 1
│  ├─ 'a' → State 2
│  │  ├─ 't' → State 3 (输出: cat)
│  │  └─ 'r' → State 4 (输出: car)
│  └─ ...
└─ 'd' → State 5
   ├─ 'o' → State 6
   │  ├─ 'g' → State 7 (输出: dog)
   │  └─ 't' → State 8 (输出: dot)
   └─ ...
```

**空间节省**：在这个例子中，"cat"和"car"共享前缀"ca"，"dog"和"dot"共享前缀"do"，大大减少了存储空间。

#### 2.3.4 FST 的优势

- **空间效率高**：通过共享前缀和后缀，FST 的空间复杂度接近 O(n)，其中 n 是词项总长度
- **查询速度快**：词项查找的时间复杂度为 O(k)，其中 k 是词项长度
- **支持前缀查询**：可以高效实现前缀匹配和自动补全
- **支持范围查询**：可以快速找到词项字典中的范围边界
- **内存占用小**：压缩后的 FST 可以完全加载到内存中，提高访问速度

#### 2.3.5 FST 在 Elasticsearch 中的应用

- **词项字典存储**：Elasticsearch 使用 FST 存储词项字典，减少磁盘和内存占用
- **前缀查询优化**：支持高效的前缀查询，如 "app*" 匹配 "apple"、"application" 等
- **自动补全功能**：基于 FST 实现高效的自动补全建议
- **短语查询加速**：通过 FST 快速定位短语中的各个词项

#### 2.3.6 FST 的性能对比

| 数据结构 | 空间复杂度 | 时间复杂度（查找） | 支持前缀查询 | 构建时间 | 适用场景 |
|---------|-----------|-----------------|------------|---------|---------|
| FST | O(n) | O(k) | 是 | 较慢 | 词项字典、自动补全 |
| 哈希表 | O(n) | O(1) | 否 | 快 | 精确匹配 |
| 二叉搜索树 | O(n) | O(log n) | 部分支持 | 中 | 范围查询 |
| 数组 | O(n) | O(log n) (二分) | 部分支持 | 快 | 小数据集 |

FST 在空间效率和查询性能之间取得了很好的平衡，特别适合 Elasticsearch 这样需要处理大量词项的场景。

### 2.4 分词器

分词器是 Elasticsearch 中负责将文本分割成词项的组件，它对搜索结果的质量有很大影响。

#### 2.4.1 内置分词器

- **Standard Analyzer** - 标准分词器，根据 Unicode 文本分割算法分词
- **Simple Analyzer** - 简单分词器，根据非字母字符分词
- **Whitespace Analyzer** - 空白分词器，根据空白字符分词
- **Stop Analyzer** - 停用词分词器，在简单分词器的基础上去除停用词
- **Keyword Analyzer** - 关键词分词器，将整个文本作为一个词项
- **Pattern Analyzer** - 模式分词器，根据正则表达式分词
- **Language Analyzers** - 语言特定分词器，如英文、法文等
- **Fingerprint Analyzer** - 指纹分词器，用于去重

#### 2.4.2 中文分词器

在中文分词器中使用较多的是 ik 分词器：
- **ik_max_word** - 最大词长分词
- **ik_smart** - 智能分词

### 2.5 Term Dictionary 和 Posting List 详解

#### 2.5.1 Term Dictionary 的基本概念

**Term Dictionary**（词项字典）是 Elasticsearch 中存储所有唯一词项的字典结构，它按字典序排序，支持快速的词项查找。

**核心作用**：Term Dictionary 是倒排索引的核心组件之一，它将词项映射到对应的倒排列表，是词项查找的最终来源。

#### 2.5.2 Term Dictionary 的基本原理

**工作原理**：

1. **字典序排序**：所有词项按字典序排序，便于二分查找和范围查询
2. **FST 压缩**：使用 FST（Finite State Transducer）数据结构存储，减少存储空间
3. **内存映射**：支持内存映射（mmap），提高访问速度
4. **分层存储**：对于大型词项字典，可能采用分层存储结构

**Term Dictionary 的查询流程**：

1. 从 Term Index 获取词项在 Term Dictionary 中的大致位置
2. 从该位置开始，按字典序顺序查找目标词项
3. 找到词项后，获取其对应的倒排列表指针
4. 返回倒排列表

#### 2.5.3 Term Dictionary 的存储过程

**存储过程**：

1. **词项收集**：
   - 从文档中提取所有词项
   - 去除重复词项
   - 按字典序排序词项
2. **FST 构建**：
   - 使用排序后的词项构建 FST
   - 优化 FST 结构，共享前缀和后缀
   - 为每个词项关联倒排列表的指针
3. **持久化存储**：
   - 将 FST 写入磁盘
   - 生成 Term Index，加速词项查找
4. **加载优化**：
   - 支持内存映射，减少 I/O 操作
   - 缓存热点词项，提高访问速度

**存储样例**：

假设有词项："apple"、"application"、"banana"、"book"、"computer"

对应的 FST 存储结构：

```
State 0
├─ 'a' → State 1
│  ├─ 'p' → State 2
│  │  ├─ 'p' → State 3
│  │  │  ├─ 'l' → State 4
│  │  │  │  └─ 'e' → State 5 (指向 "apple" 的倒排列表)
│  │  │  └─ 'l' → State 6
│  │  │     └─ 'i' → State 7
│  │  │        └─ 'c' → State 8
│  │  │           └─ 'a' → State 9
│  │  │              └─ 't' → State 10
│  │  │                 └─ 'i' → State 11
│  │  │                    └─ 'o' → State 12
│  │  │                       └─ 'n' → State 13 (指向 "application" 的倒排列表)
│  │  └─ ...
│  └─ ...
├─ 'b' → State 14
│  ├─ 'a' → State 15
│  │  └─ 'n' → State 16
│  │     └─ 'a' → State 17
│  │        └─ 'n' → State 18
│  │           └─ 'a' → State 19 (指向 "banana" 的倒排列表)
│  └─ 'o' → State 20
│     └─ 'o' → State 21
│        └─ 'k' → State 22 (指向 "book" 的倒排列表)
└─ 'c' → State 23
   └─ 'o' → State 24
      └─ 'm' → State 25
         └─ 'p' → State 26
            └─ 'u' → State 27
               └─ 't' → State 28
                  └─ 'e' → State 29
                     └─ 'r' → State 30 (指向 "computer" 的倒排列表)
```

#### 2.5.4 Posting List 的基本概念

**Posting List**（倒排列表）是 Elasticsearch 中存储词项对应文档信息的列表结构，它包含了所有包含该词项的文档 ID 以及相关的词频、位置等信息。

**核心作用**：Posting List 是倒排索引的核心组件之一，它直接回答了"哪些文档包含某个词项"的问题。

#### 2.5.5 Posting List 的基本原理

**工作原理**：

1. **文档 ID 列表**：存储包含词项的文档 ID，按升序排列
2. **词频信息**：记录词项在每个文档中的出现次数（TF）
3. **位置信息**：记录词项在文档中的具体位置，支持短语查询
4. **偏移信息**：记录词项在文档中的字符偏移量，支持高亮显示
5. **压缩存储**：使用多种压缩算法减少存储空间

**Posting List 的查询流程**：

1. 从 Term Dictionary 获取词项对应的 Posting List
2. 遍历 Posting List 中的文档 ID
3. 应用查询过滤器，过滤不符合条件的文档
4. 根据词频、位置等信息计算文档相关性评分
5. 返回符合条件的文档 ID 和评分

#### 2.5.6 Posting List 的存储过程

**存储过程**：

1. **文档 ID 收集**：
   - 收集包含词项的所有文档 ID
   - 按升序排序文档 ID
2. **信息丰富**：
   - 为每个文档 ID 添加词频信息
   - 为每个文档 ID 添加位置信息
   - 为每个文档 ID 添加偏移信息
3. **压缩存储**：
   - 使用 FOR（Frame Of Reference）编码压缩文档 ID
   - 使用 VInt（Variable-length Integer）编码压缩词频和位置信息
   - 使用 Roaring BitMap 优化稀疏文档 ID 集合
4. **持久化存储**：
   - 将压缩后的 Posting List 写入磁盘
   - 为 Posting List 创建索引，加速访问

**存储样例**：

假设有词项 "apple" 出现在文档 ID 为 1、3、5、7、9 的文档中

对应的 Posting List 存储结构：

```
Term: "apple"
Posting List:
├─ Doc ID: 1, TF: 2, Positions: [0, 5], Offsets: [(0, 5), (10, 15)]
├─ Doc ID: 3, TF: 1, Positions: [2], Offsets: [(5, 10)]
├─ Doc ID: 5, TF: 3, Positions: [1, 4, 7], Offsets: [(2, 7), (12, 17), (20, 25)]
├─ Doc ID: 7, TF: 1, Positions: [3], Offsets: [(8, 13)]
└─ Doc ID: 9, TF: 2, Positions: [0, 6], Offsets: [(0, 5), (15, 20)]
```

#### 2.5.7 FOR 结构说明

**FOR**（Frame Of Reference）是一种高效的整数压缩编码算法，广泛应用于 Elasticsearch 的 Posting List 存储中。

**基本原理**：

1. **差值编码**：存储文档 ID 之间的差值，而不是原始文档 ID
2. **分帧存储**：将差值序列分成固定大小的帧（如 128 个整数为一帧）
3. **位宽编码**：为每一帧选择最小的位宽来存储所有差值
4. **帧头存储**：在每一帧的开头存储该帧的位宽信息

**FOR 编码的优势**：

- **高压缩率**：对于有序的整数序列，压缩率非常高
- **快速解码**：解码速度快，适合实时搜索场景
- **随机访问**：支持快速的随机访问，不需要解压整个序列
- **内存效率**：压缩后的数据占用更少的内存

**FOR 编码示例**：

原始文档 ID 序列：[1, 3, 5, 7, 9]

差值序列：[1, 2, 2, 2, 2]（第一个元素是原始值，后续是与前一个的差值）

FOR 编码过程：
1. 分帧：假设一帧大小为 5，整个序列为一帧
2. 计算位宽：最大差值为 2，需要 2 位存储
3. 编码：存储位宽 (2) + 压缩后的差值序列
4. 结果：位宽 (2) + [01, 10, 10, 10, 10]（二进制表示）

#### 2.5.8 Term Dictionary 和 Posting List 的流程图

**Term Dictionary 和 Posting List 的查询流程**：

```
┌─────────────┐     接收搜索词项   ┌─────────────┐
│ 搜索请求    │──────────────────>│ Term Index  │
└─────────────┘                   └─────────────┘
        ^                              │
        │                              │ 二分查找
        │                              ▼
        │                    ┌─────────────┐
        │                    │ 查找索引点  │
        │                    └─────────────┘
        │                              │
        │                              ▼
        │                    ┌─────────────┐
        │                    │ Term Dict   │
        │                    └─────────────┘
        │                              │
        │                              │ 顺序查找
        │                              ▼
        │                    ┌─────────────┐
        │                    │ 找到词项    │
        │                    └─────────────┘
        │                              │
        │                              ▼
        │                    ┌─────────────┐
        │                    │ Posting List│
        │                    └─────────────┘
        │                              │
        │                              ▼
        │                    ┌─────────────┐
        │                    │ 文档过滤    │
        │                    └─────────────┘
        │                              │
        │                              ▼
        │                    ┌─────────────┐
        └────────────────────│ 相关性评分  │
                             └─────────────┘
                                    │
                                    ▼
                             ┌─────────────┐
                             │ 返回结果    │
                             └─────────────┘
```

#### 2.5.9 Term Dictionary 和 Posting List 的性能优化

**Term Dictionary 优化**：
- 使用 FST 压缩存储，减少内存占用
- 实现内存映射，提高访问速度
- 缓存热点词项，加速查找

**Posting List 优化**：
- 使用 FOR 编码压缩文档 ID
- 使用 Roaring BitMap 优化稀疏文档集合
- 实现跳跃表结构，加速文档 ID 查找
- 缓存热点 Posting List，减少 I/O 操作

### 2.6 Roaring BitMap 详解

#### 2.6.1 Roaring BitMap 的基本概念

**Roaring BitMap**（ roaring位图）是一种高效的位图数据结构，用于优化稀疏集合的存储和查询。在 Elasticsearch 中，它被用于优化 Posting List 的存储，特别是对于稀疏的文档 ID 集合。

**核心作用**：Roaring BitMap 可以大幅减少 Posting List 的存储空间，同时提高文档 ID 集合的交、并、差等操作的性能。

#### 2.6.2 Roaring BitMap 的基本原理

**工作原理**：

1. **分段存储**：将 32 位整数（文档 ID）分为高 16 位和低 16 位
2. **容器选择**：
   - 对于高 16 位相同的文档 ID，使用容器存储其低 16 位
   - 根据容器中元素的密度，选择不同类型的容器
3. **容器类型**：
   - **ArrayContainer**：元素数量小于 4096 时使用，存储实际的整数数组
   - **BitmapContainer**：元素数量大于等于 4096 时使用，存储位图
   - **RunContainer**：对于连续的整数序列，使用行程长度编码存储
4. **索引结构**：使用哈希表或数组存储高 16 位到容器的映射

**Roaring BitMap 的查询流程**：

1. 接收查询的文档 ID
2. 提取文档 ID 的高 16 位，找到对应的容器
3. 在容器中查找低 16 位
4. 返回查找结果

#### 2.6.3 Roaring BitMap 的存储过程

**存储过程**：

1. **文档 ID 收集**：
   - 收集包含词项的所有文档 ID
   - 按升序排序文档 ID
2. **分段处理**：
   - 将每个文档 ID 分为高 16 位和低 16 位
   - 按高 16 位分组
3. **容器选择**：
   - 对于每个高 16 位分组，统计元素数量
   - 根据元素数量选择合适的容器类型
   - 构建对应的容器
4. **索引构建**：
   - 构建高 16 位到容器的映射
   - 优化索引结构，提高访问速度
5. **持久化存储**：
   - 将 Roaring BitMap 写入磁盘
   - 支持内存映射，提高访问速度

**存储样例**：

假设有文档 ID 序列：[1, 3, 5, 7, 9, 100000, 100002, 100004, 200000, 200001, 200002, 200003]

对应的 Roaring BitMap 存储结构：

```
Roaring BitMap:
├─ High 16 bits: 0x0000
│  └─ Container: ArrayContainer [1, 3, 5, 7, 9]
├─ High 16 bits: 0x0001
│  └─ Container: ArrayContainer [100000 & 0xFFFF, 100002 & 0xFFFF, 100004 & 0xFFFF]
└─ High 16 bits: 0x0002
   └─ Container: RunContainer [(200000 & 0xFFFF, 3)]  // 表示从 200000 开始的 4 个连续整数
```

#### 2.6.4 Roaring BitMap 在 Posting List 中的应用

**应用场景**：

1. **稀疏 Posting List**：对于文档频率较低的词项，Roaring BitMap 可以大幅减少存储空间
2. **集合操作**：加速文档 ID 集合的交、并、差等操作，如布尔查询
3. **范围查询**：优化文档 ID 范围查询的性能
4. **聚合操作**：加速基于文档 ID 的聚合操作

**性能优势**：

- **存储空间**：相比传统的 FOR 编码，Roaring BitMap 对于稀疏集合的压缩率更高
- **查询速度**：支持快速的随机访问和集合操作
- **内存效率**：压缩后的数据占用更少的内存，适合缓存
- **扩展性**：对于大规模文档 ID 集合，性能表现稳定

#### 2.6.5 Roaring BitMap 的操作示例

**交操作**（AND）：

```
Bitmap1: {1, 3, 5, 7, 9}
Bitmap2: {3, 6, 9, 12}
Result: {3, 9}
```

**并操作**（OR）：

```
Bitmap1: {1, 3, 5, 7, 9}
Bitmap2: {3, 6, 9, 12}
Result: {1, 3, 5, 6, 7, 9, 12}
```

**差操作**（NOT）：

```
Bitmap1: {1, 3, 5, 7, 9}
Bitmap2: {3, 6, 9, 12}
Result: {1, 5, 7}
```

#### 2.6.6 Roaring BitMap 的流程图

**Roaring BitMap 的查询流程**：

```
┌─────────────┐     接收文档 ID   ┌─────────────┐
│ 查询请求    │──────────────────>│ 提取高 16 位 │
└─────────────┘                   └─────────────┘
        ^                              │
        │                              │
        │                              ▼
        │                    ┌─────────────┐
        │                    │ 查找容器    │
        │                    └─────────────┘
        │                              │
        │                              ▼
        │                    ┌─────────────┐
        │                    │ 提取低 16 位 │
        │                    └─────────────┘
        │                              │
        │                              ▼
        │                    ┌─────────────┐
        │                    │ 容器内查找  │
        │                    └─────────────┘
        │                              │
        │                              ▼
        │                    ┌─────────────┐
        └────────────────────│ 返回结果    │
                             └─────────────┘
```

**Roaring BitMap 的集合操作流程**：

```
┌─────────────┐     接收两个位图  ┌─────────────┐
│ 集合操作请求 │──────────────────>│ 提取容器   │
└─────────────┘                   └─────────────┘
        ^                              │
        │                              │
        │                              ▼
        │                    ┌─────────────┐
        │                    │ 容器匹配   │
        │                    └─────────────┘
        │                              │
        │                              ▼
        │                    ┌─────────────┐
        │                    │ 容器操作   │
        │                    └─────────────┘
        │                              │
        │                              ▼
        │                    ┌─────────────┐
        │                    │ 结果合并   │
        │                    └─────────────┘
        │                              │
        │                              ▼
        │                    ┌─────────────┐
        └────────────────────│ 返回结果    │
                             └─────────────┘
```

#### 2.6.7 Roaring BitMap 与其他压缩算法的比较

| 压缩算法 | 存储空间 | 查询速度 | 集合操作速度 | 适用场景 |
|---------|---------|---------|------------|----------|
| Roaring BitMap | 低 | 高 | 高 | 稀疏文档 ID 集合 |
| FOR 编码 | 中 | 中 | 低 | 密集文档 ID 集合 |
| VInt 编码 | 高 | 低 | 低 | 小范围文档 ID 集合 |
| 简单位图 | 高 | 高 | 高 | 密集文档 ID 集合 |

### 2.7 完整搜索流程示例

#### 2.7.1 样例数据

**创建示例文档**：

```json
// 文档 1
{
  "id": 1,
  "title": "Apple iPhone 13 Review",
  "content": "The Apple iPhone 13 is a great smartphone with a powerful camera and long battery life.",
  "category": "electronics",
  "price": 799
}

// 文档 2
{
  "id": 2,
  "title": "Banana Bread Recipe",
  "content": "This banana bread recipe is easy to make and tastes delicious with fresh bananas.",
  "category": "food",
  "price": 0
}

// 文档 3
{
  "id": 3,
  "title": "Apple MacBook Pro 2023",
  "content": "The new Apple MacBook Pro features the M2 chip for exceptional performance.",
  "category": "electronics",
  "price": 1299
}

// 文档 4
{
  "id": 4,
  "title": "Book Review: The Great Gatsby",
  "content": "The Great Gatsby is a classic novel that explores themes of wealth and love.",
  "category": "books",
  "price": 15
}

// 文档 5
{
  "id": 5,
  "title": "Computer Science Basics",
  "content": "Learn the fundamentals of computer science including algorithms and data structures.",
  "category": "education",
  "price": 29
}
```

#### 2.7.2 索引过程

**1. 文档分析**：

对每个文档进行分词处理，以文档 1 为例：

- 标题："Apple iPhone 13 Review" → ["apple", "iphone", "13", "review"]
- 内容："The Apple iPhone 13 is a great smartphone with a powerful camera and long battery life." → ["the", "apple", "iphone", "13", "is", "a", "great", "smartphone", "with", "a", "powerful", "camera", "and", "long", "battery", "life"]

**2. 词项处理**：

- 去除停用词：["the", "is", "a", "with", "and"]
- 词干提取：保持原样（示例中使用标准分词器）
- 小写转换：所有词项转为小写

处理后的词项：["apple", "iphone", "13", "review", "great", "smartphone", "powerful", "camera", "long", "battery", "life"]

**3. 倒排索引构建**：

- **Term Index 构建**：为词项创建稀疏索引
- **Term Dictionary 构建**：使用 FST 存储所有词项
- **Posting List 构建**：为每个词项创建倒排列表

**部分倒排索引结构**：

```
Term: "apple"
Posting List: [1, 3]  // 出现在文档 1 和 3 中

Term: "iphone"
Posting List: [1]  // 只出现在文档 1 中

Term: "banana"
Posting List: [2]  // 只出现在文档 2 中

Term: "book"
Posting List: [4]  // 只出现在文档 4 中

Term: "computer"
Posting List: [5]  // 只出现在文档 5 中
```

**4. 存储优化**：

- 使用 FOR 编码压缩文档 ID
- 使用 Roaring BitMap 优化稀疏 Posting List
- 使用 FST 压缩 Term Dictionary

#### 2.7.3 搜索过程

**搜索查询**："apple computer"

**1. 查询分析**：

- 分词处理："apple computer" → ["apple", "computer"]
- 去除停用词：无
- 小写转换：["apple", "computer"]

**2. 词项查找**：

- **查找 "apple"**：
  1. 在 Term Index 中二分查找 "apple"
  2. 获取在 Term Dictionary 中的位置
  3. 在 Term Dictionary 中找到 "apple"
  4. 获取对应的 Posting List：[1, 3]

- **查找 "computer"**：
  1. 在 Term Index 中二分查找 "computer"
  2. 获取在 Term Dictionary 中的位置
  3. 在 Term Dictionary 中找到 "computer"
  4. 获取对应的 Posting List：[5]

**3. 集合操作**：

- 执行 AND 操作：[1, 3] ∩ [5] = []
- 由于没有文档同时包含两个词项，返回空结果

**修改查询**："apple"

**重新执行搜索**：

- 查找 "apple" 的 Posting List：[1, 3]
- 加载文档 1 和 3
- 计算相关性评分
- 排序并返回结果

#### 2.7.4 完整搜索流程图

**Elasticsearch 完整搜索流程**：

```
┌─────────────┐     接收搜索请求   ┌─────────────┐
│ 客户端      │──────────────────>│ 协调节点   │
└─────────────┘                   └─────────────┘
        ^                              │
        │                              │ 转发请求
        │                              ▼
        │                    ┌─────────────┐
        │                    │ 数据节点    │
        │                    └─────────────┘
        │                              │
        │                              ▼
        │                    ┌─────────────┐
        │                    │ 查询分析    │
        │                    └─────────────┘
        │                              │
        │                              ▼
        │                    ┌─────────────┐
        │                    │ 词项查找    │
        │                    └─────────────┘
        │                              │
        │                              ▼
        │                    ┌─────────────┐
        │                    │ Term Index  │
        │                    └─────────────┘
        │                              │
        │                              ▼
        │                    ┌─────────────┐
        │                    │ Term Dict   │
        │                    └─────────────┘
        │                              │
        │                              ▼
        │                    ┌─────────────┐
        │                    │ Posting List│
        │                    └─────────────┘
        │                              │
        │                              ▼
        │                    ┌─────────────┐
        │                    │ 集合操作    │
        │                    └─────────────┘
        │                              │
        │                              ▼
        │                    ┌─────────────┐
        │                    │ 文档加载    │
        │                    └─────────────┘
        │                              │
        │                              ▼
        │                    ┌─────────────┐
        │                    │ 相关性评分  │
        │                    └─────────────┘
        │                              │
        │                              ▼
        │                    ┌─────────────┐
        │                    │ 结果排序    │
        │                    └─────────────┘
        │                              │
        │                              ▼
        │                    ┌─────────────┐
        │                    │ 结果汇总    │
        │                    └─────────────┘
        │                              │
        │                              ▼
        └────────────────────│ 返回结果    │
                             └─────────────┘
```

#### 2.7.5 搜索结果示例

**搜索查询**："apple"

**返回结果**：

```json
{
  "took": 2,
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": {
      "value": 2,
      "relation": "eq"
    },
    "max_score": 0.6931472,
    "hits": [
      {
        "_index": "products",
        "_id": "1",
        "_score": 0.6931472,
        "_source": {
          "id": 1,
          "title": "Apple iPhone 13 Review",
          "content": "The Apple iPhone 13 is a great smartphone with a powerful camera and long battery life.",
          "category": "electronics",
          "price": 799
        }
      },
      {
        "_index": "products",
        "_id": "3",
        "_score": 0.5753642,
        "_source": {
          "id": 3,
          "title": "Apple MacBook Pro 2023",
          "content": "The new Apple MacBook Pro features the M2 chip for exceptional performance.",
          "category": "electronics",
          "price": 1299
        }
      }
    ]
  }
}
```

#### 2.7.6 性能优化点

**1. 词项查找优化**：
- 使用 Term Index 加速词项查找
- 缓存热点词项，减少磁盘 I/O

**2. Posting List 优化**：
- 使用 FOR 编码压缩密集文档 ID 集合
- 使用 Roaring BitMap 优化稀疏文档 ID 集合
- 缓存热点 Posting List，提高访问速度

**3. 查询执行优化**：
- 优先执行高选择性的查询条件
- 使用过滤器缓存，加速重复查询
- 优化集合操作，减少内存使用

**4. 结果处理优化**：
- 使用 Doc Values 加速排序和聚合操作
- 限制返回结果数量，减少网络传输
- 使用字段选择，只返回需要的字段

### 2.8 节点角色

Elasticsearch 集群中的节点可以扮演不同的角色：

#### 2.8.1 Master Node

- 负责集群的管理和协调
- 处理索引的创建和删除
- 维护集群状态
- 选举主节点

#### 2.8.2 Data Node

- 存储索引数据
- 处理文档的增删改查操作
- 执行搜索和聚合操作

#### 2.8.3 Coordinate Node

- 接收用户请求
- 转发请求到对应的节点
- 汇总结果并返回给用户

#### 2.8.4 Ingest Node

- 拦截请求
- 对文档进行转换和预处理
- 支持定义管道（Pipeline）来处理文档

## 第三部分：Elasticsearch 工作原理

### 3.1 写入过程

（1）客户端发送写请求给 coordinate node
（2）coordinate node 计算路由，转发到对应的主分片所在的节点上
（3）主分片节点处理请求，并将数据同步至副本分片节点
（4）当所有节点写完数据后，coordinate node 返回相应给客户端

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/288a2007b0804cdb848d5a51e30d83be~tplv-k3u1fbpfcp-zoom-in-crop-mark:1512:0:0:0.awebp?)

### 3.2 查询过程

（1）客户端发送查询请求给 coordinate node
（2）coordinate node 转发请求到相关的分片（主分片或副本分片）
（3）每个分片执行查询并返回结果给 coordinate node
（4）coordinate node 汇总结果并返回给客户端

![](https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/b3578ff2b7e24f51beec62cf70fea620~tplv-k3u1fbpfcp-zoom-in-crop-mark:1512:0:0:0.awebp?)

### 3.3 主节点选举过程

当集群中没有主节点或者主节点不可用时，会触发主节点选举过程：

1. **发现阶段** - 节点通过集群发现机制找到其他节点
2. **选举阶段** - 节点之间通过投票选举出主节点
3. **发布阶段** - 新的主节点发布集群状态

主节点选举基于 Bully 算法，优先级如下：
- 节点 ID 大的优先
- 集群状态版本高的优先
- 加入集群时间早的优先

### 3.4 集群扩展

当集群中的数据量增加时，可以通过以下方式扩展集群：

1. **增加节点** - 向集群中添加新的节点
2. **分片重平衡** - Elasticsearch 会自动将分片均匀分布到所有节点上
3. **水平扩展** - 通过增加节点来提高集群的处理能力

## 第四部分：Elasticsearch 高级特性

### 4.1 映射（Mapping）

映射是索引中文档的结构定义，类似于关系型数据库中的表结构。映射定义了字段的类型、分词器、索引方式等属性。

#### 4.1.1 字段类型

- **核心类型** - text、keyword、number、date、boolean、binary
- **复合类型** - object、nested
- **地理类型** - geo_point、geo_shape
- **特殊类型** - ip、completion、token_count

### 4.2 查询 DSL

Elasticsearch 提供了丰富的查询 DSL（Domain Specific Language），用于构建复杂的查询。

#### 4.2.1 查询类型

- **全文查询** - match、match_phrase、multi_match
- ** term 查询** - term、terms、range、exists
- **复合查询** - bool、function_score、dis_max
- **聚合查询** - terms、range、date_histogram

### 4.3 聚合

聚合是 Elasticsearch 中用于数据分析的功能，它可以对数据进行分组、计算等操作。

#### 4.3.1 聚合类型

- **桶聚合** - 对数据进行分组
- **指标聚合** - 对数据进行计算
- **管道聚合** - 对聚合结果进行再聚合

### 4.4 快照和恢复

快照和恢复是 Elasticsearch 中用于备份和恢复数据的功能。

#### 4.4.1 快照操作

```bash
# 创建快照
PUT /_snapshot/my_backup/snapshot_1

# 查看快照
GET /_snapshot/my_backup/snapshot_1

# 删除快照
DELETE /_snapshot/my_backup/snapshot_1
```

#### 4.4.2 恢复操作

```bash
# 恢复快照
POST /_snapshot/my_backup/snapshot_1/_restore

# 查看恢复状态
GET /_recovery
```

## 第五部分：Elasticsearch 最佳实践

### 5.1 索引设计

#### 5.1.1 索引命名

- 使用有意义的名称
- 包含时间信息（如 logs-2024.01.01）
- 避免使用特殊字符

#### 5.1.2 分片设计

- 根据数据量和节点数确定分片数
- 分片大小建议在 20-40GB 之间
- 考虑未来的扩展性

### 5.2 性能优化

#### 5.2.1 写入性能优化

- 使用批量 API
- 合理设置 refresh_interval
- 关闭不需要的副本
- 使用自动生成的文档 ID

#### 5.2.2 查询性能优化

- 使用合适的分词器
- 避免使用深度嵌套查询
- 使用过滤器缓存
- 合理设置查询超时

### 5.3 监控和运维

#### 5.3.1 监控指标

- 集群健康状态
- 节点 CPU、内存、磁盘使用情况
- 索引大小和文档数
- 查询延迟和吞吐量

#### 5.3.2 常见问题

- 集群状态为 yellow 或 red
- 节点磁盘空间不足
- 查询速度慢
- 写入失败

## 第六部分：Elasticsearch 面试题解析

### 6.1 基础概念

#### 6.1.1 Elasticsearch 和 Lucene 的关系是什么？

**答案**：Elasticsearch 是基于 Lucene 构建的分布式搜索和分析引擎，它封装了 Lucene 的复杂性，提供了 RESTful API 接口，支持分布式部署和水平扩展。Lucene 是一个开源的全文搜索引擎库，它提供了核心的索引和搜索功能，但不支持分布式部署。

#### 6.1.2 Elasticsearch 的核心数据结构是什么？

**答案**：Elasticsearch 的核心数据结构是倒排索引，它将文档中的词项映射到文档 ID，使得搜索变得高效。倒排索引主要由词项字典和倒排列表组成，词项字典使用 FST 数据结构实现，倒排列表存储每个词项对应的文档 ID 列表。

### 6.2 架构设计

#### 6.2.1 Elasticsearch 集群中的节点有哪些角色？

**答案**：Elasticsearch 集群中的节点可以扮演以下角色：
- **Master Node** - 负责集群的管理和协调
- **Data Node** - 存储索引数据，处理文档的增删改查操作
- **Coordinate Node** - 接收用户请求，转发请求到对应的节点，汇总结果并返回给用户
- **Ingest Node** - 拦截请求，对文档进行转换和预处理

#### 6.2.2 Elasticsearch 如何实现高可用性？

**答案**：Elasticsearch 通过以下方式实现高可用性：
- **副本机制** - 每个分片可以有多个副本，当主分片不可用时，副本可以升级为主分片
- **节点故障转移** - 当节点不可用时，集群会自动重新分配分片
- **集群状态管理** - 主节点负责维护集群状态，确保集群的一致性

### 6.3 性能优化

#### 6.3.1 Elasticsearch 索引数据多了怎么办？如何调优？

**答案**：当 Elasticsearch 索引数据多时，可以通过以下方式调优：
- **分片策略** - 根据数据量和节点数合理设置分片数
- **索引生命周期管理** - 使用 ILM 自动管理索引的生命周期
- **冷热分离** - 将热数据存储在快速存储上，冷数据存储在慢速存储上
- **查询优化** - 使用合适的查询方式，避免深度嵌套查询
- **硬件升级** - 增加节点的 CPU、内存、磁盘空间

#### 6.3.2 Elasticsearch 对于大数据量（上亿量级）的聚合如何实现？

**答案**：对于大数据量的聚合，Elasticsearch 可以通过以下方式实现：
- **使用近似聚合** - 对于不需要精确结果的场景，使用近似聚合如 cardinality
- **设置合理的分片数** - 分片数过多或过少都会影响聚合性能
- **使用路由** - 通过路由将相关数据集中在少数分片上
- **预聚合** - 在写入时进行预聚合，减少查询时的计算量
- **增加内存** - 聚合操作需要大量内存，确保节点有足够的内存

### 6.4 实际应用

#### 6.4.1 Elasticsearch 在日志系统中的应用

**答案**：Elasticsearch 在日志系统中的应用主要包括：
- **日志收集** - 使用 Filebeat、Logstash 等工具收集日志
- **日志存储** - 将日志存储在 Elasticsearch 中
- **日志分析** - 使用 Kibana 可视化分析日志
- **日志告警** - 基于日志内容设置告警规则

#### 6.4.2 Elasticsearch 如何与关系型数据库集成？

**答案**：Elasticsearch 可以通过以下方式与关系型数据库集成：
- **使用 Logstash JDBC 插件** - 定期从数据库中同步数据
- **使用 Change Data Capture** - 捕获数据库的变更并同步到 Elasticsearch
- **使用第三方工具** - 如 Debezium、Maxwell 等
- **应用程序双写** - 在写入数据库的同时写入 Elasticsearch

## 第七部分：参考链接

### 7.1 官方文档

- [Elasticsearch 官方文档](https://www.elastic.co/guide/en/elasticsearch/reference/current/index.html)
- [Elasticsearch 中文文档](https://www.elastic.co/guide/cn/elasticsearch/guide/current/index.html)
- [Lucene 官方文档](https://lucene.apache.org/core/documentation.html)

### 7.2 学习资源

- [Elasticsearch 权威指南](https://www.elastic.co/guide/cn/elasticsearch/guide/current/index.html)
- [Elasticsearch 实战](https://www.amazon.cn/dp/B07P8XSYGX)
- [Elasticsearch 技术解析与实战](https://www.amazon.cn/dp/B07V3L9Q7Q)

### 7.3 社区资源

- [Elastic 社区](https://discuss.elastic.co/)
- [Elasticsearch GitHub](https://github.com/elastic/elasticsearch)
- [Elasticsearch 中文社区](https://elasticsearch.cn/)

### 7.4 工具和插件

- [Kibana](https://www.elastic.co/products/kibana)
- [Logstash](https://www.elastic.co/products/logstash)
- [Filebeat](https://www.elastic.co/products/beats/filebeat)
- [Elasticsearch Analysis IK](https://github.com/medcl/elasticsearch-analysis-ik)

### 7.5 视频教程

- [Elasticsearch 核心技术 - B 站](https://www.bilibili.com/video/BV1TR4y1Q7kd/)
- [Elasticsearch 实战教程 - B 站](https://www.bilibili.com/video/BV1oE411B7SU/)

### 7.6 博客和文章

- [Elasticsearch 知识点汇总](https://juejin.cn/column/7026888535719886885)
- [Elasticsearch 解析](https://juejin.cn/post/7026902638176239653)
- [Elasticsearch 性能优化实战](https://cloud.tencent.com/developer/article/2141868)