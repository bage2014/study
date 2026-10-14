import { test, expect } from '@playwright/test'

/**
 * RCA 分析页：表单校验 + Mock 场景分析结果渲染（API 打桩，不依赖真实后端）。
 */

const mockResult = {
  analysisId: 'A-TEST-001',
  appId: 'order-service',
  scene: 'db_slow_query',
  analysisTime: '2026-10-13T10:00:00Z',
  alarmTime: '2026-10-13T09:59:00Z',
  alarmDescription: 'mock',
  resultLevel: 'CONFIRMED',
  llmUsed: false,
  conclusions: [
    {
      hypothesisId: 'H1',
      rank: 1,
      title: '数据库慢查询导致下单超时',
      targetType: 'DATABASE',
      targetName: 'order-db',
      symptom: '慢查询',
      confidence: 0.87,
      confirmed: true,
      rationale: 'P99 延迟与慢查询日志吻合',
      counterfactual: '若慢查询消失则超时消失',
      causalChain: ['慢查询', '连接池耗尽', '下单超时']
    }
  ],
  suggestions: {
    mitigation: ['扩容连接池'],
    remediation: ['优化索引'],
    prevention: ['增加慢查询告警']
  },
  stages: [
    { stage: 'Plan', status: 'SUCCESS', note: 'ok' },
    { stage: 'BroadScan', status: 'SUCCESS', note: 'ok' }
  ],
  hypotheses: [
    {
      id: 'H1',
      targetType: 'DATABASE',
      targetName: 'order-db',
      symptom: '慢查询',
      priorConfidence: 0.5,
      confidence: 0.87,
      desc: '慢查询'
    }
  ],
  timeline: [
    { time: '2026-10-13T09:59:00Z', kind: 'ALERT', source: 'alarm', description: '下单超时告警', relatedTarget: 'order-service' }
  ],
  evidences: [
    {
      id: 'E1',
      hypothesisId: 'H1',
      phase: 'BroadScan',
      toolName: 'SlowQueryLogTool',
      targetName: 'order-db',
      value: '12',
      deviationRatio: 6,
      verdict: 'SUPPORT'
    }
  ]
}

test.describe('RCA 分析页', () => {
  test('告警描述为空时拦截并提示', async ({ page }) => {
    await page.goto('/analyze')
    await page.getByRole('button', { name: '执行分析' }).click()
    await expect(page.locator('.el-message--warning')).toContainText('请填写告警描述')
    // 仍在空态，未发起请求
    await expect(page.locator('.el-empty')).toBeVisible()
  })

  test('Mock 场景一键分析渲染完整结果', async ({ page }) => {
    await page.route('**/api/rca/analyze/mock**', async (route) => {
      await route.fulfill({ json: mockResult })
    })

    await page.goto('/analyze')
    await page.getByRole('button', { name: 'Mock 场景一键分析' }).click()

    // 结果卡：等级标签 + LLM 状态
    await expect(page.locator('.el-tag', { hasText: 'CONFIRMED' }).first()).toBeVisible()
    await expect(page.getByText('LLM: 未调用')).toBeVisible()
    await expect(page.locator('.el-message--success')).toContainText('分析完成')

    // 结论 Tab：结论标题 + 置信度 + 处置建议三栏
    await expect(page.getByText('数据库慢查询导致下单超时')).toBeVisible()
    await expect(page.getByText('置信度 87.0%')).toBeVisible()
    await expect(page.getByText('止血')).toBeVisible()
    await expect(page.getByText('扩容连接池')).toBeVisible()

    // 阶段轨迹 Tab
    await page.getByRole('tab', { name: '阶段轨迹' }).click()
    await expect(page.locator('.el-table__row', { hasText: 'BroadScan' }).first()).toBeVisible()

    // 假设全景 Tab
    await page.getByRole('tab', { name: '假设全景' }).click()
    await expect(page.locator('.el-table__row', { hasText: 'order-db' }).first()).toBeVisible()

    // 时间线 Tab
    await page.getByRole('tab', { name: '时间线' }).click()
    await expect(page.getByText('下单超时告警').first()).toBeVisible()

    // 证据 Tab
    await page.getByRole('tab', { name: '证据' }).click()
    await expect(page.locator('.el-table__row', { hasText: 'SlowQueryLogTool' }).first()).toBeVisible()
    await expect(page.locator('.el-tag', { hasText: 'SUPPORT' }).first()).toBeVisible()
  })

  test('后端报错时展示错误提示', async ({ page }) => {
    await page.route('**/api/rca/analyze', async (route) => {
      await route.fulfill({ status: 500, json: { message: 'boom' } })
    })

    await page.goto('/analyze')
    await page.getByPlaceholder(/描述告警现象/).fill('下单接口超时')
    await page.getByRole('button', { name: '执行分析' }).click()
    await expect(page.locator('.el-message--error')).toContainText('分析失败')
  })
})
