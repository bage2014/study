export class GrayHomePage {
  constructor(page) {
    this.page = page
    this.logCountCard = page.locator('.card').filter({ hasText: '日志总条数' })
    this.sessionCountCard = page.locator('.card').filter({ hasText: '回放会话总数' })
    this.goToLogsButton = page.getByRole('button', { name: '查看日志' })
    this.goToReplayButton = page.getByRole('button', { name: '查看回放' })
  }

  async goto() {
    await this.page.goto('/')
  }

  async waitForLoad() {
    await this.page.waitForResponse(resp =>
      resp.url().includes('/api/logs') && resp.status() === 200
    )
  }

  logCountValue() {
    return this.logCountCard.locator('.card-value')
  }

  sessionCountValue() {
    return this.sessionCountCard.locator('.card-value')
  }
}