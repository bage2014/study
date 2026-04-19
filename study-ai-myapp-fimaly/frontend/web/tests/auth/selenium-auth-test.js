import { SeleniumTestRunner, By, until } from '../selenium-config.js';

async function testLoginPage() {
  const runner = new SeleniumTestRunner('登录页面测试');
  await runner.setup(false); // UI模式

  try {
    await runner.navigateTo('/login');
    await runner.waitAndFindElement(By.css('button[type="submit"]'));

    const loginButton = await runner.driver.findElement(By.css('button[type="submit"]'));
    const buttonText = await loginButton.getText();

    if (buttonText.includes('登录')) {
      runner.pass('登录页面加载成功，登录按钮存在');
    } else {
      runner.fail(`登录按钮文本不正确: ${buttonText}`);
    }

    const emailInput = await runner.driver.findElement(By.id('email'));
    const passwordInput = await runner.driver.findElement(By.id('password'));

    if (await emailInput.isDisplayed() && await passwordInput.isDisplayed()) {
      runner.pass('用户名和密码输入框显示正常');
    } else {
      runner.fail('用户名或密码输入框未显示');
    }

  } catch (error) {
    runner.fail(`测试过程出错: ${error.message}`);
  } finally {
    await runner.sleep(2000);
    await runner.teardown();
    runner.printSummary();
  }
}

async function testSuccessfulLogin() {
  const runner = new SeleniumTestRunner('成功登录测试');
  await runner.setup(false);

  try {
    await runner.navigateTo('/login');
    await runner.waitAndFindElement(By.id('email'));

    await runner.fillInput(By.id('email'), 'bage@qq.com');
    await runner.fillInput(By.id('password'), 'bage1234');
    await runner.clickButton(By.css('button[type="submit"]'));

    await runner.sleep(3000);

    const currentUrl = await runner.getCurrentUrl();
    if (currentUrl.includes('/home')) {
      runner.pass(`登录成功，跳转到首页: ${currentUrl}`);
    } else {
      runner.fail(`登录失败，当前URL: ${currentUrl}`);
    }

    try {
      await runner.waitAndFindElement(By.css('header'), 3000);
      const headerText = await runner.getElementText(By.css('header'));
      if (headerText.includes('bage@qq.com')) {
        runner.pass('用户信息在Header中显示正确');
      } else {
        runner.fail(`Header内容不包含用户邮箱: ${headerText.substring(0, 50)}`);
      }
    } catch (e) {
      runner.fail('未找到Header元素');
    }

  } catch (error) {
    runner.fail(`测试过程出错: ${error.message}`);
  } finally {
    await runner.sleep(2000);
    await runner.teardown();
    runner.printSummary();
  }
}

async function testFailedLogin() {
  const runner = new SeleniumTestRunner('登录失败测试');
  await runner.setup(false);

  try {
    await runner.navigateTo('/login');
    await runner.waitAndFindElement(By.id('email'));

    await runner.fillInput(By.id('email'), 'invalid@example.com');
    await runner.fillInput(By.id('password'), 'wrongpassword');
    await runner.clickButton(By.css('button[type="submit"]'));

    await runner.sleep(2000);

    const currentUrl = await runner.getCurrentUrl();
    if (currentUrl.includes('/login')) {
      runner.pass('登录失败时停留在登录页面');
    } else {
      runner.fail(`登录失败后跳转到了: ${currentUrl}`);
    }

  } catch (error) {
    runner.fail(`测试过程出错: ${error.message}`);
  } finally {
    await runner.sleep(2000);
    await runner.teardown();
    runner.printSummary();
  }
}

async function testLogout() {
  const runner = new SeleniumTestRunner('退出登录测试');
  await runner.setup(false);

  try {
    // 先登录
    await runner.navigateTo('/login');
    await runner.waitAndFindElement(By.id('email'));
    await runner.fillInput(By.id('email'), 'bage@qq.com');
    await runner.fillInput(By.id('password'), 'bage1234');
    await runner.clickButton(By.css('button[type="submit"]'));
    await runner.sleep(2000);

    // 查找并点击退出按钮
    try {
      const logoutButtons = await runner.driver.findElements(By.xpath("//button[contains(text(),'退出')]"));
      if (logoutButtons.length > 0) {
        await logoutButtons[0].click();
        await runner.sleep(1000);
        const urlAfterLogout = await runner.getCurrentUrl();
        if (urlAfterLogout.includes('/login')) {
          runner.pass(`退出登录成功，跳转到: ${urlAfterLogout}`);
        } else {
          runner.fail(`退出登录后未跳转到登录页面，当前URL: ${urlAfterLogout}`);
        }
      } else {
        runner.info('未找到退出按钮，尝试通过其他方式退出');
      }
    } catch (e) {
      runner.fail(`退出按钮操作失败: ${e.message}`);
    }

  } catch (error) {
    runner.fail(`测试过程出错: ${error.message}`);
  } finally {
    await runner.sleep(2000);
    await runner.teardown();
    runner.printSummary();
  }
}

