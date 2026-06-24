import { test, expect } from '@playwright/test'
import { GrayHomePage } from './pages/GrayHomePage.js'

const mockLogs = { content: [{ id: 1 }], totalElements: 42 }
const mockSessions = [{ sessionId: 's1' }, { sessionId: 's2' }, { sessionId: 's3' }]

test.beforeEach(async ({ page }) => {
  await page.route('**/api/logs**', route =>
    route.fulfill({ status: 200, contentType: 'application/json', body: JSON.stringify(mockLogs) })
  )
  await page.route('**/api/replay/sessions', route =>
    route.fulfill({ status: 200, contentType: 'application/json', body: JSON.stringify(mockSessions) })
  )
})

test.describe('首页概览', () => {
  test('显示日志总条数', async ({ page }) => {
    const home = new GrayHomePage(page)
    await home.goto()
    await home.waitForLoad()
    await expect(home.logCountValue()).toContainText('42')
  })

  test('显示回放会话总数', async ({ page }) => {
    const home = new GrayHomePage(page)
    await home.goto()
    await home.waitForLoad()
    await expect(home.sessionCountValue()).toContainText('3')
  })

  test('点击「查看日志」跳转到日志页', async ({ page }) => {
    const home = new GrayHomePage(page)
    await home.goto()
    await home.waitForLoad()
    await home.goToLogsButton.click()
    await expect(page).toHaveURL('/logs')
  })

  test('点击「查看回放」跳转到回放页', async ({ page }) => {
    const home = new GrayHomePage(page)
    await home.goto()
    await home.waitForLoad()
    await home.goToReplayButton.click()
    await expect(page).toHaveURL('/replay')
  })

  test('接口失败时保持 — 展示不崩溃', async ({ page }) => {
    await page.route('**/api/logs**', route => route.fulfill({ status: 500 }))
    await page.route('**/api/replay/sessions', route => route.fulfill({ status: 500 }))
    const home = new GrayHomePage(page)
    await home.goto()
    await expect(home.logCountValue()).toContainText('—')
    await expect(home.sessionCountValue()).toContainText('—')
  })
})