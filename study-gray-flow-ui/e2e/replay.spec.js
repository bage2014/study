import { test, expect } from '@playwright/test'
import { ReplayViewPage } from './pages/ReplayViewPage.js'

const SESSION_ID = 'sess-uuid-0000-0001'

const mockSessions = [
  {
    id: 1,
    sessionId: SESSION_ID,
    status: 'COMPLETED',
    totalCount: 2,
    completedCount: 2,
    createdAt: '2026-06-10T10:00:00Z',
  },
]

const mockSessionDetail = {
  ...mockSessions[0],
  records: [
    {
      id: 1,
      httpMethod: 'GET',
      requestUri: '/api/users',
      matchResult: 'MATCH',
      chainMatchResult: 'CHAIN_MATCH',
      diffPatch: null,
      errorMsg: null,
    },
    {
      id: 2,
      httpMethod: 'POST',
      requestUri: '/api/users',
      matchResult: 'MISMATCH',
      chainMatchResult: 'CHAIN_MISMATCH',
      diffPatch: JSON.stringify([{ path: '/name', expected: 'Alice', actual: 'Bob' }]),
      errorMsg: null,
    },
  ],
}

function mockSessionRoutes(page, { sessions = mockSessions, detail = mockSessionDetail } = {}) {
  return Promise.all([
    page.route('**/api/replay/sessions/**', route =>
      route.fulfill({ status: 200, contentType: 'application/json', body: JSON.stringify(detail) })
    ),
    page.route('**/api/replay/sessions', async route => {
      if (route.request().method() === 'POST') {
        await route.fulfill({ status: 201 })
      } else {
        await route.fulfill({ status: 200, contentType: 'application/json', body: JSON.stringify(sessions) })
      }
    }),
  ])
}

