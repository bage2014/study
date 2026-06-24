import { test, expect } from '@playwright/test'
import { LogsViewPage } from './pages/LogsViewPage.js'

const TRACE_ID = 'abc12345-dead-beef-0000-000000000001'

const mockLogs = {
  content: [
    {
      id: 1,
      logType: 'CONTROLLER',
      traceId: TRACE_ID,
      className: 'UserController',
      methodName: 'list',
      durationMs: 12,
      createdAt: '2026-06-10T10:00:00Z',
    },
    {
      id: 2,
      logType: 'PROXY',
      traceId: TRACE_ID,
      className: 'UserServiceImpl',
      methodName: 'findAll',
      durationMs: 8,
      createdAt: '2026-06-10T10:00:01Z',
    },
  ],
  totalElements: 2,
}

const mockTrace = [
  {
    id: 1,
    logType: 'CONTROLLER',
    traceId: TRACE_ID,
    className: 'UserController',
    methodName: 'list',
    durationMs: 12,
    callIndex: 0,
    args: '[]',
    resultSummary: '[{"id":1}]',
    errorMsg: null,
  },
  {
    id: 2,
    logType: 'PROXY',
    traceId: TRACE_ID,
    className: 'UserServiceImpl',
    methodName: 'findAll',
    durationMs: 8,
    callIndex: 1,
    args: '[]',
    resultSummary: '[{"id":1}]',
    errorMsg: null,
  },
]

function mockRoutes(page, { logsResp = mockLogs, traceResp = mockTrace } = {}) {
  return Promise.all([
    page.route('**/api/logs/trace/**', route =>
      route.fulfill({ status: 200, contentType: 'application/json', body: JSON.stringify(traceResp) })
    ),
    page.route('**/api/logs**', route =>
      route.fulfill({ status: 200, contentType: 'application/json', body: JSON.stringify(logsResp) })
    ),
  ])
}

test.describe('日志查询', () => {
  test('正常加载日志列表', async ({ page }) => {
    await mockRoutes(page)
    const logsPage = new LogsViewPage(page)
    await logsPage.goto()
    await logsPage.waitForLoad()

    await expect(logsPage.tableRows).toHaveCount(2)
    await expect(logsPage.tableRows.first()).toContainText('UserController.list')
  })

  test('按 CONTROLLER 类型筛选', async ({ page }) => {
    const controllerOnly = { content: [mockLogs.content[0]], totalElements: 1 }
    await page.route('**/api/logs/trace/**', route =>
      route.fulfill({ status: 200, contentType: 'application/json', body: JSON.stringify(mockTrace) })
    )
    await page.route('**/api/logs**', async route => {
      const url = route.request().url()
      const resp = url.includes('logType=CONTROLLER') ? controllerOnly : mockLogs
      await route.fulfill({ status: 200, contentType: 'application/json', body: JSON.stringify(resp) })
    })

    const logsPage = new LogsViewPage(page)
    await logsPage.goto()
    await logsPage.filterByType('CONTROLLER')

    await expect(logsPage.tableRows).toHaveCount(1)
    await expect(logsPage.tableRows.first()).toContainText('CONTROLLER')
  })

  test('无数据时显示空状态', async ({ page }) => {
    await mockRoutes(page, { logsResp: { content: [], totalElements: 0 } })
    const logsPage = new LogsViewPage(page)
    await logsPage.goto()
    await logsPage.waitForLoad()

    await expect(logsPage.emptyState).toBeVisible()
    await expect(logsPage.tableRows).toHaveCount(0)
  })

  test('列表接口失败时显示错误提示', async ({ page }) => {
    await page.route('**/api/logs/trace/**', route =>
      route.fulfill({ status: 200, contentType: 'application/json', body: JSON.stringify([]) })
    )
    await page.route('**/api/logs**', route => route.fulfill({ status: 500 }))

    const logsPage = new LogsViewPage(page)
    await logsPage.goto()
    await expect(logsPage.errorMessage).toBeVisible()
  })

  test('输入 traceId 后点击「查链路」打开调用链 Modal', async ({ page }) => {
    await mockRoutes(page)
    const logsPage = new LogsViewPage(page)
    await logsPage.goto()
    await logsPage.waitForLoad()

    await logsPage.openTraceInput(TRACE_ID)
    await expect(logsPage.traceModal).toBeVisible()
  })

  test('调用链 Modal 展示节点列表', async ({ page }) => {
    await mockRoutes(page)
    const logsPage = new LogsViewPage(page)
    await logsPage.goto()
    await logsPage.waitForLoad()

    await logsPage.openTraceInput(TRACE_ID)
    await expect(logsPage.traceModal).toBeVisible()
    await expect(logsPage.traceNodes).toHaveCount(2)
    await expect(logsPage.traceNodes.first()).toContainText('UserController.list')
    await expect(logsPage.traceNodes.first()).toContainText('12ms')
  })

  test('点击 TraceId 单元格打开调用链', async ({ page }) => {
    await mockRoutes(page)
    const logsPage = new LogsViewPage(page)
    await logsPage.goto()
    await logsPage.waitForLoad()

    await logsPage.openTraceByClickRow(0)
    await expect(logsPage.traceModal).toBeVisible()
  })

  test('点击 ✕ 关闭 Modal', async ({ page }) => {
    await mockRoutes(page)
    const logsPage = new LogsViewPage(page)
    await logsPage.goto()
    await logsPage.waitForLoad()

    await logsPage.openTraceInput(TRACE_ID)
    await expect(logsPage.traceModal).toBeVisible()
    await logsPage.closeTraceModal()
    await expect(logsPage.traceModal).toBeHidden()
  })

  test('点击 Modal 背景关闭 Modal', async ({ page }) => {
    await mockRoutes(page)
    const logsPage = new LogsViewPage(page)
    await logsPage.goto()
    await logsPage.waitForLoad()

    await logsPage.openTraceInput(TRACE_ID)
    await expect(logsPage.traceModal).toBeVisible()
    await page.locator('.modal-backdrop').click({ position: { x: 5, y: 5 } })
    await expect(logsPage.traceModal).toBeHidden()
  })

  test('分页按钮初始状态：上一页禁用、有数据时下一页可用', async ({ page }) => {
    const multiPage = { content: mockLogs.content, totalElements: 100 }
    await mockRoutes(page, { logsResp: multiPage })
    const logsPage = new LogsViewPage(page)
    await logsPage.goto()
    await logsPage.waitForLoad()

    await expect(logsPage.prevButton).toBeDisabled()
    await expect(logsPage.nextButton).toBeEnabled()
  })

  test('只有一页时下一页禁用', async ({ page }) => {
    await mockRoutes(page)
    const logsPage = new LogsViewPage(page)
    await logsPage.goto()
    await logsPage.waitForLoad()

    await expect(logsPage.nextButton).toBeDisabled()
  })
})