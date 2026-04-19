import { SeleniumTestRunner, By, until } from '../selenium-config.js';

async function testFamilyManagementPage() {
  const runner = new SeleniumTestRunner('家族管理页面测试');
  await runner.setup(false);

  try {
    // 先登录
    await runner.navigateTo('/login');
    await runner.waitAndFindElement(By.id('email'));
    await runner.fillInput(By.id('email'), 'bage@qq.com');
    await runner.fillInput(By.id('password'), 'bage1234');
    await runner.clickButton(By.css('button[type="submit"]'));
    await runner.sleep(2000);

    // 导航到家族管理页面
    await runner.navigateTo('/family');
    await runner.sleep(2000);

    const currentUrl = await runner.getCurrentUrl();
    if (currentUrl.includes('/family')) {
      runner.pass(`成功打开家族管理页面: ${currentUrl}`);
    } else {
      runner.fail(`未能打开家族管理页面，当前URL: ${currentUrl}`);
    }

    // 检查页面元素
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

async function testFamilyListDisplay() {
  const runner = new SeleniumTestRunner('家族列表显示测试');
  await runner.setup(false);

  try {
    // 先登录
    await runner.navigateTo('/login');
    await runner.waitAndFindElement(By.id('email'));
    await runner.fillInput(By.id('email'), 'bage@qq.com');
    await runner.fillInput(By.id('password'), 'bage1234');
    await runner.clickButton(By.css('button[type="submit"]'));
    await runner.sleep(2000);

    // 导航到家族管理页面
    await runner.navigateTo('/family');
    await runner.sleep(2000);

    // 检查是否有家族列表容器
    try {
      const familyList = await runner.driver.findElement(By.css('.family-list, .family-grid, [class*="family"]'));
      if (await familyList.isDisplayed()) {
        runner.pass('家族列表容器显示正常');
      }
    } catch (e) {
      runner.info('未找到特定的家族列表容器');
    }

    // 检查家族卡片或列表项
    try {
      const familyItems = await runner.driver.findElements(By.css('.family-card, .family-item, [class*="family-item"]'));
      if (familyItems.length > 0) {
        runner.pass(`找到 ${familyItems.length} 个家族项`);
      } else {
        runner.info('未找到家族项，可能需要创建家族');
      }
    } catch (e) {
      runner.info('未找到家族项元素');
    }

  } catch (error) {
    runner.fail(`测试过程出错: ${error.message}`);
  } finally {
    await runner.sleep(2000);
    await runner.teardown();
    runner.printSummary();
  }
}

async function testFamilyTreePage() {
  const runner = new SeleniumTestRunner('家族树页面测试');
  await runner.setup(false);

  try {
    // 先登录
    await runner.navigateTo('/login');
    await runner.waitAndFindElement(By.id('email'));
    await runner.fillInput(By.id('email'), 'bage@qq.com');
    await runner.fillInput(By.id('password'), 'bage1234');
    await runner.clickButton(By.css('button[type="submit"]'));
    await runner.sleep(2000);

    // 导航到家族树页面
    await runner.navigateTo('/family-tree');
    await runner.sleep(2000);

    const currentUrl = await runner.getCurrentUrl();
    if (currentUrl.includes('/family-tree')) {
      runner.pass(`成功打开家族树页面: ${currentUrl}`);
    } else {
      runner.fail(`未能打开家族树页面，当前URL: ${currentUrl}`);
    }

    // 检查是否有家族树容器
    try {
      const treeContainer = await runner.driver.findElement(By.css('.tree-container, .family-tree, [class*="tree"]'));
      if (await treeContainer.isDisplayed()) {
        runner.pass('家族树容器显示正常');
      }
    } catch (e) {
      runner.info('未找到特定的家族树容器');
    }

  } catch (error) {
    runner.fail(`测试过程出错: ${error.message}`);
  } finally {
    await runner.sleep(2000);
    await runner.teardown();
    runner.printSummary();
  }
}

async function testCreateFamilyButton() {
  const runner = new SeleniumTestRunner('创建家族按钮测试');
  await runner.setup(false);

  try {
    // 先登录
    await runner.navigateTo('/login');
    await runner.waitAndFindElement(By.id('email'));
    await runner.fillInput(By.id('email'), 'bage@qq.com');
    await runner.fillInput(By.id('password'), 'bage1234');
    await runner.clickButton(By.css('button[type="submit"]'));
    await runner.sleep(2000);

    // 导航到家族管理页面
    await runner.navigateTo('/family');
    await runner.sleep(2000);

    // 查找创建家族按钮
    try {
      const createButton = await runner.driver.findElement(By.xpath("//button[contains(text(),'创建家族') or contains(text(),'新建家族')]"));
      if (await createButton.isDisplayed()) {
        runner.pass('创建家族按钮存在并显示');
      }
    } catch (e) {
      runner.info('未找到创建家族按钮');
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
  testFamilyManagementPage,
  testFamilyListDisplay,
  testFamilyTreePage,
  testCreateFamilyButton
};

// 如果直接运行此文件，则执行所有测试
const isMainModule = process.argv[1]?.includes('selenium-family-test.js');
if (isMainModule) {
  console.log('\n=== 开始运行家族管理测试 ===\n');

  const tests = [
    testFamilyManagementPage,
    testFamilyListDisplay,
    testFamilyTreePage,
    testCreateFamilyButton
  ];

  for (const test of tests) {
    await test();
    await new Promise(resolve => setTimeout(resolve, 1000));
  }

  console.log('\n=== 所有家族管理测试完成 ===');
  process.exit(0);
}
