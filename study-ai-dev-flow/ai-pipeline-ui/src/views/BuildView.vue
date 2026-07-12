<template>
  <div class="space-y-6">
    <div class="flex items-center justify-between">
      <div>
        <h2 class="text-xl font-bold text-gray-800">编译构建</h2>
        <p class="text-sm text-gray-500 mt-1">查看编译构建结果和测试验证报告</p>
      </div>
      <span class="bg-green-100 text-green-700 px-3 py-1 rounded-full text-sm font-medium">✓ 构建成功</span>
    </div>

    <div class="bg-white rounded-xl shadow-md p-6">

      <div class="grid grid-cols-2 md:grid-cols-4 gap-4 mb-6">
        <div class="bg-blue-50 p-4 rounded-lg">
          <p class="text-xs text-blue-600 mb-1">构建工具</p>
          <p class="text-lg font-bold text-blue-800">Maven</p>
        </div>
        <div class="bg-green-50 p-4 rounded-lg">
          <p class="text-xs text-green-600 mb-1">构建耗时</p>
          <p class="text-lg font-bold text-green-800">12.5s</p>
        </div>
        <div class="bg-purple-50 p-4 rounded-lg">
          <p class="text-xs text-purple-600 mb-1">Java版本</p>
          <p class="text-lg font-bold text-purple-800">21</p>
        </div>
        <div class="bg-orange-50 p-4 rounded-lg">
          <p class="text-xs text-orange-600 mb-1">构建时间</p>
          <p class="text-lg font-bold text-orange-800">2026-07-11</p>
        </div>
      </div>

      <div class="bg-gray-900 rounded-lg p-4">
        <div class="flex items-center justify-between mb-3">
          <span class="text-xs text-gray-400">构建日志</span>
          <span class="text-xs text-green-400">SUCCESS</span>
        </div>
        <pre class="text-sm text-gray-300 overflow-x-auto font-mono max-h-64 overflow-y-auto">{{ buildLog }}</pre>
      </div>
    </div>

    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
      <div class="bg-white rounded-lg shadow-md p-6">
        <h4 class="font-semibold text-gray-800 mb-4">测试结果</h4>
        <div class="space-y-4">
          <div class="flex items-center justify-between">
            <span class="text-gray-600">测试用例总数</span>
            <span class="text-xl font-bold text-indigo-600">5</span>
          </div>
          <div class="flex items-center justify-between">
            <span class="text-gray-600">通过</span>
            <span class="text-xl font-bold text-green-600">5</span>
          </div>
          <div class="flex items-center justify-between">
            <span class="text-gray-600">失败</span>
            <span class="text-xl font-bold text-red-600">0</span>
          </div>
          <div class="flex items-center justify-between">
            <span class="text-gray-600">跳过</span>
            <span class="text-xl font-bold text-gray-600">0</span>
          </div>
          <div>
            <div class="flex items-center justify-between mb-2">
              <span class="text-gray-600">覆盖率</span>
              <span class="text-green-600 font-medium">85%</span>
            </div>
            <div class="w-full bg-gray-200 rounded-full h-2">
              <div class="bg-green-600 h-2 rounded-full" style="width: 85%"></div>
            </div>
          </div>
        </div>
      </div>

      <div class="bg-white rounded-lg shadow-md p-6">
        <h4 class="font-semibold text-gray-800 mb-4">测试用例详情</h4>
        <div class="space-y-3">
          <div class="flex items-center justify-between p-3 bg-green-50 rounded-lg">
            <span class="text-gray-700">HealthControllerTest</span>
            <span class="text-green-600">✓ 通过</span>
          </div>
          <div class="flex items-center justify-between p-3 bg-green-50 rounded-lg">
            <span class="text-gray-700">HealthServiceTest</span>
            <span class="text-green-600">✓ 通过</span>
          </div>
          <div class="flex items-center justify-between p-3 bg-green-50 rounded-lg">
            <span class="text-gray-700">HealthResponseTest</span>
            <span class="text-green-600">✓ 通过</span>
          </div>
          <div class="flex items-center justify-between p-3 bg-green-50 rounded-lg">
            <span class="text-gray-700">HealthRepositoryTest</span>
            <span class="text-green-600">✓ 通过</span>
          </div>
          <div class="flex items-center justify-between p-3 bg-green-50 rounded-lg">
            <span class="text-gray-700">IntegrationTest</span>
            <span class="text-green-600">✓ 通过</span>
          </div>
        </div>
      </div>
    </div>

    <div class="bg-white rounded-lg shadow-md p-6">
      <h4 class="font-semibold text-gray-800 mb-4">生成的产物</h4>
      <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
        <div class="border border-gray-200 rounded-lg p-4">
          <div class="flex items-center justify-between mb-2">
            <span class="text-gray-700">demo-backend-1.0.0.jar</span>
            <span class="text-green-600">✓</span>
          </div>
          <p class="text-xs text-gray-500">大小: 15.2 MB</p>
          <p class="text-xs text-gray-500">路径: target/demo-backend-1.0.0.jar</p>
        </div>
        <div class="border border-gray-200 rounded-lg p-4">
          <div class="flex items-center justify-between mb-2">
            <span class="text-gray-700">demo-backend-1.0.0.jar.original</span>
            <span class="text-green-600">✓</span>
          </div>
          <p class="text-xs text-gray-500">大小: 2.1 MB</p>
          <p class="text-xs text-gray-500">路径: target/demo-backend-1.0.0.jar.original</p>
        </div>
        <div class="border border-gray-200 rounded-lg p-4">
          <div class="flex items-center justify-between mb-2">
            <span class="text-gray-700">classes/</span>
            <span class="text-green-600">✓</span>
          </div>
          <p class="text-xs text-gray-500">编译后的 class 文件</p>
          <p class="text-xs text-gray-500">路径: target/classes/</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
