import { Builder, By, until } from 'selenium-webdriver';
import chrome from 'selenium-webdriver/chrome.js';

export class SeleniumTestRunner {
  constructor(testName) {
    this.testName = testName;
    this.driver = null;
    this.testsPassed = 0;
    this.testsFailed = 0;
    this.baseUrl = 'http://localhost:5173';
  }

  async setup(isHeadless = false) {
    const chromeOptions = new chrome.Options();
    if (isHeadless) {
      chromeOptions.addArguments('--headless');
    }
    chromeOptions.addArguments('--no-sandbox');
    chromeOptions.addArguments('--disable-dev-shm-usage');
    chromeOptions.addArguments('--window-size=1920,1080');

    this.driver = await new Builder()
      .forBrowser('chrome')
      .setChromeOptions(chromeOptions)
      .build();

    console.log(`\n=== ${this.testName} ===\n`);
    if (!isHeadless) {
      console.log('提示: 浏览器以可视化模式运行，您可以观察到整个测试过程\n');
    }
  }

  async teardown() {
    if (this.driver) {
      await this.driver.quit();
    }
  }

  async waitAndFindElement(locator, timeout = 5000) {
    return await this.driver.wait(until.elementLocated(locator), timeout);
  }

  async sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
  }

  async navigateTo(path) {
    await this.driver.get(this.baseUrl + path);
    console.log(`  🌐 打开页面: ${this.baseUrl}${path}`);
  }

  async fillInput(locator, value) {
    const element = await this.waitAndFindElement(locator);
    await element.clear();
    await element.sendKeys(value);
    console.log(`  ✏️  输入内容: ${value.substring(0, 20)}${value.length > 20 ? '...' : ''}`);
  }

  async clickButton(locator) {
    const element = await this.waitAndFindElement(locator);
    await element.click();
    console.log(`  🖱️  点击按钮`);
  }

  async getCurrentUrl() {
    return await this.driver.getCurrentUrl();
  }

  async getElementText(locator) {
    const element = await this.waitAndFindElement(locator);
    return await element.getText();
  }

  pass(message) {
    console.log(`  ✅ ${message}`);
    this.testsPassed++;
  }

  fail(message) {
    console.log(`  ❌ ${message}`);
    this.testsFailed++;
  }

  info(message) {
    console.log(`  ℹ️  ${message}`);
  }

  printSummary() {
    console.log('\n=== 测试结果汇总 ===');
    console.log(`✅ 通过: ${this.testsPassed}`);
    console.log(`❌ 失败: ${this.testsFailed}`);
    console.log(`总计: ${this.testsPassed + this.testsFailed}`);
    console.log('');

    if (this.testsFailed === 0) {
      console.log('🎉 所有测试通过！');
    } else {
      console.log('⚠️ 部分测试失败，请检查上述输出。');
    }
  }

  getExitCode() {
    return this.testsFailed > 0 ? 1 : 0;
  }
}

export { By, until };
