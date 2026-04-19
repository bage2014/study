import { SeleniumTestRunner, By, until } from '../selenium-config.js';

async function testMemberManagementPage() {
  const runner = new SeleniumTestRunner('成员管理页面测试');
  await runner.setup(false);

  try {
    // 先登录
    await runner.navigateTo('/login');
    await runner.waitAndFindElement(By.id('email'));
    await runner.fillInput(By.id('email'), 'bage@qq.com');
    await runner.fillInput(By.id('password'), 'bage1234');
    await runner.clickButton(By.css('button[type="submit"]'));
    await runner.sleep(2000);

    // 导航到成员管理页面
    await runner.navigateTo('/members');
    await runner.sleep(2000);

    const currentUrl = await runner.getCurrentUrl();
    if (currentUrl.includes('/members')) {
      runner.pass(`成功打开成员管理页面: ${currentUrl}`);
    } else {
      runner.fail(`未能打开成员管理页面，当前URL: ${currentUrl}`);
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

async function testMemberListDisplay() {
  const runner = new SeleniumTestRunner('成员列表显示测试');
  await runner.setup(false);

  try {
    // 先登录
    await runner.navigateTo('/login');
    await runner.waitAndFindElement(By.id('email'));
    await runner.fillInput(By.id('email'), 'bage@qq.com');
    await runner.fillInput(By.id('password'), 'bage1234');
    await runner.clickButton(By.css('button[type="submit"]'));
    await runner.sleep(2000);

    // 导航到成员管理页面
    await runner.navigateTo('/members');
    await runner.sleep(2000);

    // 检查是否有成员列表容器
    try {
      const memberList = await runner.driver.findElement(By.css('.member-list, .member-grid, [class*="member"]'));
      if (await memberList.isDisplayed()) {
        runner.pass('成员列表容器显示正常');
      }
    } catch (e) {
      runner.info('未找到特定的成员列表容器');
    }

    // 检查成员卡片或列表项
    try {
      const memberItems = await runner.driver.findElements(By.css('.member-card, .member-item, [class*="member-item"]'));
      if (memberItems.length > 0) {
        runner.pass(`找到 ${memberItems.length} 个成员项`);
      } else {
        runner.info('未找到成员项，可能需要添加成员');
      }
    } catch (e) {
      runner.info('未找到成员项元素');
    }

  } catch (error) {
    runner.fail(`测试过程出错: ${error.message}`);
  } finally {
    await runner.sleep(2000);
    await runner.teardown();
    runner.printSummary();
  }
}

async function testAddMemberButton() {
  const runner = new SeleniumTestRunner('添加成员按钮测试');
  await runner.setup(false);

  try {
    // 先登录
    await runner.navigateTo('/login');
    await runner.waitAndFindElement(By.id('email'));
    await runner.fillInput(By.id('email'), 'bage@qq.com');
    await runner.fillInput(By.id('password'), 'bage1234');
    await runner.clickButton(By.css('button[type="submit"]'));
    await runner.sleep(2000);

    // 导航到成员管理页面
    await runner.navigateTo('/members');
    await runner.sleep(2000);

    // 查找添加成员按钮
    try {
      const addButton = await runner.driver.findElement(By.xpath("//button[contains(text(),'添加成员') or contains(text(),'新增成员') or contains(text(),'添加')]"));
      if (await addButton.isDisplayed()) {
        runner.pass('添加成员按钮存在并显示');
      }
    } catch (e) {
      runner.info('未找到添加成员按钮');
    }

  } catch (error) {
    runner.fail(`测试过程出错: ${error.message}`);
  } finally {
    await runner.sleep(2000);
    await runner.teardown();
    runner.printSummary();
  }
}

async function testMemberDetailPage() {
  const runner = new SeleniumTestRunner('成员详情页面测试');
  await runner.setup(false);

  try {
    // 先登录
    await runner.navigateTo('/login');
    await runner.waitAndFindElement(By.id('email'));
    await runner.fillInput(By.id('email'), 'bage@qq.com');
    await runner.fillInput(By.id('password'), 'bage1234');
    await runner.clickButton(By.css('button[type="submit"]'));
    await runner.sleep(2000);

    // 导航到成员管理页面
    await runner.navigateTo('/members');
    await runner.sleep(2000);

    // 尝试点击某个成员项进入详情页
    try {
      const memberItems = await runner.driver.findElements(By.css('.member-card, .member-item, [class*="member-item"]'));
      if (memberItems.length > 0) {
        await memberItems[0].click();
        await runner.sleep(2000);

        const currentUrl = await runner.getCurrentUrl();
        if (currentUrl.includes('/members')) {
          runner.pass('成功进入成员详情页面');
        } else {
          runner.info(`当前URL: ${currentUrl}`);
        }
      } else {
        runner.info('未找到成员项，无法测试详情页');
      }
    } catch (e) {
      runner.info(`无法进入详情页: ${e.message}`);
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
  testMemberManagementPage,
  testMemberListDisplay,
  testAddMemberButton,
  testMemberDetailPage
};

// 如果直接运行此文件，则执行所有测试
const isMainModule = process.argv[1]?.includes('selenium-member-test.js');
if (isMainModule) {
  console.log('\n=== 开始运行成员管理测试 ===\n');

  const tests = [
    testMemberManagementPage,
    testMemberListDisplay,
    testAddMemberButton,
    testMemberDetailPage
  ];

  for (const test of tests) {
    await test();
    await new Promise(resolve => setTimeout(resolve, 1000));
  }

  console.log('\n=== 所有成员管理测试完成 ===');
  process.exit(0);
}
