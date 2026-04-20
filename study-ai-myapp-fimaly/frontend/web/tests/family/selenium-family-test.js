import { SeleniumTestRunner, By, until } from '../selenium-config.js';

async function testFamilyManagementPage() {
  const runner = new SeleniumTestRunner('家族管理页面测试');
  await runner.setup(false);

  try {
    await runner.navigateTo('/login');
    await runner.waitAndFindElement(By.id('email'));
    await runner.fillInput(By.id('email'), 'bage@qq.com');
    await runner.fillInput(By.id('password'), 'bage1234');
    await runner.clickButton(By.css('button[type="submit"]'));
    await runner.sleep(2000);

    await runner.navigateTo('/family-management');
    await runner.sleep(2000);

    const currentUrl = await runner.getCurrentUrl();
    if (currentUrl.includes('/family-management')) {
      runner.pass(`成功打开家族管理页面: ${currentUrl}`);
    } else {
      runner.fail(`未能打开家族管理页面，当前URL: ${currentUrl}`);
    }

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
    await runner.navigateTo('/login');
    await runner.waitAndFindElement(By.id('email'));
    await runner.fillInput(By.id('email'), 'bage@qq.com');
    await runner.fillInput(By.id('password'), 'bage1234');
    await runner.clickButton(By.css('button[type="submit"]'));
    await runner.sleep(2000);

    await runner.navigateTo('/family-management');
    await runner.sleep(2000);

    try {
      const familyList = await runner.driver.findElement(By.css('.bg-white.p-6.rounded-lg'));
      if (await familyList.isDisplayed()) {
        runner.pass('家族列表容器显示正常');
      }
    } catch (e) {
      runner.info('未找到特定的家族列表容器');
    }

    try {
      const familyItems = await runner.driver.findElements(By.xpath("//div[contains(@class,'cursor-pointer')]"));
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
    await runner.navigateTo('/login');
    await runner.waitAndFindElement(By.id('email'));
    await runner.fillInput(By.id('email'), 'bage@qq.com');
    await runner.fillInput(By.id('password'), 'bage1234');
    await runner.clickButton(By.css('button[type="submit"]'));
    await runner.sleep(2000);

    await runner.navigateTo('/family-management-tree');
    await runner.sleep(2000);

    const currentUrl = await runner.getCurrentUrl();
    if (currentUrl.includes('/family-management-tree')) {
      runner.pass(`成功打开家族树页面: ${currentUrl}`);
    } else {
      runner.fail(`未能打开家族树页面，当前URL: ${currentUrl}`);
    }

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
    await runner.navigateTo('/login');
    await runner.waitAndFindElement(By.id('email'));
    await runner.fillInput(By.id('email'), 'bage@qq.com');
    await runner.fillInput(By.id('password'), 'bage1234');
    await runner.clickButton(By.css('button[type="submit"]'));
    await runner.sleep(2000);

    await runner.navigateTo('/family-management');
    await runner.sleep(2000);

    try {
      const createButton = await runner.driver.findElement(By.xpath("//button[contains(text(),'新建家族')]"));
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

async function selectFirstFamily(runner) {
  try {
    const familyCards = await runner.driver.findElements(By.xpath(
      "//div[contains(@class,'border') and contains(@class,'rounded-md') and contains(@class,'p-4') and contains(@class,'cursor-pointer')]"
    ));
    if (familyCards.length > 0) {
      await familyCards[0].click();
      await runner.sleep(1500);
      return true;
    }
  } catch (e) {
  }

  try {
    const familyDivs = await runner.driver.findElements(By.css('main div.cursor-pointer'));
    if (familyDivs.length > 0) {
      await familyDivs[0].click();
      await runner.sleep(1500);
      return true;
    }
  } catch (e) {
  }

  return false;
}

async function testViewAdministrator() {
  const runner = new SeleniumTestRunner('查看管理员测试');
  await runner.setup(false);

  try {
    await runner.navigateTo('/login');
    await runner.waitAndFindElement(By.id('email'));
    await runner.fillInput(By.id('email'), 'bage@qq.com');
    await runner.fillInput(By.id('password'), 'bage1234');
    await runner.clickButton(By.css('button[type="submit"]'));
    await runner.sleep(2000);

    await runner.navigateTo('/family-management');
    await runner.sleep(3000);

    const hasFamily = await selectFirstFamily(runner);
    if (!hasFamily) {
      runner.info('未找到家族或无法选中家族');
      await runner.sleep(2000);
      await runner.teardown();
      runner.printSummary();
      return;
    }

    await runner.sleep(2000);

    try {
      const adminLabel = await runner.driver.findElement(By.xpath("//p[contains(text(),'管理员')]"));
      if (await adminLabel.isDisplayed()) {
        runner.pass('管理员信息显示正常');
        const adminText = await adminLabel.getText();
        runner.info(`管理员信息: ${adminText}`);
      }
    } catch (e) {
      runner.info('未找到管理员标签');
    }

  } catch (error) {
    runner.fail(`测试过程出错: ${error.message}`);
  } finally {
    await runner.sleep(2000);
    await runner.teardown();
    runner.printSummary();
  }
}

async function testUpdateAdministratorButton() {
  const runner = new SeleniumTestRunner('更改管理员按钮测试');
  await runner.setup(false);

  try {
    await runner.navigateTo('/login');
    await runner.waitAndFindElement(By.id('email'));
    await runner.fillInput(By.id('email'), 'bage@qq.com');
    await runner.fillInput(By.id('password'), 'bage1234');
    await runner.clickButton(By.css('button[type="submit"]'));
    await runner.sleep(2000);

    await runner.navigateTo('/family-management');
    await runner.sleep(3000);

    const hasFamily = await selectFirstFamily(runner);
    if (!hasFamily) {
      runner.info('未找到家族或无法选中家族');
      await runner.sleep(2000);
      await runner.teardown();
      runner.printSummary();
      return;
    }

    await runner.sleep(2000);

    try {
      const updateAdminButton = await runner.driver.findElement(By.xpath("//button[contains(text(),'更改管理员')]"));
      if (await updateAdminButton.isDisplayed()) {
        runner.pass('更改管理员按钮存在并显示');
      }
    } catch (e) {
      runner.fail('更改管理员按钮未找到');
    }

  } catch (error) {
    runner.fail(`测试过程出错: ${error.message}`);
  } finally {
    await runner.sleep(2000);
    await runner.teardown();
    runner.printSummary();
  }
}

async function testUpdateAdministratorModal() {
  const runner = new SeleniumTestRunner('更改管理员弹窗测试');
  await runner.setup(false);

  try {
    await runner.navigateTo('/login');
    await runner.waitAndFindElement(By.id('email'));
    await runner.fillInput(By.id('email'), 'bage@qq.com');
    await runner.fillInput(By.id('password'), 'bage1234');
    await runner.clickButton(By.css('button[type="submit"]'));
    await runner.sleep(2000);

    await runner.navigateTo('/family-management');
    await runner.sleep(3000);

    const hasFamily = await selectFirstFamily(runner);
    if (!hasFamily) {
      runner.info('未找到家族或无法选中家族');
      await runner.sleep(2000);
      await runner.teardown();
      runner.printSummary();
      return;
    }

    await runner.sleep(2000);

    try {
      const updateAdminButton = await runner.driver.findElement(By.xpath("//button[contains(text(),'更改管理员')]"));
      await updateAdminButton.click();
      await runner.sleep(1000);

      const modal = await runner.driver.findElement(By.xpath("//h3[contains(text(),'更改管理员')]"));
      if (await modal.isDisplayed()) {
        runner.pass('更改管理员弹窗打开成功');
      }

      try {
        const selectElement = await runner.driver.findElement(By.id('newAdministrator'));
        if (await selectElement.isDisplayed()) {
          runner.pass('新管理员选择框显示正常');
        }
      } catch (e) {
        runner.info('新管理员选择框未找到');
      }

    } catch (e) {
      runner.fail('更改管理员弹窗未正确打开');
    }

  } catch (error) {
    runner.fail(`测试过程出错: ${error.message}`);
  } finally {
    await runner.sleep(2000);
    await runner.teardown();
    runner.printSummary();
  }
}

async function testAdministratorPermission() {
  const runner = new SeleniumTestRunner('管理员权限验证测试');
  await runner.setup(false);

  try {
    await runner.navigateTo('/login');
    await runner.waitAndFindElement(By.id('email'));
    await runner.fillInput(By.id('email'), 'bage@qq.com');
    await runner.fillInput(By.id('password'), 'bage1234');
    await runner.clickButton(By.css('button[type="submit"]'));
    await runner.sleep(2000);

    await runner.navigateTo('/family-management');
    await runner.sleep(3000);

    const hasFamily = await selectFirstFamily(runner);
    if (!hasFamily) {
      runner.info('未找到家族或无法选中家族');
      await runner.sleep(2000);
      await runner.teardown();
      runner.printSummary();
      return;
    }

    await runner.sleep(2000);

    try {
      const changeAdminButton = await runner.driver.findElement(By.xpath("//button[contains(text(),'更改管理员')]"));
      if (await changeAdminButton.isDisplayed()) {
        runner.pass('管理员可以看到更改管理员按钮');
      }
    } catch (e) {
      runner.fail('管理员未看到更改管理员按钮');
    }

  } catch (error) {
    runner.fail(`测试过程出错: ${error.message}`);
  } finally {
    await runner.sleep(2000);
    await runner.teardown();
    runner.printSummary();
  }
}

export {
  testFamilyManagementPage,
  testFamilyListDisplay,
  testFamilyTreePage,
  testCreateFamilyButton,
  testViewAdministrator,
  testUpdateAdministratorButton,
  testUpdateAdministratorModal,
  testAdministratorPermission
};

const isMainModule = process.argv[1]?.includes('selenium-family-test.js');
if (isMainModule) {
  console.log('\n=== 开始运行家族管理测试 ===\n');

  const tests = [
    testFamilyManagementPage,
    testFamilyListDisplay,
    testFamilyTreePage,
    testCreateFamilyButton,
    testViewAdministrator,
    testUpdateAdministratorButton,
    testUpdateAdministratorModal,
    testAdministratorPermission
  ];

  for (const test of tests) {
    await test();
    await new Promise(resolve => setTimeout(resolve, 1000));
  }

  console.log('\n=== 所有家族管理测试完成 ===');
  process.exit(0);
}