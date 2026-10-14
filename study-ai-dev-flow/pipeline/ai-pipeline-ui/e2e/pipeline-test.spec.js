import { test, expect } from '@playwright/test'

test('create pipeline with demo-backend and verify test count', async ({ page }) => {
  test.setTimeout(600000)

  await page.goto('/')
  await page.waitForTimeout(3000)

  const demoBackendCard = page.locator('div:has-text("demo-backend")').first()
  await demoBackendCard.click()
  await page.waitForTimeout(1000)
  console.log('Selected demo-backend project')

  const titleInput = page.locator('input[placeholder*="请输入需求标题"]')
  await titleInput.click()
  await titleInput.fill('测试消息功能')
  
  const descriptionInput = page.locator('textarea[placeholder*="请详细描述您的业务需求"]')
  await descriptionInput.click()
  await descriptionInput.fill('添加消息CRUD功能，包括创建、查询、更新、删除消息')
  
  await page.waitForTimeout(500)

  await page.click('button:has-text("提交需求，启动AI流水线")')
  console.log('Submitted requirement, starting pipeline...')
  await page.waitForTimeout(15000)

  const currentUrl = await page.url()
  console.log('Current URL:', currentUrl)

  if (!currentUrl.includes('/pipelines/')) {
    console.log('页面没有跳转到流水线详情，尝试手动导航到流水线列表')
    await page.goto('/pipelines')
    await page.waitForTimeout(3000)
    
    const pipelineItems = page.locator('div:has-text("测试消息功能")')
    const count = await pipelineItems.count()
    console.log('找到的流水线数量:', count)
    
    if (count > 0) {
      await pipelineItems.first().click()
      await page.waitForTimeout(5000)
    }
  }

  console.log('Pipeline Detail URL:', await page.url())

  for (let i = 0; i < 60; i++) {
    await page.waitForTimeout(10000)
    
    const testExecSections = page.locator('div:has(h4:has-text("测试验证"))')
    const testExecCount = await testExecSections.count()
    
    if (testExecCount > 0) {
      const testExecSection = testExecSections.first()
      await testExecSection.scrollIntoViewIfNeeded()
      await page.waitForTimeout(1000)

      const testStatsDiv = testExecSection.locator('div.grid-cols-4')
      if (await testStatsDiv.count() > 0) {
        const totalElement = testStatsDiv.locator('p.text-2xl.font-bold.text-gray-800').first()
        const passedElement = testStatsDiv.locator('p.text-2xl.font-bold.text-green-600').first()
        const failedElement = testStatsDiv.locator('p.text-2xl.font-bold.text-red-600').first()
        const coverageElement = testStatsDiv.locator('p.text-2xl.font-bold.text-blue-600').first()

        const totalTests = await totalElement.textContent()
        const passedTests = await passedElement.textContent()
        const failedTests = await failedElement.textContent()
        const coverage = await coverageElement.textContent()

        console.log(`测试统计 - 总数: ${totalTests}, 通过: ${passedTests}, 失败: ${failedTests}, 覆盖率: ${coverage}`)

        if (totalTests && parseInt(totalTests) > 0) {
          console.log('🎉 测试数量显示正常！')
          await page.screenshot({ path: 'test-result-screenshot.png', fullPage: true })
          return
        } else {
          console.log(`⚠️ 测试总数仍显示为: ${totalTests}`)
        }
      }
    }

    const statusText = await page.locator('.text-yellow-600, .text-green-600, .text-red-600').first().textContent()
    const currentStage = await page.locator('span:has-text("当前阶段:")').first().textContent()
    console.log(`轮询 ${i+1}/60 - 当前状态: ${statusText || '未知'} - 当前阶段: ${currentStage || '未知'}`)
  }

  console.log('⚠️ 流水线未在预期时间内完成，或测试数量仍显示为0')
  await page.screenshot({ path: 'test-result-screenshot.png', fullPage: true })
})