import { test, expect } from '@playwright/test'

/**
 * 全局导航与页面可达性：5 个页面均可通过顶部导航到达，路由高亮正确。
 */
test.describe('导航与布局', () => {
  test('默认路由重定向到 /analyze', async ({ page }) => {
    await page.goto('/')
    await expect(page).toHaveURL(/\/analyze$/)
  })

  const menus = [
    { path: '/analyze', name: 'RCA 分析', title: 'RCA 分析 · RCA Agent' },
    { path: '/history', name: '分析历史', title: '分析历史 · RCA Agent' },
    { path: '/recording', name: '录制明细', title: '录制明细 · RCA Agent' },
    { path: '/replay', name: '回放对比', title: '回放对比 · RCA Agent' },
    { path: '/health', name: '服务健康', title: '服务健康 · RCA Agent' }
  ]

  for (const menu of menus) {
    test(`导航到 ${menu.path}`, async ({ page }) => {
      await page.goto('/analyze')
      await page.getByRole('menuitem', { name: menu.name }).click()
      await expect(page).toHaveURL(new RegExp(menu.path + '$'))
      await expect(page).toHaveTitle(menu.title)
      // 当前菜单高亮
      await expect(page.getByRole('menuitem', { name: menu.name })).toHaveClass(/is-active/)
    })
  }

  test('品牌区展示平台名称', async ({ page }) => {
    await page.goto('/analyze')
    await expect(page.locator('.brand')).toContainText('RCA Agent · 根因分析平台')
  })
})
