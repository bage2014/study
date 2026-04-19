import { SeleniumTestRunner, By, until } from '../selenium-config.js';

async function testMemberEventsPage() {
  const runner = new SeleniumTestRunner('成员大事件页面测试');
  await runner.setup(false);

  try {
    // 先登录
    await runner.navigateTo('/login');
    await runner.waitAndFindElement(By.id('email'));
    await runner.fillInput(By.id('email'), 'bage@qq.com');
    await runner.fillInput(By.id('password'), 'bage1234');
    await runner.clickButton(By.css('button[type="submit"]'));
    await runner.sleep(2000);

    // 导航到成员大事件页面
    await runner.navigateTo('/member-events');
    await runner.sleep(2000);

    const currentUrl = await runner.getCurrentUrl();
    if (currentUrl.includes('/member-events')) {
      runner.pass(`成功打开成员大事件页面: ${currentUrl}`);
    } else {
      runner.fail(`未能打开成员大事件页面，当前URL: ${currentUrl}`);
    }

    // 检查Header
    try {
      const header = await runner.driver.findElement(By.css('header'));
      if (await header.isDisplayed()) {
        runner.pass('Header组件显示正常');
      }
    } catch (e) {
      runner.fail('Header组件未找到');
    }

  } catch (error) {
    runner.fail(`测试过程出错: ${error.message}`);
  } finally {
    await runner.sleep(2000);
    await runner.teardown();
    runner.printSummary();
  }
}

async function testOperationLogsPage() {
  const runner = new SeleniumTestRunner('操作日志页面测试');
  await runner.setup(false);

  try {
    // 先登录
    await runner.navigateTo('/login');
    await runner.waitAndFindElement(By.id('email'));
    await runner.fillInput(By.id('email'), 'bage@qq.com');
    await runner.fillInput(By.id('password'), 'bage1234');
    await runner.clickButton(By.css('button[type="submit"]'));
    await runner.sleep(2000);

    // 导航到操作日志页面
    await runner.navigateTo('/operation-logs');
    await runner.sleep(2000);

    const currentUrl = await runner.getCurrentUrl();
    if (currentUrl.includes('/operation-logs')) {
      runner.pass(`成功打开操作日志页面: ${currentUrl}`);
    } else {
      runner.fail(`未能打开操作日志页面，当前URL: ${currentUrl}`);
    }

    // 检查日志列表
    try {
      const logsContainer = await runner.driver.findElement(By.css('.logs-container, .log-list, [class*="log"]'));
      if (await logsContainer.isDisplayed()) {
        runner.pass('日志列表容器显示正常');
      }
    } catch (e) {
      runner.info('未找到特定的日志容器');
    }

  } catch (error) {
    runner.fail(`测试过程出错: ${error.message}`);
  } finally {
    await runner.sleep(2000);
    await runner.teardown();
    runner.printSummary();
  }
}

async function testFamilyStoryPage() {
  const runner = new SeleniumTestRunner('家庭故事页面测试');
  await runner.setup(false);

  try {
    // 先登录
    await runner.navigateTo('/login');
    await runner.waitAndFindElement(By.id('email'));
    await runner.fillInput(By.id('email'), 'bage@qq.com');
    await runner.fillInput(By.id('password'), 'bage1234');
    await runner.clickButton(By.css('button[type="submit"]'));
    await runner.sleep(2000);

    // 导航到家庭故事页面
    await runner.navigateTo('/family-story');
    await runner.sleep(2000);

    const currentUrl = await runner.getCurrentUrl();
    if (currentUrl.includes('/family-story')) {
      runner.pass(`成功打开家庭故事页面: ${currentUrl}`);
    } else {
      runner.fail(`未能打开家庭故事页面，当前URL: ${currentUrl}`);
    }

    // 检查故事生成按钮
    try {
      const generateButton = await runner.driver.findElement(By.xpath("//button[contains(text(),'生成故事') or contains(text(),'创建故事')]"));
      if (await generateButton.isDisplayed()) {
        runner.pass('生成故事按钮存在');
      }
    } catch (e) {
      runner.info('未找到生成故事按钮');
    }

  } catch (error) {
    runner.fail(`测试过程出错: ${error.message}`);
  } finally {
    await runner.sleep(2000);
    await runner.teardown();
    runner.printSummary();
  }
}

