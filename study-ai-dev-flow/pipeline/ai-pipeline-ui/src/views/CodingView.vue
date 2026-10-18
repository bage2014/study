<template>
  <div class="space-y-6">
    <header class="flex items-start justify-between">
      <div>
        <p class="text-2xs font-medium uppercase tracking-wide text-ink-muted mb-1">Stage 05</p>
        <h1 class="text-2xl font-semibold tracking-tight text-ink">编码实现</h1>
        <p class="text-ink-secondary mt-1 text-sm">AI 根据技术方案自动生成代码实现</p>
      </div>
      <span class="tag bg-ok-soft text-ok">已完成</span>
    </header>

    <section class="card">
      <div class="px-5 py-3 border-b border-line flex flex-wrap gap-1.5">
        <button
          v-for="file in files"
          :key="file.name"
          @click="selectFile(file)"
          :class="[
            'mono text-xs px-2 py-1 rounded border transition-colors',
            selectedFile?.name === file.name
              ? 'border-accent bg-accent-soft text-accent'
              : 'border-line text-ink-secondary hover:border-line-strong'
          ]"
        >{{ file.name }}</button>
      </div>
      <div class="bg-ink">
        <div class="flex items-center justify-between px-4 py-2 border-b border-white/10">
          <span class="mono text-xs text-white/60">{{ selectedFile?.path }}</span>
          <span class="text-2xs font-medium uppercase tracking-wide text-ok">已生成</span>
        </div>
        <pre class="mono text-xs text-white/90 p-4 overflow-x-auto max-h-96 overflow-y-auto">{{ selectedFile?.content }}</pre>
      </div>
    </section>

    <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
      <section class="card">
        <h2 class="px-5 py-3 border-b border-line text-sm font-medium text-ink">代码统计</h2>
        <dl class="px-5 py-4 space-y-2 text-sm">
          <div class="flex justify-between"><dt class="text-ink-muted">总文件数</dt><dd class="mono text-ink">4</dd></div>
          <div class="flex justify-between"><dt class="text-ink-muted">总行数</dt><dd class="mono text-ink">156</dd></div>
          <div class="flex justify-between"><dt class="text-ink-muted">代码行数</dt><dd class="mono text-ink">128</dd></div>
          <div class="flex justify-between"><dt class="text-ink-muted">注释行数</dt><dd class="mono text-ink-secondary">28</dd></div>
          <div class="flex justify-between"><dt class="text-ink-muted">空行数</dt><dd class="mono text-ink-secondary">0</dd></div>
        </dl>
      </section>

      <section class="card">
        <h2 class="px-5 py-3 border-b border-line text-sm font-medium text-ink">代码质量</h2>
        <div class="px-5 py-4 space-y-4">
          <div>
            <div class="flex items-center justify-between mb-1.5 text-sm">
              <span class="text-ink-secondary">代码覆盖率</span>
              <span class="mono text-xs text-ink">85%</span>
            </div>
            <div class="w-full bg-line rounded-full h-1">
              <div class="bg-ok h-1 rounded-full" style="width: 85%"></div>
            </div>
          </div>
          <div>
            <div class="flex items-center justify-between mb-1.5 text-sm">
              <span class="text-ink-secondary">复杂度</span>
              <span class="mono text-xs text-ink">低</span>
            </div>
            <div class="w-full bg-line rounded-full h-1">
              <div class="bg-ok h-1 rounded-full" style="width: 30%"></div>
            </div>
          </div>
          <div>
            <div class="flex items-center justify-between mb-1.5 text-sm">
              <span class="text-ink-secondary">重复率</span>
              <span class="mono text-xs text-ink">5%</span>
            </div>
            <div class="w-full bg-line rounded-full h-1">
              <div class="bg-ok h-1 rounded-full" style="width: 5%"></div>
            </div>
          </div>
          <div>
            <div class="flex items-center justify-between mb-1.5 text-sm">
              <span class="text-ink-secondary">规范评分</span>
              <span class="mono text-xs text-ink">92</span>
            </div>
            <div class="w-full bg-line rounded-full h-1">
              <div class="bg-accent h-1 rounded-full" style="width: 92%"></div>
            </div>
          </div>
        </div>
      </section>
    </div>

    <section class="card">
      <h2 class="px-5 py-3 border-b border-line text-sm font-medium text-ink">代码审查</h2>
      <ul class="divide-y divide-line">
        <li class="px-5 py-4 flex items-start gap-3">
          <span class="w-1.5 h-1.5 rounded-full bg-ok flex-shrink-0 mt-1.5"></span>
          <div>
            <p class="text-sm font-medium text-ink">命名规范</p>
            <p class="text-xs text-ink-secondary mt-0.5">变量和方法命名符合 Java 命名规范</p>
          </div>
        </li>
        <li class="px-5 py-4 flex items-start gap-3">
          <span class="w-1.5 h-1.5 rounded-full bg-ok flex-shrink-0 mt-1.5"></span>
          <div>
            <p class="text-sm font-medium text-ink">异常处理</p>
            <p class="text-xs text-ink-secondary mt-0.5">已正确处理异常情况</p>
          </div>
        </li>
        <li class="px-5 py-4 flex items-start gap-3">
          <span class="w-1.5 h-1.5 rounded-full bg-ok flex-shrink-0 mt-1.5"></span>
          <div>
            <p class="text-sm font-medium text-ink">注释完整</p>
            <p class="text-xs text-ink-secondary mt-0.5">关键方法和类都有适当的注释</p>
          </div>
        </li>
      </ul>
    </section>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const files = ref([
  {
    name: 'HealthController.java',
    path: 'src/main/java/com/example/demo/controller/HealthController.java',
    content: `package com.example.demo.controller;

import com.example.demo.service.HealthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 健康检查控制器
 */
@RestController
@RequestMapping("/api/health")
@RequiredArgsConstructor
public class HealthController {

    private final HealthService healthService;

    /**
     * 健康检查端点
     */
    @GetMapping
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(healthService.getHealthStatus());
    }

    /**
     * 详细健康状态
     */
    @GetMapping("/detail")
    public ResponseEntity<?> healthDetail() {
        return ResponseEntity.ok(healthService.getHealthDetail());
    }
}`
  },
  {
    name: 'HealthService.java',
    path: 'src/main/java/com/example/demo/service/HealthService.java',
    content: `package com.example.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 健康检查服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HealthService {

    /**
     * 获取健康状态
     */
    public Map<String, Object> getHealthStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "UP");
        status.put("timestamp", LocalDateTime.now().toString());
        return status;
    }

    /**
     * 获取详细健康状态
     */
    public Map<String, Object> getHealthDetail() {
        Map<String, Object> detail = new HashMap<>();
        detail.put("status", "UP");
        detail.put("timestamp", LocalDateTime.now().toString());
        detail.put("service", "demo-backend");
        detail.put("version", "1.0.0");
        detail.put("database", "UP");
        detail.put("memory", getMemoryInfo());
        return detail;
    }

    private Map<String, Object> getMemoryInfo() {
        Map<String, Object> memory = new HashMap<>();
        Runtime runtime = Runtime.getRuntime();
        memory.put("max", runtime.maxMemory() / 1024 / 1024 + " MB");
        memory.put("used", (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024 + " MB");
        memory.put("free", runtime.freeMemory() / 1024 / 1024 + " MB");
        return memory;
    }
}`
  },
  {
    name: 'HealthResponse.java',
    path: 'src/main/java/com/example/demo/dto/HealthResponse.java',
    content: `package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 健康检查响应 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthResponse {

    /**
     * 服务状态
     */
    private String status;

    /**
     * 时间戳
     */
    private LocalDateTime timestamp;

    /**
     * 服务名称
     */
    private String service;

    /**
     * 版本号
     */
    private String version;
}`
  },
  {
    name: 'HealthRepository.java',
    path: 'src/main/java/com/example/demo/repository/HealthRepository.java',
    content: `package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 健康检查数据访问接口
 */
@Repository
public interface HealthRepository extends JpaRepository<Object, Long> {
}`
  },
])

const selectedFile = ref(files.value[0])

const selectFile = (file) => {
  selectedFile.value = file
}
</script>
