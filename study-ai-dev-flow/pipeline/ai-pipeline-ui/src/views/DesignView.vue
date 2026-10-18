<template>
  <div class="space-y-6">
    <header class="flex items-start justify-between">
      <div>
        <p class="text-2xs font-medium uppercase tracking-wide text-ink-muted mb-1">Stage 04</p>
        <h1 class="text-2xl font-semibold tracking-tight text-ink">技术方案设计</h1>
        <p class="text-ink-secondary mt-1 text-sm">AI 根据需求自动生成技术方案和架构设计</p>
      </div>
      <span class="tag bg-accent-soft text-accent">AI 生成</span>
    </header>

    <section class="card">
      <h2 class="px-5 py-3 border-b border-line text-sm font-medium text-ink">方案详情</h2>
      <div class="px-5 py-5 space-y-6">
        <div>
          <p class="text-2xs font-medium uppercase tracking-wide text-ink-muted mb-2">需求描述</p>
          <div class="border-l-2 border-accent pl-3">
            <p class="text-sm text-ink-secondary">添加健康检查端点，返回服务状态信息</p>
          </div>
        </div>

        <div>
          <p class="text-2xs font-medium uppercase tracking-wide text-ink-muted mb-2">技术选型</p>
          <div class="grid grid-cols-2 md:grid-cols-4 gap-px bg-line border border-line rounded-lg overflow-hidden">
            <div class="bg-surface p-4">
              <p class="text-sm font-medium text-ink">Spring Boot</p>
              <p class="text-2xs text-ink-muted mt-0.5">3.2.x</p>
            </div>
            <div class="bg-surface p-4">
              <p class="text-sm font-medium text-ink">Java</p>
              <p class="text-2xs text-ink-muted mt-0.5">21</p>
            </div>
            <div class="bg-surface p-4">
              <p class="text-sm font-medium text-ink">H2</p>
              <p class="text-2xs text-ink-muted mt-0.5">内存数据库</p>
            </div>
            <div class="bg-surface p-4">
              <p class="text-sm font-medium text-ink">Maven</p>
              <p class="text-2xs text-ink-muted mt-0.5">构建工具</p>
            </div>
          </div>
        </div>

        <div>
          <p class="text-2xs font-medium uppercase tracking-wide text-ink-muted mb-2">架构设计</p>
          <pre class="mono bg-ink text-white/90 p-4 rounded text-xs overflow-x-auto leading-relaxed">
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

        <div>
          <p class="text-2xs font-medium uppercase tracking-wide text-ink-muted mb-2">API 设计</p>
          <div class="border border-line rounded-lg overflow-hidden">
            <table class="w-full text-sm">
              <thead>
                <tr class="border-b border-line text-left text-2xs font-medium uppercase tracking-wide text-ink-muted">
                  <th class="px-4 py-2.5">方法</th>
                  <th class="px-4 py-2.5">路径</th>
                  <th class="px-4 py-2.5">描述</th>
                  <th class="px-4 py-2.5">状态码</th>
                </tr>
              </thead>
              <tbody class="divide-y divide-line">
                <tr class="hover:bg-base transition-colors">
                  <td class="px-4 py-3"><span class="tag bg-ok-soft text-ok">GET</span></td>
                  <td class="px-4 py-3 mono text-xs text-ink">/api/health</td>
                  <td class="px-4 py-3 text-ink-secondary">健康检查端点</td>
                  <td class="px-4 py-3 mono text-xs text-ink-muted">200 OK</td>
                </tr>
                <tr class="hover:bg-base transition-colors">
                  <td class="px-4 py-3"><span class="tag bg-ok-soft text-ok">GET</span></td>
                  <td class="px-4 py-3 mono text-xs text-ink">/api/health/detail</td>
                  <td class="px-4 py-3 text-ink-secondary">详细健康状态</td>
                  <td class="px-4 py-3 mono text-xs text-ink-muted">200 OK</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <div>
          <p class="text-2xs font-medium uppercase tracking-wide text-ink-muted mb-2">类设计</p>
          <div class="grid grid-cols-1 md:grid-cols-3 gap-2">
            <div class="border border-line rounded p-3 hover:border-line-strong transition-colors">
              <p class="mono text-xs font-medium text-ink">HealthController.java</p>
              <p class="text-xs text-ink-muted mt-1">控制器类，提供健康检查 REST API</p>
            </div>
            <div class="border border-line rounded p-3 hover:border-line-strong transition-colors">
              <p class="mono text-xs font-medium text-ink">HealthService.java</p>
              <p class="text-xs text-ink-muted mt-1">服务类，处理健康检查业务逻辑</p>
            </div>
            <div class="border border-line rounded p-3 hover:border-line-strong transition-colors">
              <p class="mono text-xs font-medium text-ink">HealthResponse.java</p>
              <p class="text-xs text-ink-muted mt-1">响应 DTO，封装健康检查结果</p>
            </div>
          </div>
        </div>
      </div>
    </section>

    <section class="card">
      <h2 class="px-5 py-3 border-b border-line text-sm font-medium text-ink">方案评审</h2>
      <div class="px-5 py-5 space-y-4">
        <div>
          <label class="block text-2xs font-medium uppercase tracking-wide text-ink-muted mb-1.5">评审意见</label>
          <textarea class="input resize-none" rows="3" placeholder="输入评审意见…"></textarea>
        </div>
        <div class="flex gap-2 justify-end">
          <button class="btn-secondary text-danger border-danger/30 hover:bg-danger-soft">驳回</button>
          <button class="btn-primary">批准</button>
        </div>
        <div class="border-l-2 border-accent bg-accent-soft px-3 py-2.5 rounded-r">
          <p class="text-xs text-ink-secondary"><span class="font-medium text-ink">AI 建议</span> · 方案设计合理，建议增加数据库连接池状态检查和缓存状态检查</p>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
</script>
