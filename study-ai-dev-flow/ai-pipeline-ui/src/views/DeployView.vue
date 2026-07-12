<template>
  <div class="space-y-6">
    <div class="flex items-center justify-between">
      <div>
        <h2 class="text-xl font-bold text-gray-800">发布部署</h2>
        <p class="text-sm text-gray-500 mt-1">查看部署状态和运行监控信息</p>
      </div>
      <span class="bg-green-100 text-green-700 px-3 py-1 rounded-full text-sm font-medium">✓ 部署成功</span>
    </div>

    <div class="bg-white rounded-xl shadow-md p-6">

      <div class="grid grid-cols-2 md:grid-cols-5 gap-4 mb-6">
        <div class="bg-blue-50 p-4 rounded-lg">
          <p class="text-xs text-blue-600 mb-1">部署环境</p>
          <p class="text-lg font-bold text-blue-800">开发环境</p>
        </div>
        <div class="bg-green-50 p-4 rounded-lg">
          <p class="text-xs text-green-600 mb-1">部署状态</p>
          <p class="text-lg font-bold text-green-800">运行中</p>
        </div>
        <div class="bg-purple-50 p-4 rounded-lg">
          <p class="text-xs text-purple-600 mb-1">版本号</p>
          <p class="text-lg font-bold text-purple-800">1.0.0</p>
        </div>
        <div class="bg-orange-50 p-4 rounded-lg">
          <p class="text-xs text-orange-600 mb-1">部署时间</p>
          <p class="text-lg font-bold text-orange-800">23:25</p>
        </div>
        <div class="bg-pink-50 p-4 rounded-lg">
          <p class="text-xs text-pink-600 mb-1">部署耗时</p>
          <p class="text-lg font-bold text-pink-800">45s</p>
        </div>
      </div>
    </div>

    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
      <div class="bg-white rounded-lg shadow-md p-6">
        <h4 class="font-semibold text-gray-800 mb-4">服务端点</h4>
        <div class="space-y-3">
          <div class="flex items-center justify-between p-3 bg-green-50 rounded-lg">
            <div>
              <p class="text-gray-700 font-medium">健康检查</p>
              <p class="text-xs text-gray-500">GET /api/health</p>
            </div>
            <span class="text-green-600">✓ 在线</span>
          </div>
          <div class="flex items-center justify-between p-3 bg-green-50 rounded-lg">
            <div>
              <p class="text-gray-700 font-medium">详细状态</p>
              <p class="text-xs text-gray-500">GET /api/health/detail</p>
            </div>
            <span class="text-green-600">✓ 在线</span>
          </div>
        </div>
      </div>

      <div class="bg-white rounded-lg shadow-md p-6">
        <h4 class="font-semibold text-gray-800 mb-4">运行状态</h4>
        <div class="space-y-4">
          <div class="flex items-center justify-between">
            <span class="text-gray-600">CPU 使用率</span>
            <span class="text-green-600 font-medium">12%</span>
          </div>
          <div class="w-full bg-gray-200 rounded-full h-2">
            <div class="bg-green-600 h-2 rounded-full" style="width: 12%"></div>
          </div>
          
          <div class="flex items-center justify-between">
            <span class="text-gray-600">内存使用</span>
            <span class="text-blue-600 font-medium">256 MB / 512 MB</span>
          </div>
          <div class="w-full bg-gray-200 rounded-full h-2">
            <div class="bg-blue-600 h-2 rounded-full" style="width: 50%"></div>
          </div>
          
          <div class="flex items-center justify-between">
            <span class="text-gray-600">JVM 线程数</span>
            <span class="text-purple-600 font-medium">24</span>
          </div>
          
          <div class="flex items-center justify-between">
            <span class="text-gray-600">启动时间</span>
            <span class="text-gray-600 font-medium">2026-07-11 23:25:30</span>
          </div>
        </div>
      </div>
    </div>

    <div class="bg-white rounded-lg shadow-md p-6">
      <h4 class="font-semibold text-gray-800 mb-4">部署历史</h4>
      <div class="space-y-3">
        <div class="flex items-center justify-between p-4 border border-gray-200 rounded-lg">
          <div class="flex items-center">
            <span class="w-3 h-3 rounded-full bg-green-500 mr-3"></span>
            <div>
              <p class="text-gray-800 font-medium">版本 1.0.0</p>
              <p class="text-xs text-gray-500">2026-07-11 23:25:30 | 部署到开发环境</p>
            </div>
          </div>
          <span class="text-green-600 text-sm">成功</span>
        </div>
        <div class="flex items-center justify-between p-4 border border-gray-200 rounded-lg">
          <div class="flex items-center">
            <span class="w-3 h-3 rounded-full bg-yellow-500 mr-3"></span>
            <div>
              <p class="text-gray-800 font-medium">版本 0.9.0</p>
              <p class="text-xs text-gray-500">2026-07-10 15:42:18 | 部署到开发环境</p>
            </div>
          </div>
          <span class="text-yellow-600 text-sm">回滚</span>
        </div>
        <div class="flex items-center justify-between p-4 border border-gray-200 rounded-lg">
          <div class="flex items-center">
            <span class="w-3 h-3 rounded-full bg-green-500 mr-3"></span>
            <div>
              <p class="text-gray-800 font-medium">版本 0.8.0</p>
              <p class="text-xs text-gray-500">2026-07-09 09:15:00 | 部署到开发环境</p>
            </div>
          </div>
          <span class="text-green-600 text-sm">成功</span>
        </div>
      </div>
    </div>

    <div class="bg-white rounded-lg shadow-md p-6">
      <h4 class="font-semibold text-gray-800 mb-4">部署日志</h4>
      <div class="bg-gray-900 rounded-lg p-4">
        <pre class="text-sm text-gray-300 overflow-x-auto font-mono max-h-48 overflow-y-auto">{{ deployLog }}</pre>
      </div>
    </div>
  </div>
</template>

<script setup>
const deployLog = `2026-07-11 23:25:00 INFO  - 开始部署 demo-backend-1.0.0.jar
2026-07-11 23:25:01 INFO  - 检查目标环境...
2026-07-11 23:25:02 INFO  - 环境检查通过
2026-07-11 23:25:03 INFO  - 停止旧版本服务...
2026-07-11 23:25:05 INFO  - 旧版本服务已停止
2026-07-11 23:25:06 INFO  - 复制新包到目标目录...
2026-07-11 23:25:10 INFO  - 包复制完成
2026-07-11 23:25:11 INFO  - 更新配置文件...
2026-07-11 23:25:12 INFO  - 配置文件更新完成
2026-07-11 23:25:13 INFO  - 启动新服务...
2026-07-11 23:25:15 INFO  - 服务启动中...
2026-07-11 23:25:25 INFO  - 服务启动成功
2026-07-11 23:25:26 INFO  - 执行健康检查...
2026-07-11 23:25:28 INFO  - 健康检查通过: {"status":"UP","timestamp":"2026-07-11T23:25:28"}
2026-07-11 23:25:30 INFO  - 部署完成，服务运行正常`
</script>
