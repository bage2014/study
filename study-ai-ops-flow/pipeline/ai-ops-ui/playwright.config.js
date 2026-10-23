import { defineConfig, devices } from '@playwright/test'

export default defineConfig({
  testDir: './e2e',
  // 单 worker 顺序执行：整个测试过程只打开一次浏览器
  fullyParallel: false,
  forbidOnly: !!process.env.CI,
  retries: process.env.CI ? 2 : 0,
  workers: 1,
  use: {
    baseURL: 'http://localhost:8081',
    trace: 'on-first-retry',
    screenshot: 'on',
    video: 'on-first-retry'
  },
  projects: [
    {
      name: 'chromium',
      use: { ...devices['Desktop Chrome'] }
    }
  ]
})