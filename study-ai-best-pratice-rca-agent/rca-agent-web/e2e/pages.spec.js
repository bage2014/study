import { test, expect } from '@playwright/test'

/**
 * 分析历史 / 录制明细 / 回放对比 / 服务健康页面（API 打桩）。
 */
test.describe('分析历史页', () => {
  test('列表渲染与空态', async ({ page }) => {
    await page.route('**/api/rca/history**', async (route) => {
      if (route.request().method() === 'GET' && !/\/history\/[^/]+/.test(route.request().url())) {
        await route.fulfill({
          json: {
            items: [
              {
                analysisId: 'A-1',
                appId: 'order-service',
                scene: 'db_slow_query',
                resultLevel: 'CONFIRMED',
                recorded: true,
                createdAt: '2026-10-13T10:00:00Z'
              }
            ],
            total: 1
          }
        })
      } else {
        await route.fallback()
      }
    })

    await page.goto('/history')
    await expect(page.getByText('RCA 分析历史')).toBeVisible()
    await expect(page.locator('.el-table__row', { hasText: 'A-1' })).toBeVisible()
    await expect(page.locator('.el-table__row .el-tag', { hasText: 'CONFIRMED' })).toBeVisible()
  })
})

test.describe('录制明细页', () => {
  test('录制列表与明细空态提示', async ({ page }) => {
    await page.route('**/api/rca/recordings', async (route) => {
      await route.fulfill({
        json: [
          { recordingId: 'R-1', analysisId: 'A-1', appId: 'order-service', interactionCount: 5, createdAt: '2026-10-13T10:00:00Z' }
        ]
      })
    })

    await page.goto('/recording')
    await expect(page.getByText('录制列表')).toBeVisible()
    await expect(page.getByText('点击上方录制查看交互明细')).toBeVisible()
    await expect(page.locator('.el-table__row', { hasText: 'R-1' })).toBeVisible()
  })
})

test.describe('回放对比页', () => {
  test('初始空态：无可回放记录时执行按钮禁用', async ({ page }) => {
    await page.route('**/api/rca/history**', async (route) => {
      await route.fulfill({ json: { items: [], total: 0 } })
    })

    await page.goto('/replay')
    await expect(page.getByText('RCA 回放')).toBeVisible()
    await expect(page.getByText('选择一条已录制的分析')).toBeVisible()
    await expect(page.getByRole('button', { name: '执行回放' })).toBeDisabled()
  })
})

test.describe('服务健康页', () => {
  test('UP 状态渲染', async ({ page }) => {
    await page.route('**/api/rca/health', async (route) => {
      await route.fulfill({ json: { status: 'UP', service: 'rca-agent-backend' } })
    })

    await page.goto('/health')
    await expect(page.getByText('服务运行中')).toBeVisible()
    await expect(page.getByText('rca-agent-backend').first()).toBeVisible()
  })

  test('后端不可达时错误提示', async ({ page }) => {
    await page.route('**/api/rca/health', async (route) => {
      await route.abort()
    })

    await page.goto('/health')
    await expect(page.locator('.el-message--error')).toContainText('健康检查失败')
    await expect(page.getByText('点击刷新检查后端服务健康状态')).toBeVisible()
  })
})