const buildLog = `[INFO] Scanning for projects...
[INFO] 
[INFO] ------------------< com.example:demo-backend >-------------------
[INFO] Building demo-backend 1.0.0
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- maven-resources-plugin:3.3.1:resources (default-resources) @ demo-backend ---
[INFO] Copying 3 resources
[INFO] 
[INFO] --- maven-compiler-plugin:3.11.0:compile (default-compile) @ demo-backend ---
[INFO] Changes detected - recompiling the module!
[INFO] Compiling 4 source files to /Users/bage/bage/github/study/study-ai-dev-flow/demo-backend/target/classes
[INFO] 
[INFO] --- maven-resources-plugin:3.3.1:testResources (default-testResources) @ demo-backend ---
[INFO] Copying 5 resources
[INFO] 
[INFO] --- maven-compiler-plugin:3.11.0:testCompile (default-testCompile) @ demo-backend ---
[INFO] Changes detected - recompiling the module!
[INFO] Compiling 5 test source files to /Users/bage/bage/github/study/study-ai-dev-flow/demo-backend/target/test-classes
[INFO] 
[INFO] --- maven-surefire-plugin:3.2.2:test (default-test) @ demo-backend ---
[INFO] 
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.example.demo.HealthControllerTest
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] Running com.example.demo.HealthServiceTest
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] Running com.example.demo.HealthResponseTest
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] Running com.example.demo.HealthRepositoryTest
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] Running com.example.demo.IntegrationTest
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] 
[INFO] --- maven-jar-plugin:3.3.0:jar (default-jar) @ demo-backend ---
[INFO] Building jar: /Users/bage/bage/github/study/study-ai-dev-flow/demo-backend/target/demo-backend-1.0.0.jar
[INFO] 
[INFO] --- spring-boot-maven-plugin:3.2.0:repackage (repackage) @ demo-backend ---
[INFO] Replacing main artifact with repackaged archive
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  12.534 s
[INFO] Finished at: 2026-07-11T23:20:50+08:00
[INFO] ------------------------------------------------------------------------`
</script>
