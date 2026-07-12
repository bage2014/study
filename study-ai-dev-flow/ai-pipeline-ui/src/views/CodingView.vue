<template>
  <div class="space-y-6">
    <div class="flex items-center justify-between">
      <div>
        <h2 class="text-xl font-bold text-gray-800">编码实现</h2>
        <p class="text-sm text-gray-500 mt-1">AI 根据技术方案自动生成代码实现</p>
      </div>
      <span class="bg-green-100 text-green-700 px-3 py-1 rounded-full text-sm font-medium">已完成</span>
    </div>

    <div class="bg-white rounded-xl shadow-md p-6">
      
      <div class="flex flex-wrap gap-2 mb-4">
        <button 
          v-for="file in files" 
          :key="file.name"
          @click="selectFile(file)"
          :class="[
            'px-3 py-2 rounded-lg text-sm whitespace-nowrap transition-all',
            selectedFile?.name === file.name
              ? 'bg-indigo-600 text-white shadow-md'
              : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
          ]"
        >
          {{ file.name }}
        </button>
      </div>

      <div class="bg-gray-900 rounded-xl p-4 border border-gray-700">
        <div class="flex items-center justify-between mb-3">
          <span class="text-xs text-gray-400 font-mono">{{ selectedFile?.path }}</span>
          <span class="text-xs text-green-400">✓ 已生成</span>
        </div>
        <pre class="text-sm text-green-400 overflow-x-auto font-mono max-h-80 overflow-y-auto">{{ selectedFile?.content }}</pre>
      </div>
    </div>

    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
      <div class="bg-white rounded-xl shadow-md p-6">
        <h4 class="font-semibold text-gray-800 mb-4">代码统计</h4>
        <div class="space-y-4">
          <div class="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
            <span class="text-gray-600">总文件数</span>
            <span class="text-xl font-bold text-indigo-600">4</span>
          </div>
          <div class="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
            <span class="text-gray-600">总行数</span>
            <span class="text-xl font-bold text-green-600">156</span>
          </div>
          <div class="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
            <span class="text-gray-600">代码行数</span>
            <span class="text-xl font-bold text-blue-600">128</span>
          </div>
          <div class="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
            <span class="text-gray-600">注释行数</span>
            <span class="text-xl font-bold text-gray-600">28</span>
          </div>
          <div class="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
            <span class="text-gray-600">空行数</span>
            <span class="text-xl font-bold text-gray-500">0</span>
          </div>
        </div>
      </div>

      <div class="bg-white rounded-xl shadow-md p-6">
        <h4 class="font-semibold text-gray-800 mb-4">代码质量</h4>
        <div class="space-y-4">
          <div>
            <div class="flex items-center justify-between mb-2">
              <span class="text-gray-600">代码覆盖率</span>
              <span class="text-green-600 font-medium">85%</span>
            </div>
            <div class="w-full bg-gray-200 rounded-full h-2.5">
              <div class="bg-gradient-to-r from-green-500 to-green-400 h-2.5 rounded-full transition-all" style="width: 85%"></div>
            </div>
          </div>
          <div>
            <div class="flex items-center justify-between mb-2">
              <span class="text-gray-600">复杂度</span>
              <span class="text-green-600 font-medium">低</span>
            </div>
            <div class="w-full bg-gray-200 rounded-full h-2.5">
              <div class="bg-gradient-to-r from-green-500 to-green-400 h-2.5 rounded-full transition-all" style="width: 30%"></div>
            </div>
          </div>
          <div>
            <div class="flex items-center justify-between mb-2">
              <span class="text-gray-600">重复率</span>
              <span class="text-green-600 font-medium">5%</span>
            </div>
            <div class="w-full bg-gray-200 rounded-full h-2.5">
              <div class="bg-gradient-to-r from-green-500 to-green-400 h-2.5 rounded-full transition-all" style="width: 5%"></div>
            </div>
          </div>
          <div>
            <div class="flex items-center justify-between mb-2">
              <span class="text-gray-600">规范评分</span>
              <span class="text-green-600 font-medium">92</span>
            </div>
            <div class="w-full bg-gray-200 rounded-full h-2.5">
              <div class="bg-gradient-to-r from-green-500 to-green-400 h-2.5 rounded-full transition-all" style="width: 92%"></div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="bg-white rounded-xl shadow-md p-6">
      <h4 class="font-semibold text-gray-800 mb-4">代码审查</h4>
      <div class="space-y-3">
        <div class="bg-green-50 p-4 rounded-lg border border-green-100">
          <div class="flex items-start">
            <span class="text-green-600 text-xl mr-3">✓</span>
            <div>
              <p class="font-medium text-green-800">命名规范</p>
              <p class="text-sm text-green-600">变量和方法命名符合 Java 命名规范</p>
            </div>
          </div>
        </div>
        <div class="bg-green-50 p-4 rounded-lg border border-green-100">
          <div class="flex items-start">
            <span class="text-green-600 text-xl mr-3">✓</span>
            <div>
              <p class="font-medium text-green-800">异常处理</p>
              <p class="text-sm text-green-600">已正确处理异常情况</p>
            </div>
          </div>
        </div>
        <div class="bg-green-50 p-4 rounded-lg border border-green-100">
          <div class="flex items-start">
            <span class="text-green-600 text-xl mr-3">✓</span>
            <div>
              <p class="font-medium text-green-800">注释完整</p>
              <p class="text-sm text-green-600">关键方法和类都有适当的注释</p>
            </div>
          </div>
        </div>
      </div>
    </div>
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
