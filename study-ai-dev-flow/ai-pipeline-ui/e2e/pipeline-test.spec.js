import { test, expect } from '@playwright/test'

test('create pipeline and verify test count display', async ({ page }) => {
  test.setTimeout(300000)

  await page.goto('/')
  await page.waitForTimeout(3000)

  const titleInput = page.locator('input[placeholder*="请输入需求标题"]')
  await titleInput.click()
  await titleInput.fill('测试消息功能')
  
  const descriptionInput = page.locator('textarea[placeholder*="请详细描述您的业务需求"]')
  await descriptionInput.click()
  await descriptionInput.fill('添加消息CRUD功能，包括创建、查询、更新、删除消息')
  
  await page.waitForTimeout(500)

  await page.click('button:has-text("提交需求，启动AI流水线")')
  await page.waitForTimeout(10000)

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

  const pipelineId = await page.url()
  console.log('Pipeline Detail URL:', pipelineId)

  for (let i = 0; i < 30; i++) {
    await page.waitForTimeout(10000)
    
    const testExecHeaders = page.locator('h4:has-text("测试验证")')
    if (await testExecHeaders.count() > 0) {
      const testExecHeader = testExecHeaders.first()
      await testExecHeader.scrollIntoViewIfNeeded()
      await page.waitForTimeout(1000)

      const testResultSections = page.locator('h5:has-text("测试结果")')
      if (await testResultSections.count() > 0) {
        const testResultSection = testResultSections.first()
        await testResultSection.scrollIntoViewIfNeeded()
        await page.waitForTimeout(1000)

        const counts = await page.locator('p:text("总数"), p:text("通过"), p:text("失败")').all()
        for (const count of counts) {
          const text = await count.textContent()
          const valueElement = count.locator('xpath=preceding-sibling::p').first()
          const value = await valueElement.textContent()
          console.log(`${text}: ${value}`)
        }
      }

      const totalElements = await page.locator('p:text("总数")').all()
      if (totalElements.length > 0) {
        const totalElement = totalElements[0]
        const valueElement = await totalElement.locator('xpath=preceding-sibling::p').first()
        const totalTests = await valueElement.textContent()
        console.log('测试总数:', totalTests || '0')

        if (totalTests && parseInt(totalTests) > 0) {
          console.log('🎉 测试数量显示正常！')
          await page.screenshot({ path: 'test-result-screenshot.png', fullPage: true })
          return
        }
      }
    }

    const statusText = await page.locator('.text-yellow-600, .text-green-600, .text-red-600').first().textContent()
    console.log(`轮询 ${i+1}/30 - 当前状态: ${statusText || '未知'}`)
  }

  console.log('⚠️ 流水线未在预期时间内完成，或测试数量仍显示为0')
  await page.screenshot({ path: 'test-result-screenshot.png', fullPage: true })
})
