export class LogsViewPage {
  constructor(page) {
    this.page = page
    this.typeSelect = page.getByRole('combobox')
    this.searchButton = page.getByRole('button', { name: '查询' })
    this.traceInput = page.getByPlaceholder('输入 traceId 查调用链')
    this.traceButton = page.getByRole('button', { name: '查链路' })
    this.tableRows = page.locator('tbody tr').filter({ hasNot: page.locator('[colspan]') })
    this.emptyState = page.getByText('暂无数据')
    this.errorMessage = page.getByRole('alert')
    this.prevButton = page.getByRole('button', { name: '上一页' })
    this.nextButton = page.getByRole('button', { name: '下一页' })
    this.traceModal = page.getByRole('dialog')
    this.traceNodes = page.locator('.trace-node')
    this.traceModalCloseButton = page.getByRole('dialog').getByRole('button', { name: '✕' })
  }

  async goto() {
    await this.page.goto('/logs')
  }

  async waitForLoad() {
    await this.page.waitForResponse(resp =>
      resp.url().includes('/api/logs') &&
      !resp.url().includes('/trace/') &&
      resp.status() === 200
    )
  }

  async filterByType(type) {
    await this.typeSelect.selectOption(type)
    await this.searchButton.click()
    await this.waitForLoad()
  }

  async openTraceInput(traceId) {
    await this.traceInput.fill(traceId)
    await this.traceButton.click()
  }

  async openTraceByClickRow(rowIndex) {
    const traceCell = this.tableRows.nth(rowIndex).locator('.trace-cell')
    await traceCell.click()
  }

  async closeTraceModal() {
    await this.traceModalCloseButton.click()
  }

  paginationText() {
    return this.page.locator('.pagination span')
  }
}