async function testFamilyLocationPage() {
  const runner = new SeleniumTestRunner('家族地理位置页面测试');
  await runner.setup(false);

  try {
    // 先登录
    await runner.navigateTo('/login');
    await runner.waitAndFindElement(By.id('email'));
    await runner.fillInput(By.id('email'), 'bage@qq.com');
    await runner.fillInput(By.id('password'), 'bage1234');
    await runner.clickButton(By.css('button[type="submit"]'));
    await runner.sleep(2000);

    // 导航到家族地理位置页面
    await runner.navigateTo('/family-location');
    await runner.sleep(2000);

    const currentUrl = await runner.getCurrentUrl();
    if (currentUrl.includes('/family-location')) {
      runner.pass(`成功打开家族地理位置页面: ${currentUrl}`);
    } else {
      runner.fail(`未能打开家族地理位置页面，当前URL: ${currentUrl}`);
    }

    // 检查地图容器
    try {
      const mapContainer = await runner.driver.findElement(By.css('.map-container, .location-map, [class*="map"]'));
      if (await mapContainer.isDisplayed()) {
        runner.pass('地图容器显示正常');
      }
    } catch (e) {
      runner.info('未找到地图容器');
    }

  } catch (error) {
    runner.fail(`测试过程出错: ${error.message}`);
  } finally {
    await runner.sleep(2000);
    await runner.teardown();
    runner.printSummary();
  }
}

async function testAIRelationPage() {
  const runner = new SeleniumTestRunner('AI关系分析页面测试');
  await runner.setup(false);

  try {
    // 先登录
    await runner.navigateTo('/login');
    await runner.waitAndFindElement(By.id('email'));
    await runner.fillInput(By.id('email'), 'bage@qq.com');
    await runner.fillInput(By.id('password'), 'bage1234');
    await runner.clickButton(By.css('button[type="submit"]'));
    await runner.sleep(2000);

    // 导航到AI关系分析页面
    await runner.navigateTo('/ai-relation');
    await runner.sleep(2000);

    const currentUrl = await runner.getCurrentUrl();
    if (currentUrl.includes('/ai-relation')) {
      runner.pass(`成功打开AI关系分析页面: ${currentUrl}`);
    } else {
      runner.fail(`未能打开AI关系分析页面，当前URL: ${currentUrl}`);
    }

    // 检查分析按钮
    try {
      const analyzeButton = await runner.driver.findElement(By.xpath("//button[contains(text(),'分析') or contains(text(),'AI分析')]"));
      if (await analyzeButton.isDisplayed()) {
        runner.pass('分析按钮存在');
      }
    } catch (e) {
      runner.info('未找到分析按钮');
    }

  } catch (error) {
    runner.fail(`测试过程出错: ${error.message}`);
  } finally {
    await runner.sleep(2000);
    await runner.teardown();
    runner.printSummary();
  }
}

// 导出所有测试函数
export {
  testMemberEventsPage,
  testOperationLogsPage,
  testFamilyStoryPage,
  testFamilyLocationPage,
  testAIRelationPage
};

// 如果直接运行此文件，则执行所有测试
const isMainModule = process.argv[1]?.includes('selenium-pages-test.js');
if (isMainModule) {
  console.log('\n=== 开始运行页面测试 ===\n');

  const tests = [
    testMemberEventsPage,
    testOperationLogsPage,
    testFamilyStoryPage,
    testFamilyLocationPage,
    testAIRelationPage
  ];

  for (const test of tests) {
    await test();
    await new Promise(resolve => setTimeout(resolve, 1000));
  }

  console.log('\n=== 所有页面测试完成 ===');
  process.exit(0);
}
