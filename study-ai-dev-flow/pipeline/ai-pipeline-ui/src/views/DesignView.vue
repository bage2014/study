<template>
  <div class="space-y-6">
    <div class="flex items-center justify-between">
      <div>
        <h2 class="text-xl font-bold text-gray-800">技术方案设计</h2>
        <p class="text-sm text-gray-500 mt-1">AI 根据需求自动生成技术方案和架构设计</p>
      </div>
      <span class="bg-blue-100 text-blue-700 px-3 py-1 rounded-full text-sm font-medium">AI 生成</span>
    </div>

    <div class="bg-white rounded-xl shadow-md p-6">
      <div class="space-y-6">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">需求描述</label>
          <div class="bg-gray-50 p-4 rounded-lg border border-gray-200">
            <p class="text-gray-600">添加健康检查端点，返回服务状态信息</p>
          </div>
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">技术选型</label>
          <div class="grid grid-cols-2 md:grid-cols-4 gap-3">
            <div class="bg-green-50 p-4 rounded-lg text-center border border-green-100">
              <p class="text-green-600 font-medium">Spring Boot</p>
              <p class="text-xs text-gray-500 mt-1">3.2.x</p>
            </div>
            <div class="bg-blue-50 p-4 rounded-lg text-center border border-blue-100">
              <p class="text-blue-600 font-medium">Java</p>
              <p class="text-xs text-gray-500 mt-1">21</p>
            </div>
            <div class="bg-purple-50 p-4 rounded-lg text-center border border-purple-100">
              <p class="text-purple-600 font-medium">H2</p>
              <p class="text-xs text-gray-500 mt-1">内存数据库</p>
            </div>
            <div class="bg-orange-50 p-4 rounded-lg text-center border border-orange-100">
              <p class="text-orange-600 font-medium">Maven</p>
              <p class="text-xs text-gray-500 mt-1">构建工具</p>
            </div>
          </div>
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">架构设计</label>
          <div class="bg-gray-50 p-4 rounded-lg border border-gray-200">
            <pre class="text-sm text-gray-600 whitespace-pre-wrap font-mono">
┌─────────────────────────────────────────────────┐
│                  Controller Layer               │
│         HealthController (REST API)             │
└─────────────────────────────────────────────────┘
                        │
                        ▼
┌─────────────────────────────────────────────────┐
│                   Service Layer                 │
│           HealthService (业务逻辑)              │
└─────────────────────────────────────────────────┘
                        │
                        ▼
┌─────────────────────────────────────────────────┐
│                  Data Layer                     │
│           HealthRepository (数据访问)           │
└─────────────────────────────────────────────────┘
                        │
                        ▼
┌─────────────────────────────────────────────────┐
│                  H2 Database                    │
└─────────────────────────────────────────────────┘
            </pre>
          </div>
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">API 设计</label>
          <div class="overflow-x-auto border border-gray-200 rounded-lg">
            <table class="w-full text-sm">
              <thead>
                <tr class="bg-gray-50">
                  <th class="px-4 py-2 text-left font-medium text-gray-600">方法</th>
                  <th class="px-4 py-2 text-left font-medium text-gray-600">路径</th>
                  <th class="px-4 py-2 text-left font-medium text-gray-600">描述</th>
                  <th class="px-4 py-2 text-left font-medium text-gray-600">状态码</th>
                </tr>
              </thead>
              <tbody>
                <tr class="border-b border-gray-100 hover:bg-gray-50">
                  <td class="px-4 py-3"><span class="bg-green-100 text-green-700 px-2 py-1 rounded text-xs font-medium">GET</span></td>
                  <td class="px-4 py-3 text-gray-700 font-mono">/api/health</td>
                  <td class="px-4 py-3 text-gray-600">健康检查端点</td>
                  <td class="px-4 py-3 text-gray-500">200 OK</td>
                </tr>
                <tr class="hover:bg-gray-50">
                  <td class="px-4 py-3"><span class="bg-blue-100 text-blue-700 px-2 py-1 rounded text-xs font-medium">GET</span></td>
                  <td class="px-4 py-3 text-gray-700 font-mono">/api/health/detail</td>
                  <td class="px-4 py-3 text-gray-600">详细健康状态</td>
                  <td class="px-4 py-3 text-gray-500">200 OK</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">类设计</label>
          <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div class="border border-gray-200 rounded-lg p-4 hover:border-indigo-300 transition-colors">
              <div class="flex items-center mb-2">
                <span class="text-xl mr-2">📄</span>
                <span class="font-medium text-gray-800">HealthController.java</span>
              </div>
              <p class="text-xs text-gray-500">控制器类，提供健康检查 REST API</p>
            </div>
            <div class="border border-gray-200 rounded-lg p-4 hover:border-indigo-300 transition-colors">
              <div class="flex items-center mb-2">
                <span class="text-xl mr-2">📄</span>
                <span class="font-medium text-gray-800">HealthService.java</span>
              </div>
              <p class="text-xs text-gray-500">服务类，处理健康检查业务逻辑</p>
            </div>
            <div class="border border-gray-200 rounded-lg p-4 hover:border-indigo-300 transition-colors">
              <div class="flex items-center mb-2">
                <span class="text-xl mr-2">📄</span>
                <span class="font-medium text-gray-800">HealthResponse.java</span>
              </div>
              <p class="text-xs text-gray-500">响应 DTO，封装健康检查结果</p>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="bg-white rounded-xl shadow-md p-6">
      <h3 class="font-semibold text-gray-800 mb-4">方案评审</h3>
      <div class="space-y-4">
        <div class="flex items-center gap-4">
          <div class="flex-1">
            <label class="block text-sm font-medium text-gray-700 mb-2">评审意见</label>
            <textarea class="w-full border border-gray-300 rounded-lg p-3 text-sm resize-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent" rows="3" placeholder="输入评审意见..."></textarea>
          </div>
          <div class="flex gap-2">
            <button class="px-5 py-2.5 bg-green-600 text-white rounded-lg hover:bg-green-700 text-sm font-medium transition-colors">批准</button>
            <button class="px-5 py-2.5 bg-red-600 text-white rounded-lg hover:bg-red-700 text-sm font-medium transition-colors">驳回</button>
          </div>
        </div>
        <div class="bg-blue-50 p-4 rounded-lg border border-blue-100">
          <p class="text-sm text-blue-700">💡 AI 建议：方案设计合理，建议增加数据库连接池状态检查和缓存状态检查。</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
</script>
