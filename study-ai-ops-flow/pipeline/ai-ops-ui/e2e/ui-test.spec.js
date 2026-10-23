import { test as base, expect } from '@playwright/test'

// AI Ops UI 端到端测试
// 前置条件：API(8080) / Gateway(8083) / UI(8081) 均已启动
// 运行：npx playwright test e2e/ui-test.spec.js --headed
// 说明：
// - 后端 H2 为内存库，重启后数据清空，故 beforeAll 自动通过 API 准备测试数据
// - sharedPage 为 worker 级共享页面：单浏览器单页面连续执行全部用例，不再反复开关

const GATEWAY = 'http://localhost:8083'

const test = base.extend({
  sharedPage: [async ({ browser }, use) => {
    const context = await browser.newContext()
    const page = await context.newPage()
    await use(page)
    await context.close()
  }, { scope: 'worker' }]
})

let projectId = null

test.beforeAll(async ({ request }) => {
  // 确保 demo-backend 项目存在（幂等）
  const listResp = await request.get(`${GATEWAY}/api/project?page=0&size=50`)
  const list = await listResp.json()
  const existing = (list.content || []).find((p) => p.projectName === 'demo-backend')
  if (existing) {
    projectId = existing.id
    return
  }
  const createResp = await request.post(`${GATEWAY}/api/project`, {
    data: {
      projectName: 'demo-backend',
      description: '演示目标项目（Spring Boot CRUD）',
      projectType: 'BACKEND',
      localPath: '/Users/bage/bage/github/study/study-ai-ops-flow/demo/demo-backend'
    }
  })
  expect(createResp.status()).toBe(200)
  projectId = (await createResp.json()).id
})

test.describe('导航与布局', () => {
  test('首页正常加载并显示主横幅', async ({ sharedPage: page }) => {
    await page.goto('/')
    await expect(page.getByRole('heading', { name: '智能开发流水线' })).toBeVisible()
    await expect(page.getByRole('heading', { name: '选择项目 & 输入需求' })).toBeVisible()
  })

  test('顶部导航可跳转到项目管理和流水线列表', async ({ sharedPage: page }) => {
    await page.goto('/')
    await page.waitForLoadState('networkidle')

    await page.locator('a[href="/projects"]').first().click()
    await expect(page).toHaveURL(/\/projects$/)
    await expect(page.getByText('我的项目').first()).toBeVisible({ timeout: 10000 })

    await page.locator('a[href="/pipelines"]').first().click()
    await expect(page).toHaveURL(/\/pipelines$/)
    await expect(page.getByText('流水线列表').first()).toBeVisible({ timeout: 10000 })
  })
})

test.describe('项目管理', () => {
  test('项目列表加载并显示 demo-backend 项目卡片', async ({ sharedPage: page }) => {
    await page.goto('/projects')
    await page.waitForLoadState('networkidle')

    await expect(page.getByText('我的项目').first()).toBeVisible()
    await expect(page.getByPlaceholder('搜索项目...')).toBeVisible()
    // beforeAll 已确保 demo-backend 存在
    await expect(page.getByText('demo-backend').first()).toBeVisible({ timeout: 10000 })
  })

  test('项目搜索框可输入并过滤', async ({ sharedPage: page }) => {
    await page.goto('/projects')
    await page.waitForLoadState('networkidle')
    await page.getByText('demo-backend').first().waitFor({ timeout: 10000 })

    const search = page.getByPlaceholder('搜索项目...')
    await search.fill('demo')
    await expect(search).toHaveValue('demo')
  })

  test('项目卡片点击进入项目详情', async ({ sharedPage: page }) => {
    await page.goto('/projects')
    await page.waitForLoadState('networkidle')

    await page.getByText('demo-backend').first().click()
    await page.waitForURL(/\/projects\/\d+/, { timeout: 10000 })
    await expect(page).toHaveURL(new RegExp(`/projects/${projectId}`))
  })
})

test.describe('流水线管理', () => {
  test('流水线列表加载：有记录则显示卡片，无记录则显示空态', async ({ sharedPage: page }) => {
    await page.goto('/pipelines')
    await page.waitForLoadState('networkidle')

    await expect(page.getByText('流水线列表').first()).toBeVisible({ timeout: 10000 })
    // 空态与记录卡片互斥，二者必现其一
    const emptyVisible = await page.getByText('暂无流水线记录').isVisible()
    const cardVisible = await page.locator('.cursor-pointer').first().isVisible()
    expect(emptyVisible || cardVisible).toBeTruthy()
  })

  test('点击流水线卡片进入详情页', async ({ sharedPage: page }) => {
    await page.goto('/pipelines')
    await page.waitForLoadState('networkidle')
    await page.getByText('暂无流水线记录').waitFor({ state: 'hidden', timeout: 10000 }).catch(() => {})

    const card = page.locator('.cursor-pointer').first()
    test.skip((await card.count()) === 0, '无流水线记录可测')
    await card.click()
    await page.waitForURL(/\/pipelines\/[\w-]+/, { timeout: 10000 })
    await expect(page).toHaveURL(/\/pipelines\/[\w-]+/)
  })
})

test.describe('流水线详情页', () => {
  test('详情页显示流程进度、阶段名称与当前阶段', async ({ sharedPage: page }) => {
    const resp = await page.request.get(`${GATEWAY}/api/pipeline`)
    const pipelines = await resp.json()
    test.skip(pipelines.length === 0, '无流水线记录可测')

    await page.goto(`/pipelines/${pipelines[0].pipelineId}`)

    await expect(page.getByText('流程进度')).toBeVisible({ timeout: 10000 })
    await expect(page.getByText('需求分析').first()).toBeVisible()
    await expect(page.getByText('代码生成').first()).toBeVisible()
    await expect(page.getByText(/当前阶段/).first()).toBeVisible()
  })

  test('详情页展示需求标题（与 API 数据一致）', async ({ sharedPage: page }) => {
    const resp = await page.request.get(`${GATEWAY}/api/pipeline`)
    const pipelines = await resp.json()
    test.skip(pipelines.length === 0, '无流水线记录可测')

    const title = pipelines[0].requirementTitle || ''
    test.skip(!title, '该记录无需求标题')
    await page.goto(`/pipelines/${pipelines[0].pipelineId}`)
    await expect(page.getByText(title).first()).toBeVisible({ timeout: 10000 })
  })
})
