import { SeleniumTestRunner, By, until } from './selenium-config.js';

async function runAdministratorTest() {
  const runner = new SeleniumTestRunner('更改管理员功能测试');
  
  try {
    // 初始化浏览器（使用headless模式）
    await runner.setup(true);

    // ===== 测试步骤1: 登录系统 =====
    runner.info('测试步骤1: 登录系统');
    await runner.navigateTo('/login');
    await runner.fillInput(By.css('input[type="email"]'), 'bage@qq.com');
    await runner.fillInput(By.css('input[type="password"]'), 'bage1234');
    await runner.clickButton(By.css('button[type="submit"]'));
    await runner.sleep(2000);
    
    // 验证登录成功（跳转到首页）
    const currentUrl = await runner.getCurrentUrl();
    if (currentUrl.includes('/home')) {
      runner.pass('登录成功，已跳转到首页');
    } else {
      runner.fail(`登录失败，当前URL: ${currentUrl}`);
      await runner.teardown();
      process.exit(1);
    }

    // ===== 测试步骤2: 进入家族管理页面 =====
    runner.info('测试步骤2: 进入家族管理页面');
    await runner.clickButton(By.linkText('家族管理'));
    await runner.sleep(1500);
    
    const familyUrl = await runner.getCurrentUrl();
    if (familyUrl.includes('/family-management')) {
      runner.pass('成功进入家族管理页面');
    } else {
      runner.fail(`进入家族管理页面失败，当前URL: ${familyUrl}`);
      await runner.teardown();
      process.exit(1);
    }

    // ===== 测试步骤3: 选择家族 =====
    runner.info('测试步骤3: 选择家族');
    try {
      // 等待家族列表加载
      const familyItem = await runner.driver.wait(
        until.elementLocated(By.css('.family-list-item:first-child')),
        5000
      );
      await familyItem.click();
      await runner.sleep(1000);
      runner.pass('成功选择家族');
    } catch (error) {
      runner.fail(`选择家族失败: ${error.message}`);
      await runner.teardown();
      process.exit(1);
    }

    // ===== 测试步骤4: 打开更改管理员弹窗 =====
    runner.info('测试步骤4: 打开更改管理员弹窗');
    try {
      const updateAdminBtn = await runner.driver.wait(
        until.elementLocated(By.xpath('//button[contains(text(), "更改管理员")]')),
        5000
      );
      await updateAdminBtn.click();
      await runner.sleep(1000);
      runner.pass('成功打开更改管理员弹窗');
    } catch (error) {
      runner.fail(`打开更改管理员弹窗失败: ${error.message}`);
      await runner.teardown();
      process.exit(1);
    }

    // ===== 测试步骤5: 选择新管理员 =====
    runner.info('测试步骤5: 选择新管理员');
    try {
      // 点击下拉选择器
      const selectElement = await runner.driver.wait(
        until.elementLocated(By.css('.el-select')),
        5000
      );
      await selectElement.click();
      await runner.sleep(500);
      
      // 选择第一个成员选项（跳过默认选项）
      const options = await runner.driver.wait(
        until.elementsLocated(By.css('.el-select-dropdown__item')),
        5000
      );
      
      if (options.length > 0) {
        // 选择第二个选项（第一个可能是"请选择"）
        const targetOption = options.length > 1 ? options[1] : options[0];
        await targetOption.click();
        await runner.sleep(500);
        runner.pass('成功选择新管理员');
      } else {
        runner.fail('下拉列表中没有成员选项');
        await runner.teardown();
        process.exit(1);
      }
    } catch (error) {
      runner.fail(`选择新管理员失败: ${error.message}`);
      await runner.teardown();
      process.exit(1);
    }

    // ===== 测试步骤6: 提交更改 =====
    runner.info('测试步骤6: 提交更改');
    try {
      const confirmBtn = await runner.driver.wait(
        until.elementLocated(By.xpath('//button[contains(text(), "确定")]')),
        5000
      );
      await confirmBtn.click();
      await runner.sleep(2000);
      
      // 检查成功提示
      const successToast = await runner.driver.wait(
        until.elementLocated(By.css('.el-toast--success')),
        5000
      );
      const toastText = await successToast.getText();
      
      if (toastText.includes('管理员更新成功')) {
        runner.pass('更改管理员成功，显示成功提示');
      } else {
        runner.fail(`更改管理员失败，提示内容: ${toastText}`);
      }
    } catch (error) {
      runner.fail(`提交更改失败: ${error.message}`);
      await runner.teardown();
      process.exit(1);
    }

    // ===== 测试步骤7: 验证管理员已更新 =====
    runner.info('测试步骤7: 验证管理员已更新');
    try {
      await runner.sleep(1000);
      // 检查家族详情中管理员是否已更新
      const adminInfo = await runner.driver.wait(
        until.elementLocated(By.css('.administrator-info')),
        5000
      );
      const adminText = await adminInfo.getText();
      
      if (adminText && adminText !== '无') {
        runner.pass(`管理员信息已更新: ${adminText}`);
      } else {
        runner.fail('管理员信息未更新');
      }
    } catch (error) {
      runner.fail(`验证管理员更新失败: ${error.message}`);
    }

  } catch (error) {
    runner.fail(`测试过程发生未预期错误: ${error.message}`);
  } finally {
    // 清理并输出汇总
    await runner.teardown();
    runner.printSummary();
    process.exit(runner.getExitCode());
  }
}

// 运行测试
runAdministratorTest();
