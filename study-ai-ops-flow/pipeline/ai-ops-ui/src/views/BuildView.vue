<template>
  <div class="space-y-6">
    <header class="flex items-start justify-between">
      <div>
        <p class="text-2xs font-medium uppercase tracking-wide text-ink-muted mb-1">Stage 10</p>
        <h1 class="text-2xl font-semibold tracking-tight text-ink">编译构建</h1>
        <p class="text-ink-secondary mt-1 text-sm">查看编译构建结果和测试验证报告</p>
      </div>
      <span class="tag bg-ok-soft text-ok">构建成功</span>
    </header>

    <section class="card">
      <h2 class="px-5 py-3 border-b border-line text-sm font-medium text-ink">构建信息</h2>
      <div class="px-5 py-5 space-y-5">
        <div class="grid grid-cols-2 md:grid-cols-4 gap-px bg-line border border-line rounded-lg overflow-hidden">
          <div class="bg-surface p-4">
            <p class="text-2xs font-medium uppercase tracking-wide text-ink-muted">构建工具</p>
            <p class="mono text-sm text-ink mt-1">Maven</p>
          </div>
          <div class="bg-surface p-4">
            <p class="text-2xs font-medium uppercase tracking-wide text-ink-muted">构建耗时</p>
            <p class="mono text-sm text-ok mt-1">12.5s</p>
          </div>
          <div class="bg-surface p-4">
            <p class="text-2xs font-medium uppercase tracking-wide text-ink-muted">Java 版本</p>
            <p class="mono text-sm text-ink mt-1">21</p>
          </div>
          <div class="bg-surface p-4">
            <p class="text-2xs font-medium uppercase tracking-wide text-ink-muted">构建时间</p>
            <p class="mono text-sm text-ink mt-1">2026-07-11</p>
          </div>
        </div>

        <div class="bg-ink rounded overflow-hidden">
          <div class="flex items-center justify-between px-4 py-2 border-b border-white/10">
            <span class="text-2xs font-medium uppercase tracking-wide text-white/60">构建日志</span>
            <span class="text-2xs font-medium uppercase tracking-wide text-ok">SUCCESS</span>
          </div>
          <pre class="mono text-xs text-white/90 p-4 overflow-x-auto max-h-72 overflow-y-auto">{{ buildLog }}</pre>
        </div>
      </div>
    </section>

    <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
      <section class="card">
        <h2 class="px-5 py-3 border-b border-line text-sm font-medium text-ink">测试结果</h2>
        <div class="px-5 py-4 space-y-4">
          <div class="grid grid-cols-4 gap-px bg-line border border-line rounded overflow-hidden text-center">
            <div class="bg-surface p-3">
              <p class="mono text-lg text-ink">5</p>
              <p class="text-2xs text-ink-muted">总数</p>
            </div>
            <div class="bg-surface p-3">
              <p class="mono text-lg text-ok">5</p>
              <p class="text-2xs text-ink-muted">通过</p>
            </div>
            <div class="bg-surface p-3">
              <p class="mono text-lg text-danger">0</p>
              <p class="text-2xs text-ink-muted">失败</p>
            </div>
            <div class="bg-surface p-3">
              <p class="mono text-lg text-ink-secondary">0</p>
              <p class="text-2xs text-ink-muted">跳过</p>
            </div>
          </div>
          <div>
            <div class="flex items-center justify-between mb-1.5 text-sm">
              <span class="text-ink-secondary">覆盖率</span>
              <span class="mono text-xs text-ink">85%</span>
            </div>
            <div class="w-full bg-line rounded-full h-1">
              <div class="bg-ok h-1 rounded-full" style="width: 85%"></div>
            </div>
          </div>
        </div>
      </section>

      <section class="card">
        <h2 class="px-5 py-3 border-b border-line text-sm font-medium text-ink">测试用例详情</h2>
        <ul class="divide-y divide-line">
          <li v-for="t in ['HealthControllerTest', 'HealthServiceTest', 'HealthResponseTest', 'HealthRepositoryTest', 'IntegrationTest']" :key="t" class="px-5 py-3 flex items-center justify-between text-sm">
            <span class="mono text-xs text-ink">{{ t }}</span>
            <span class="tag bg-ok-soft text-ok">通过</span>
          </li>
        </ul>
      </section>
    </div>

    <section class="card">
      <h2 class="px-5 py-3 border-b border-line text-sm font-medium text-ink">生成的产物</h2>
      <div class="px-5 py-5 grid grid-cols-1 md:grid-cols-3 gap-2">
        <div class="border border-line rounded p-3">
          <div class="flex items-center justify-between">
            <span class="mono text-xs font-medium text-ink">demo-backend-1.0.0.jar</span>
            <span class="w-1.5 h-1.5 rounded-full bg-ok"></span>
          </div>
          <p class="text-2xs text-ink-muted mt-2">大小 · 15.2 MB</p>
          <p class="mono text-2xs text-ink-muted mt-0.5">target/demo-backend-1.0.0.jar</p>
        </div>
        <div class="border border-line rounded p-3">
          <div class="flex items-center justify-between">
            <span class="mono text-xs font-medium text-ink">demo-backend-1.0.0.jar.original</span>
            <span class="w-1.5 h-1.5 rounded-full bg-ok"></span>
          </div>
          <p class="text-2xs text-ink-muted mt-2">大小 · 2.1 MB</p>
          <p class="mono text-2xs text-ink-muted mt-0.5">target/demo-backend-1.0.0.jar.original</p>
        </div>
        <div class="border border-line rounded p-3">
          <div class="flex items-center justify-between">
            <span class="mono text-xs font-medium text-ink">classes/</span>
            <span class="w-1.5 h-1.5 rounded-full bg-ok"></span>
          </div>
          <p class="text-2xs text-ink-muted mt-2">编译后的 class 文件</p>
          <p class="mono text-2xs text-ink-muted mt-0.5">target/classes/</p>
        </div>
      </div>
    </section>
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
[INFO] Compiling 4 source files to /Users/bage/bage/github/study/study-ai-ops-flow/demo-backend/target/classes
[INFO] 
[INFO] --- maven-resources-plugin:3.3.1:testResources (default-testResources) @ demo-backend ---
[INFO] Copying 5 resources
[INFO] 
[INFO] --- maven-compiler-plugin:3.11.0:testCompile (default-testCompile) @ demo-backend ---
[INFO] Changes detected - recompiling the module!
[INFO] Compiling 5 test source files to /Users/bage/bage/github/study/study-ai-ops-flow/demo-backend/target/test-classes
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
[INFO] Building jar: /Users/bage/bage/github/study/study-ai-ops-flow/demo-backend/target/demo-backend-1.0.0.jar
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
