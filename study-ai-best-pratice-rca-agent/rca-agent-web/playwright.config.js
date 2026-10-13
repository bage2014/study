import { defineConfig } from '@playwright/test'

export default defineConfig({
  testDir: './e2e',
  timeout: 30000,
  retries: 0,
  reporter: [['list']],
  use: {
    baseURL: 'http://localhost:5173',
    browserName: 'chromium',
    viewport: { width: 1440, height: 900 },
    screenshot: 'only-on-failure'
  }
})
