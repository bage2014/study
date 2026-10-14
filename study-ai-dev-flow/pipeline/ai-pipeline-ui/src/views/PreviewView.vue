<template>
  <div class="space-y-6">
    <div class="flex items-center justify-between">
      <div>
        <h2 class="text-xl font-bold text-gray-800">变更预览</h2>
        <p class="text-sm text-gray-500 mt-1">查看代码变更差异和风险评估</p>
      </div>
      <span class="bg-blue-100 text-blue-700 px-3 py-1 rounded-full text-sm font-medium">Diff 视图</span>
    </div>

    <div class="bg-white rounded-xl shadow-md p-6">
      
      <div class="flex flex-wrap gap-2 mb-4">
        <button 
          v-for="file in changedFiles" 
          :key="file.name"
          @click="selectFile(file)"
          :class="[
            'px-3 py-2 rounded-lg text-sm whitespace-nowrap transition-all flex items-center',
            selectedFile?.name === file.name
              ? 'bg-indigo-600 text-white shadow-md'
              : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
          ]"
        >
          <span :class="file.type === 'added' ? 'text-green-400' : file.type === 'modified' ? 'text-yellow-400' : 'text-red-400'">
            {{ file.type === 'added' ? '+' : file.type === 'modified' ? '~' : '-' }}
          </span>
          <span class="ml-2">{{ file.name }}</span>
        </button>
      </div>

      <div class="border border-gray-200 rounded-xl overflow-hidden">
        <div class="bg-gray-50 px-4 py-3 flex items-center justify-between border-b border-gray-200">
          <span class="text-sm text-gray-600 font-mono">{{ selectedFile?.path }}</span>
          <span :class="[
            'text-xs px-2.5 py-1 rounded-full font-medium',
            selectedFile?.type === 'added' ? 'bg-green-100 text-green-700' :
            selectedFile?.type === 'modified' ? 'bg-yellow-100 text-yellow-700' :
            'bg-red-100 text-red-700'
          ]">
            {{ selectedFile?.type === 'added' ? '新增' : selectedFile?.type === 'modified' ? '修改' : '删除' }}
          </span>
        </div>
        <div class="bg-gray-900 p-4">
          <pre class="text-sm font-mono overflow-x-auto text-gray-300 max-h-96 overflow-y-auto">{{ selectedFile?.diff }}</pre>
        </div>
      </div>
    </div>

    <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
      <div class="bg-white rounded-xl shadow-md p-6">
        <h4 class="font-semibold text-gray-800 mb-4">变更统计</h4>
        <div class="space-y-3">
          <div class="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
            <div class="flex items-center">
              <span class="w-3 h-3 rounded-full bg-green-500 mr-3"></span>
              <span class="text-gray-600">新增文件</span>
            </div>
            <span class="text-xl font-bold text-green-600">2</span>
          </div>
          <div class="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
            <div class="flex items-center">
              <span class="w-3 h-3 rounded-full bg-yellow-500 mr-3"></span>
              <span class="text-gray-600">修改文件</span>
            </div>
            <span class="text-xl font-bold text-yellow-600">2</span>
          </div>
          <div class="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
            <div class="flex items-center">
              <span class="w-3 h-3 rounded-full bg-red-500 mr-3"></span>
              <span class="text-gray-600">删除文件</span>
            </div>
            <span class="text-xl font-bold text-red-600">0</span>
          </div>
          <div class="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
            <div class="flex items-center">
              <span class="w-3 h-3 rounded-full bg-blue-500 mr-3"></span>
              <span class="text-gray-600">新增行数</span>
            </div>
            <span class="text-xl font-bold text-blue-600">+156</span>
          </div>
          <div class="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
            <div class="flex items-center">
              <span class="w-3 h-3 rounded-full bg-orange-500 mr-3"></span>
              <span class="text-gray-600">删除行数</span>
            </div>
            <span class="text-xl font-bold text-orange-600">-0</span>
          </div>
        </div>
      </div>

      <div class="bg-white rounded-xl shadow-md p-6">
        <h4 class="font-semibold text-gray-800 mb-4">影响范围</h4>
        <div class="space-y-3">
          <div class="flex items-center justify-between p-3 bg-green-50 rounded-lg border border-green-100">
            <span class="text-gray-600">Controller</span>
            <span class="text-green-600 font-medium">新增</span>
          </div>
          <div class="flex items-center justify-between p-3 bg-green-50 rounded-lg border border-green-100">
            <span class="text-gray-600">Service</span>
            <span class="text-green-600 font-medium">新增</span>
          </div>
          <div class="flex items-center justify-between p-3 bg-green-50 rounded-lg border border-green-100">
            <span class="text-gray-600">DTO</span>
            <span class="text-green-600 font-medium">新增</span>
          </div>
          <div class="flex items-center justify-between p-3 bg-yellow-50 rounded-lg border border-yellow-100">
            <span class="text-gray-600">Repository</span>
            <span class="text-yellow-600 font-medium">修改</span>
          </div>
          <div class="flex items-center justify-between p-3 bg-gray-50 rounded-lg border border-gray-100">
            <span class="text-gray-600">配置文件</span>
            <span class="text-gray-600 font-medium">无变更</span>
          </div>
        </div>
      </div>

      <div class="bg-white rounded-xl shadow-md p-6">
        <h4 class="font-semibold text-gray-800 mb-4">风险评估</h4>
        <div class="space-y-4">
          <div>
            <div class="flex items-center justify-between mb-2">
              <span class="text-gray-600">整体风险</span>
              <span class="text-green-600 font-medium">低</span>
            </div>
            <div class="w-full bg-gray-200 rounded-full h-2.5">
              <div class="bg-gradient-to-r from-green-500 to-green-400 h-2.5 rounded-full transition-all" style="width: 20%"></div>
            </div>
          </div>
          <div class="bg-green-50 p-3 rounded-lg border border-green-100">
            <p class="text-sm text-green-700">✅ 无破坏性变更</p>
          </div>
          <div class="bg-green-50 p-3 rounded-lg border border-green-100">
            <p class="text-sm text-green-700">✅ 向后兼容</p>
          </div>
          <div class="bg-blue-50 p-3 rounded-lg border border-blue-100">
            <p class="text-sm text-blue-700">ℹ️ 建议进行回归测试</p>
          </div>
        </div>
      </div>
    </div>

    <div class="bg-white rounded-xl shadow-md p-6">
      <h4 class="font-semibold text-gray-800 mb-4">变更摘要</h4>
      <div class="bg-gray-50 p-4 rounded-lg border border-gray-200">
        <p class="text-sm text-gray-600">
          <strong>变更类型：</strong>功能新增<br>
          <strong>变更描述：</strong>添加健康检查端点，返回服务状态信息<br>
          <strong>影响模块：</strong>controller, service, dto, repository<br>
          <strong>变更日期：</strong>2026-07-11 23:20:35<br>
          <strong>分支：</strong>ai-run-a9b79feb<br>
        </p>
      </div>
    </div>
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
</script>
