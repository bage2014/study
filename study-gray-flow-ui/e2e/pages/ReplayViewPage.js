export class ReplayViewPage {
  constructor(page) {
    this.page = page
    this.sessionRows = page.locator('tr.session-row')
    this.emptyState = page.getByText('暂无回放会话')
    this.errorMessage = page.getByRole('alert')
    this.createButton = page.getByRole('button', { name: '新建回放' })
    this.createModal = page.getByRole('dialog')
    this.countInput = page.getByRole('dialog').getByRole('spinbutton')
    this.confirmButton = page.getByRole('dialog').getByRole('button', { name: '确认' })
    this.cancelButton = page.getByRole('dialog').getByRole('button', { name: '取消' })
    this.modalCloseButton = page.getByRole('dialog').getByRole('button', { name: '✕' })
    this.recordRows = page.locator('.record-row')
    this.diffContent = page.locator('.diff-pre')
  }

  async goto() {
    await this.page.goto('/replay')
  }

  async waitForLoad() {
    await this.page.waitForResponse(resp =>
      resp.url().includes('/api/replay/sessions') && resp.status() === 200
    )
  }

  async expandSession(index) {
    await this.sessionRows.nth(index).click()
  }

  async openCreateModal() {
    await this.createButton.click()
  }

  async createSession(count) {
    await this.createButton.click()
    await this.countInput.fill(String(count))
    await this.confirmButton.click()
  }

  diffButtonOf(recordIndex) {
    return this.recordRows.nth(recordIndex).getByRole('button', { name: '查看 Diff' })
  }

  collapseDiffButtonOf(recordIndex) {
    return this.recordRows.nth(recordIndex).getByRole('button', { name: '收起 Diff' })
  }
}