async function testRegisterPage() {
  const runner = new SeleniumTestRunner('注册页面测试');
  await runner.setup(false);

  try {
    await runner.navigateTo('/register');
    await runner.waitAndFindElement(By.id('email'));

    const emailInput = await runner.driver.findElement(By.id('email'));
    const passwordInput = await runner.driver.findElement(By.id('password'));
    const confirmPasswordInput = await runner.driver.findElement(By.id('confirmPassword'));
    const nicknameInput = await runner.driver.findElement(By.id('nickname'));

    if (await emailInput.isDisplayed() && await passwordInput.isDisplayed() &&
        await confirmPasswordInput.isDisplayed() && await nicknameInput.isDisplayed()) {
      runner.pass('注册表单所有字段显示正常');
    } else {
      runner.fail('注册表单某些字段未显示');
    }

    const registerButton = await runner.driver.findElement(By.css('button[type="submit"]'));
    const buttonText = await registerButton.getText();
    if (buttonText.includes('注册')) {
      runner.pass('注册按钮存在');
    } else {
      runner.fail(`注册按钮文本不正确: ${buttonText}`);
    }

  } catch (error) {
    runner.fail(`测试过程出错: ${error.message}`);
  } finally {
    await runner.sleep(2000);
    await runner.teardown();
    runner.printSummary();
  }
}

async function testSuccessfulRegister() {
  const runner = new SeleniumTestRunner('成功注册测试');
  await runner.setup(false);

  try {
    await runner.navigateTo('/register');
    await runner.waitAndFindElement(By.id('email'));

    const uniqueEmail = `test${Date.now()}@example.com`;

    await runner.fillInput(By.id('email'), uniqueEmail);
    await runner.fillInput(By.id('password'), 'password123');
    await runner.fillInput(By.id('confirmPassword'), 'password123');
    await runner.fillInput(By.id('nickname'), '测试用户');
    await runner.clickButton(By.css('button[type="submit"]'));

    await runner.sleep(3000);

    const currentUrl = await runner.getCurrentUrl();
    if (currentUrl.includes('/home')) {
      runner.pass(`注册成功并自动登录，跳转到: ${currentUrl}`);
    } else {
      runner.fail(`注册后URL: ${currentUrl}`);
    }

  } catch (error) {
    runner.fail(`测试过程出错: ${error.message}`);
  } finally {
    await runner.sleep(2000);
    await runner.teardown();
    runner.printSummary();
  }
}

async function testPasswordMismatch() {
  const runner = new SeleniumTestRunner('密码不匹配测试');
  await runner.setup(false);

  try {
    await runner.navigateTo('/register');
    await runner.waitAndFindElement(By.id('email'));

    await runner.fillInput(By.id('email'), 'test@example.com');
    await runner.fillInput(By.id('password'), 'password123');
    await runner.fillInput(By.id('confirmPassword'), 'differentpassword');
    await runner.fillInput(By.id('nickname'), '测试用户');

    const submitButton = await runner.driver.findElement(By.css('button[type="submit"]'));
    const isDisabled = await submitButton.isDisabled();

    if (isDisabled) {
      runner.pass('密码不匹配时提交按钮被禁用');
    } else {
      runner.fail('密码不匹配时提交按钮未被禁用');
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
  testLoginPage,
  testSuccessfulLogin,
  testFailedLogin,
  testLogout,
  testRegisterPage,
  testSuccessfulRegister,
  testPasswordMismatch
};

// 如果直接运行此文件，则执行所有测试
const isMainModule = process.argv[1]?.includes('selenium-auth-test.js');
if (isMainModule) {
  console.log('\n=== 开始运行认证测试 ===\n');

  const tests = [
    testLoginPage,
    testSuccessfulLogin,
    testFailedLogin,
    testLogout,
    testRegisterPage,
    testSuccessfulRegister,
    testPasswordMismatch
  ];

  for (const test of tests) {
    await test();
    await new Promise(resolve => setTimeout(resolve, 1000));
  }

  console.log('\n=== 所有认证测试完成 ===');
  process.exit(0);
}