test.describe('回放管理', () => {
  test('正常加载会话列表', async ({ page }) => {
    await mockSessionRoutes(page)
    const replayPage = new ReplayViewPage(page)
    await replayPage.goto()
    await replayPage.waitForLoad()

    await expect(replayPage.sessionRows).toHaveCount(1)
    await expect(replayPage.sessionRows.first()).toContainText('COMPLETED')
  })

  test('无会话时显示空状态', async ({ page }) => {
    await mockSessionRoutes(page, { sessions: [] })
    const replayPage = new ReplayViewPage(page)
    await replayPage.goto()
    await replayPage.waitForLoad()

    await expect(replayPage.emptyState).toBeVisible()
  })

  test('列表接口失败时显示错误提示', async ({ page }) => {
    await page.route('**/api/replay/sessions/**', route => route.fulfill({ status: 500 }))
    await page.route('**/api/replay/sessions', route => route.fulfill({ status: 500 }))

    const replayPage = new ReplayViewPage(page)
    await replayPage.goto()
    await expect(replayPage.errorMessage).toBeVisible()
  })

  test('点击会话行展开回放记录', async ({ page }) => {
    await mockSessionRoutes(page)
    const replayPage = new ReplayViewPage(page)
    await replayPage.goto()
    await replayPage.waitForLoad()

    await replayPage.expandSession(0)
    await page.waitForResponse(resp => resp.url().includes(`/api/replay/sessions/${SESSION_ID}`))

    await expect(replayPage.recordRows).toHaveCount(2)
    await expect(replayPage.recordRows.first()).toContainText('GET /api/users')
  })

  test('回放记录展示匹配/不匹配图标', async ({ page }) => {
    await mockSessionRoutes(page)
    const replayPage = new ReplayViewPage(page)
    await replayPage.goto()
    await replayPage.waitForLoad()
    await replayPage.expandSession(0)

    await expect(replayPage.recordRows.first()).toContainText('✅')
    await expect(replayPage.recordRows.nth(1)).toContainText('❌')
  })

  test('点击会话行再次点击收起', async ({ page }) => {
    await mockSessionRoutes(page)
    const replayPage = new ReplayViewPage(page)
    await replayPage.goto()
    await replayPage.waitForLoad()

    await replayPage.expandSession(0)
    await expect(replayPage.recordRows).toHaveCount(2)

    await replayPage.expandSession(0)
    await expect(replayPage.recordRows).toHaveCount(0)
  })

  test('点击「查看 Diff」展示 diff 内容', async ({ page }) => {
    await mockSessionRoutes(page)
    const replayPage = new ReplayViewPage(page)
    await replayPage.goto()
    await replayPage.waitForLoad()
    await replayPage.expandSession(0)

    await replayPage.diffButtonOf(1).click()
    await expect(replayPage.diffContent).toBeVisible()
    await expect(replayPage.diffContent).toContainText('Alice')
  })

  test('点击「收起 Diff」隐藏 diff 内容', async ({ page }) => {
    await mockSessionRoutes(page)
    const replayPage = new ReplayViewPage(page)
    await replayPage.goto()
    await replayPage.waitForLoad()
    await replayPage.expandSession(0)

    await replayPage.diffButtonOf(1).click()
    await expect(replayPage.diffContent).toBeVisible()

    await replayPage.collapseDiffButtonOf(1).click()
    await expect(replayPage.diffContent).toBeHidden()
  })

  test('无 diff 数据时点击「查看 Diff」显示"无差异"', async ({ page }) => {
    await mockSessionRoutes(page)
    const replayPage = new ReplayViewPage(page)
    await replayPage.goto()
    await replayPage.waitForLoad()
    await replayPage.expandSession(0)

    await replayPage.diffButtonOf(0).click()
    await expect(replayPage.diffContent).toContainText('无差异')
  })

  test('新建回放：打开 Modal 填写条数后确认', async ({ page }) => {
    await mockSessionRoutes(page)
    const replayPage = new ReplayViewPage(page)
    await replayPage.goto()
    await replayPage.waitForLoad()

    await replayPage.openCreateModal()
    await expect(replayPage.createModal).toBeVisible()
    await expect(replayPage.createModal).toContainText('新建回放会话')

    await replayPage.countInput.fill('20')
    await replayPage.confirmButton.click()
    await expect(replayPage.createModal).toBeHidden()
  })

  test('新建回放：点击取消关闭 Modal', async ({ page }) => {
    await mockSessionRoutes(page)
    const replayPage = new ReplayViewPage(page)
    await replayPage.goto()
    await replayPage.waitForLoad()

    await replayPage.openCreateModal()
    await expect(replayPage.createModal).toBeVisible()
    await replayPage.cancelButton.click()
    await expect(replayPage.createModal).toBeHidden()
  })

  test('新建回放：点击 ✕ 关闭 Modal', async ({ page }) => {
    await mockSessionRoutes(page)
    const replayPage = new ReplayViewPage(page)
    await replayPage.goto()
    await replayPage.waitForLoad()

    await replayPage.openCreateModal()
    await expect(replayPage.createModal).toBeVisible()
    await replayPage.modalCloseButton.click()
    await expect(replayPage.createModal).toBeHidden()
  })

  test('新建回放成功后刷新会话列表', async ({ page }) => {
    const refreshedSessions = [
      ...mockSessions,
      { id: 2, sessionId: 'sess-new', status: 'RUNNING', totalCount: 20, completedCount: 0, createdAt: '2026-06-10T11:00:00Z' },
    ]

    let callCount = 0
    await page.route('**/api/replay/sessions/**', route =>
      route.fulfill({ status: 200, contentType: 'application/json', body: JSON.stringify(mockSessionDetail) })
    )
    await page.route('**/api/replay/sessions', async route => {
      if (route.request().method() === 'POST') {
        callCount++
        await route.fulfill({ status: 201 })
      } else {
        const sessions = callCount > 0 ? refreshedSessions : mockSessions
        await route.fulfill({ status: 200, contentType: 'application/json', body: JSON.stringify(sessions) })
      }
    })

    const replayPage = new ReplayViewPage(page)
    await replayPage.goto()
    await replayPage.waitForLoad()
    await replayPage.createSession(20)

    await expect(replayPage.sessionRows).toHaveCount(2)
  })
})