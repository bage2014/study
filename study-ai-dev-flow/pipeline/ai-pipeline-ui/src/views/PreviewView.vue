<template>
  <div class="space-y-6">
    <header class="flex items-start justify-between">
      <div>
        <p class="text-2xs font-medium uppercase tracking-wide text-ink-muted mb-1">Stage 08</p>
        <h1 class="text-2xl font-semibold tracking-tight text-ink">变更预览</h1>
        <p class="text-ink-secondary mt-1 text-sm">查看代码变更差异和风险评估</p>
      </div>
      <span class="tag bg-accent-soft text-accent">Diff 视图</span>
    </header>

    <section class="card">
      <div class="px-5 py-3 border-b border-line flex flex-wrap gap-1.5">
        <button
          v-for="file in changedFiles"
          :key="file.name"
          @click="selectFile(file)"
          :class="[
            'mono text-xs px-2 py-1 rounded border transition-colors inline-flex items-center gap-1.5',
            selectedFile?.name === file.name
              ? 'border-accent bg-accent-soft text-accent'
              : 'border-line text-ink-secondary hover:border-line-strong'
          ]"
        >
          <span :class="file.type === 'added' ? 'text-ok' : file.type === 'modified' ? 'text-warn' : 'text-danger'">
            {{ file.type === 'added' ? '+' : file.type === 'modified' ? '~' : '-' }}
          </span>
          <span>{{ file.name }}</span>
        </button>
      </div>

      <div class="border-t border-line">
        <div class="flex items-center justify-between px-5 py-2.5 bg-base border-b border-line">
          <span class="mono text-xs text-ink">{{ selectedFile?.path }}</span>
          <span :class="['tag', typeTagClass(selectedFile?.type)]">{{ typeText(selectedFile?.type) }}</span>
        </div>
        <div class="bg-ink">
          <pre class="mono text-xs text-white/90 p-4 overflow-x-auto max-h-96 overflow-y-auto">{{ selectedFile?.diff }}</pre>
        </div>
      </div>
    </section>

    <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
      <section class="card">
        <h2 class="px-5 py-3 border-b border-line text-sm font-medium text-ink">变更统计</h2>
        <ul class="divide-y divide-line">
          <li class="px-5 py-3 flex items-center justify-between text-sm">
            <span class="flex items-center gap-2.5 text-ink-secondary">
              <span class="w-1.5 h-1.5 rounded-full bg-ok"></span>新增文件
            </span>
            <span class="mono text-ink">2</span>
          </li>
          <li class="px-5 py-3 flex items-center justify-between text-sm">
            <span class="flex items-center gap-2.5 text-ink-secondary">
              <span class="w-1.5 h-1.5 rounded-full bg-warn"></span>修改文件
            </span>
            <span class="mono text-ink">2</span>
          </li>
          <li class="px-5 py-3 flex items-center justify-between text-sm">
            <span class="flex items-center gap-2.5 text-ink-secondary">
              <span class="w-1.5 h-1.5 rounded-full bg-danger"></span>删除文件
            </span>
            <span class="mono text-ink">0</span>
          </li>
          <li class="px-5 py-3 flex items-center justify-between text-sm">
            <span class="flex items-center gap-2.5 text-ink-secondary">
              <span class="w-1.5 h-1.5 rounded-full bg-accent"></span>新增行数
            </span>
            <span class="mono text-ok">+156</span>
          </li>
          <li class="px-5 py-3 flex items-center justify-between text-sm">
            <span class="flex items-center gap-2.5 text-ink-secondary">
              <span class="w-1.5 h-1.5 rounded-full bg-line-strong"></span>删除行数
            </span>
            <span class="mono text-ink">-0</span>
          </li>
        </ul>
      </section>

      <section class="card">
        <h2 class="px-5 py-3 border-b border-line text-sm font-medium text-ink">影响范围</h2>
        <ul class="divide-y divide-line">
          <li class="px-5 py-3 flex items-center justify-between text-sm">
            <span class="mono text-xs text-ink">Controller</span>
            <span class="tag bg-ok-soft text-ok">新增</span>
          </li>
          <li class="px-5 py-3 flex items-center justify-between text-sm">
            <span class="mono text-xs text-ink">Service</span>
            <span class="tag bg-ok-soft text-ok">新增</span>
          </li>
          <li class="px-5 py-3 flex items-center justify-between text-sm">
            <span class="mono text-xs text-ink">DTO</span>
            <span class="tag bg-ok-soft text-ok">新增</span>
          </li>
          <li class="px-5 py-3 flex items-center justify-between text-sm">
            <span class="mono text-xs text-ink">Repository</span>
            <span class="tag bg-warn-soft text-warn">修改</span>
          </li>
          <li class="px-5 py-3 flex items-center justify-between text-sm">
            <span class="mono text-xs text-ink">配置文件</span>
            <span class="tag bg-base text-ink-muted">无变更</span>
          </li>
        </ul>
      </section>

      <section class="card">
        <h2 class="px-5 py-3 border-b border-line text-sm font-medium text-ink">风险评估</h2>
        <div class="px-5 py-4 space-y-3">
          <div class="flex items-center justify-between text-sm">
            <span class="text-ink-secondary">整体风险</span>
            <span class="tag bg-ok-soft text-ok">低</span>
          </div>
          <div class="w-full bg-line rounded-full h-1">
            <div class="bg-ok h-1 rounded-full" style="width: 20%"></div>
          </div>
          <ul class="space-y-1.5 pt-2">
            <li class="flex items-start gap-2 text-xs text-ink-secondary">
              <span class="w-1.5 h-1.5 rounded-full bg-ok flex-shrink-0 mt-1"></span>无破坏性变更
            </li>
            <li class="flex items-start gap-2 text-xs text-ink-secondary">
              <span class="w-1.5 h-1.5 rounded-full bg-ok flex-shrink-0 mt-1"></span>向后兼容
            </li>
            <li class="flex items-start gap-2 text-xs text-ink-secondary">
              <span class="w-1.5 h-1.5 rounded-full bg-accent flex-shrink-0 mt-1"></span>建议进行回归测试
            </li>
          </ul>
        </div>
      </section>
    </div>

    <section class="card">
      <h2 class="px-5 py-3 border-b border-line text-sm font-medium text-ink">变更摘要</h2>
      <dl class="px-5 py-4 grid grid-cols-1 md:grid-cols-2 gap-x-8 gap-y-2 text-sm">
        <div class="flex justify-between"><dt class="text-ink-muted">变更类型</dt><dd class="text-ink">功能新增</dd></div>
        <div class="flex justify-between"><dt class="text-ink-muted">变更日期</dt><dd class="mono text-xs text-ink">2026-07-11 23:20:35</dd></div>
        <div class="flex justify-between"><dt class="text-ink-muted">影响模块</dt><dd class="mono text-xs text-ink">controller, service, dto, repository</dd></div>
        <div class="flex justify-between"><dt class="text-ink-muted">分支</dt><dd class="mono text-xs text-ink">ai-run-a9b79feb</dd></div>
        <div class="md:col-span-2">
          <dt class="text-ink-muted mb-1">变更描述</dt>
          <dd class="text-ink-secondary">添加健康检查端点，返回服务状态信息</dd>
        </div>
      </dl>
    </section>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const changedFiles = ref([
  {
    name: 'HealthController.java',
    path: 'src/main/java/com/example/demo/controller/HealthController.java',
    type: 'added',
    diff: `+package com.example.demo.controller;
+
+import com.example.demo.service.HealthService;
+import lombok.RequiredArgsConstructor;
+import org.springframework.http.ResponseEntity;
+import org.springframework.web.bind.annotation.GetMapping;
+import org.springframework.web.bind.annotation.RequestMapping;
+import org.springframework.web.bind.annotation.RestController;
+
+/**
+ * 健康检查控制器
+ */
+@RestController
+@RequestMapping("/api/health")
+@RequiredArgsConstructor
+public class HealthController {
+
+    private final HealthService healthService;
+
+    /**
+     * 健康检查端点
+     */
+    @GetMapping
+    public ResponseEntity<?> health() {
+        return ResponseEntity.ok(healthService.getHealthStatus());
+    }
+
+    /**
+     * 详细健康状态
+     */
+    @GetMapping("/detail")
+    public ResponseEntity<?> healthDetail() {
+        return ResponseEntity.ok(healthService.getHealthDetail());
+    }
+}`
  },
  {
    name: 'HealthService.java',
    path: 'src/main/java/com/example/demo/service/HealthService.java',
    type: 'added',
    diff: `+package com.example.demo.service;
+
+import lombok.RequiredArgsConstructor;
+import lombok.extern.slf4j.Slf4j;
+import org.springframework.stereotype.Service;
+
+import java.time.LocalDateTime;
+import java.util.HashMap;
+import java.util.Map;
+
+/**
+ * 健康检查服务
+ */
+@Slf4j
+@Service
+@RequiredArgsConstructor
+public class HealthService {
+
+    public Map<String, Object> getHealthStatus() {
+        Map<String, Object> status = new HashMap<>();
+        status.put("status", "UP");
+        status.put("timestamp", LocalDateTime.now().toString());
+        return status;
+    }
+
+    public Map<String, Object> getHealthDetail() {
+        Map<String, Object> detail = new HashMap<>();
+        detail.put("status", "UP");
+        detail.put("timestamp", LocalDateTime.now().toString());
+        detail.put("service", "demo-backend");
+        detail.put("version", "1.0.0");
+        return detail;
+    }
+}`
  },
  {
    name: 'HealthResponse.java',
    path: 'src/main/java/com/example/demo/dto/HealthResponse.java',
    type: 'modified',
    diff: `-package com.example.demo.dto;
+package com.example.demo.dto;
+
 import lombok.AllArgsConstructor;
 import lombok.Builder;
 import lombok.Data;
 import lombok.NoArgsConstructor;
 
+import java.time.LocalDateTime;
+
 /**
  * 健康检查响应 DTO
  */
 @Data
 @Builder
 @NoArgsConstructor
 @AllArgsConstructor
 public class HealthResponse {
-
-    private String status;
+    /**
+     * 服务状态
+     */
+    private String status;
+
+    /**
+     * 时间戳
+     */
+    private LocalDateTime timestamp;
+
+    /**
+     * 服务名称
+     */
+    private String service;
+
+    /**
+     * 版本号
+     */
+    private String version;
 }`
  },
  {
    name: 'HealthRepository.java',
    path: 'src/main/java/com/example/demo/repository/HealthRepository.java',
    type: 'modified',
    diff: `-package com.example.demo.repository;
+package com.example.demo.repository;
+
 import org.springframework.data.jpa.repository.JpaRepository;
+import org.springframework.stereotype.Repository;
+
+/**
+ * 健康检查数据访问接口
+ */
+@Repository
 public interface HealthRepository extends JpaRepository<Object, Long> {
 }`
  },
])

const selectedFile = ref(changedFiles.value[0])

const selectFile = (file) => {
  selectedFile.value = file
}

const typeTagClass = (t) => ({
  added: 'bg-ok-soft text-ok',
  modified: 'bg-warn-soft text-warn',
  deleted: 'bg-danger-soft text-danger'
}[t] || 'bg-base text-ink-secondary')

const typeText = (t) => ({
  added: '新增',
  modified: '修改',
  deleted: '删除'
}[t] || t)
</script>
