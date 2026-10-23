<template>
  <div class="space-y-6">
    <header class="flex items-start justify-between">
      <div>
        <p class="text-2xs font-medium uppercase tracking-wide text-ink-muted mb-1">Stage 11</p>
        <h1 class="text-2xl font-semibold tracking-tight text-ink">发布部署</h1>
        <p class="text-ink-secondary mt-1 text-sm">查看部署状态和运行监控信息</p>
      </div>
      <span class="tag bg-ok-soft text-ok">部署成功</span>
    </header>

    <section class="card">
      <h2 class="px-5 py-3 border-b border-line text-sm font-medium text-ink">部署信息</h2>
      <div class="px-5 py-5">
        <div class="grid grid-cols-2 md:grid-cols-5 gap-px bg-line border border-line rounded-lg overflow-hidden">
          <div class="bg-surface p-4">
            <p class="text-2xs font-medium uppercase tracking-wide text-ink-muted">部署环境</p>
            <p class="text-sm font-medium text-ink mt-1">开发环境</p>
          </div>
          <div class="bg-surface p-4">
            <p class="text-2xs font-medium uppercase tracking-wide text-ink-muted">部署状态</p>
            <p class="text-sm font-medium text-ok mt-1">运行中</p>
          </div>
          <div class="bg-surface p-4">
            <p class="text-2xs font-medium uppercase tracking-wide text-ink-muted">版本号</p>
            <p class="mono text-sm text-ink mt-1">1.0.0</p>
          </div>
          <div class="bg-surface p-4">
            <p class="text-2xs font-medium uppercase tracking-wide text-ink-muted">部署时间</p>
            <p class="mono text-sm text-ink mt-1">23:25</p>
          </div>
          <div class="bg-surface p-4">
            <p class="text-2xs font-medium uppercase tracking-wide text-ink-muted">部署耗时</p>
            <p class="mono text-sm text-ink mt-1">45s</p>
          </div>
        </div>
      </div>
    </section>

    <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
      <section class="card">
        <h2 class="px-5 py-3 border-b border-line text-sm font-medium text-ink">服务端点</h2>
        <ul class="divide-y divide-line">
          <li class="px-5 py-3 flex items-center justify-between gap-3 text-sm">
            <span class="mono text-2xs font-medium text-accent w-12">GET</span>
            <span class="mono text-xs text-ink-secondary flex-1 truncate">/api/health</span>
            <span class="tag bg-ok-soft text-ok">在线</span>
          </li>
          <li class="px-5 py-3 flex items-center justify-between gap-3 text-sm">
            <span class="mono text-2xs font-medium text-accent w-12">GET</span>
            <span class="mono text-xs text-ink-secondary flex-1 truncate">/api/health/detail</span>
            <span class="tag bg-ok-soft text-ok">在线</span>
          </li>
        </ul>
      </section>

      <section class="card">
        <h2 class="px-5 py-3 border-b border-line text-sm font-medium text-ink">运行状态</h2>
        <div class="px-5 py-4 space-y-4">
          <div>
            <div class="flex items-center justify-between mb-1.5 text-sm">
              <span class="text-ink-secondary">CPU 使用率</span>
              <span class="mono text-xs text-ink">12%</span>
            </div>
            <div class="w-full bg-line rounded-full h-1">
              <div class="bg-ok h-1 rounded-full" style="width: 12%"></div>
            </div>
          </div>
          <div>
            <div class="flex items-center justify-between mb-1.5 text-sm">
              <span class="text-ink-secondary">内存使用</span>
              <span class="mono text-xs text-ink">256 MB / 512 MB</span>
            </div>
            <div class="w-full bg-line rounded-full h-1">
              <div class="bg-accent h-1 rounded-full" style="width: 50%"></div>
            </div>
          </div>
          <dl class="pt-1 space-y-2 text-sm">
            <div class="flex justify-between"><dt class="text-ink-muted">JVM 线程数</dt><dd class="mono text-xs text-ink">24</dd></div>
            <div class="flex justify-between"><dt class="text-ink-muted">启动时间</dt><dd class="mono text-xs text-ink">2026-07-11 23:25:30</dd></div>
          </dl>
        </div>
      </section>
    </div>

    <section class="card">
      <h2 class="px-5 py-3 border-b border-line text-sm font-medium text-ink">部署历史</h2>
      <ol class="divide-y divide-line">
        <li class="px-5 py-4 flex items-center justify-between">
          <div class="flex items-center gap-3">
            <span class="w-1.5 h-1.5 rounded-full bg-ok flex-shrink-0"></span>
            <div>
              <p class="text-sm font-medium text-ink">v1.0.0</p>
              <p class="mono text-2xs text-ink-muted mt-0.5">2026-07-11 23:25:30 · 开发环境</p>
            </div>
          </div>
          <span class="tag bg-ok-soft text-ok">成功</span>
        </li>
        <li class="px-5 py-4 flex items-center justify-between">
          <div class="flex items-center gap-3">
            <span class="w-1.5 h-1.5 rounded-full bg-warn flex-shrink-0"></span>
            <div>
              <p class="text-sm font-medium text-ink">v0.9.0</p>
              <p class="mono text-2xs text-ink-muted mt-0.5">2026-07-10 15:42:18 · 开发环境</p>
            </div>
          </div>
          <span class="tag bg-warn-soft text-warn">回滚</span>
        </li>
        <li class="px-5 py-4 flex items-center justify-between">
          <div class="flex items-center gap-3">
            <span class="w-1.5 h-1.5 rounded-full bg-ok flex-shrink-0"></span>
            <div>
              <p class="text-sm font-medium text-ink">v0.8.0</p>
              <p class="mono text-2xs text-ink-muted mt-0.5">2026-07-09 09:15:00 · 开发环境</p>
            </div>
          </div>
          <span class="tag bg-ok-soft text-ok">成功</span>
        </li>
      </ol>
    </section>

    <section class="card">
      <h2 class="px-5 py-3 border-b border-line text-sm font-medium text-ink">部署日志</h2>
      <div class="bg-ink">
        <pre class="mono text-xs text-white/90 p-4 overflow-x-auto max-h-64 overflow-y-auto">{{ deployLog }}</pre>
      </div>
    </section>
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
