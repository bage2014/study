import { SeleniumTestRunner, By, until } from '../selenium-config.js';

async function login(runner) {
  await runner.navigateTo('/login');
  await runner.waitAndFindElement(By.id('email'));
  await runner.fillInput(By.id('email'), 'bage@qq.com');
  await runner.fillInput(By.id('password'), 'bage1234');
  await runner.clickButton(By.css('button[type="submit"]'));
  await runner.sleep(3000);
}

async function testHomePage() {
  const runner = new SeleniumTestRunner('首页测试');
  await runner.setup(false);

  try {
    await login(runner);

    const currentUrl = await runner.getCurrentUrl();
    if (currentUrl.includes('/home')) {
      runner.pass('成功登录并跳转到首页');
    } else {
      runner.fail(`登录后未跳转到首页: ${currentUrl}`);
    }

    try {
      const welcomeText = await runner.getElementText(By.css('h1, .welcome-title'));
      runner.pass(`首页欢迎标题显示: ${welcomeText.substring(0, 30)}...`);
    } catch (e) {
      runner.info('未找到欢迎标题');
    }

    try {
      const statsCards = await runner.driver.findElements(By.css('.stat-card, .stat-item'));
      if (statsCards.length > 0) {
        runner.pass(`找到 ${statsCards.length} 个统计卡片`);
      }
    } catch (e) {
      runner.info('未找到统计卡片');
    }

    try {
      const featureCards = await runner.driver.findElements(By.css('.feature-card, .function-card'));
      if (featureCards.length > 0) {
        runner.pass(`找到 ${featureCards.length} 个功能卡片`);
      }
    } catch (e) {
      runner.info('未找到功能卡片');
    }

    try {
      const familyCards = await runner.driver.findElements(By.css('.family-card'));
      if (familyCards.length > 0) {
        runner.pass(`找到 ${familyCards.length} 个家族卡片`);
      }
    } catch (e) {
      runner.info('未找到家族卡片');
    }

  } catch (error) {
    runner.fail(`测试过程出错: ${error.message}`);
  } finally {
    await runner.sleep(2000);
    await runner.teardown();
    runner.printSummary();
  }
}

async function testFamilyManagementPage() {
  const runner = new SeleniumTestRunner('家族管理页面测试');
  await runner.setup(false);

  try {
    await login(runner);

    await runner.navigateTo('/family-management');
    await runner.sleep(2000);

    const currentUrl = await runner.getCurrentUrl();
    if (currentUrl.includes('/family-management')) {
      runner.pass('成功打开家族管理页面');
    } else {
      runner.fail(`未能打开家族管理页面: ${currentUrl}`);
    }

    try {
      const createButton = await runner.driver.findElement(By.xpath("//button[contains(text(),'新建家族')]"));
      if (await createButton.isDisplayed()) {
        runner.pass('新建家族按钮存在');
      }
    } catch (e) {
      runner.info('未找到新建家族按钮');
    }

    try {
      const familyList = await runner.driver.findElements(By.css('.family-item, .border.rounded-md.p-4.cursor-pointer'));
      if (familyList.length > 0) {
        runner.pass(`找到 ${familyList.length} 个家族`);
        await familyList[0].click();
        await runner.sleep(1000);
        runner.pass('成功选择家族');
      }
    } catch (e) {
      runner.info('未找到家族列表');
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
    await login(runner);

    await runner.navigateTo('/family-tree');
    await runner.sleep(3000);

    const currentUrl = await runner.getCurrentUrl();
    if (currentUrl.includes('/family-tree')) {
      runner.pass('成功打开家族树页面');
    } else {
      runner.fail(`未能打开家族树页面: ${currentUrl}`);
    }

    try {
      const treeContainer = await runner.driver.findElement(By.css('.tree-container, .family-tree-container'));
      if (await treeContainer.isDisplayed()) {
        runner.pass('家族树容器存在');
      }
    } catch (e) {
      runner.info('未找到家族树容器');
    }

    try {
      const nodes = await runner.driver.findElements(By.css('.node, .member-node'));
      if (nodes.length > 0) {
        runner.pass(`找到 ${nodes.length} 个家族树节点`);
      }
    } catch (e) {
      runner.info('未找到家族树节点');
    }

    try {
      const zoomControls = await runner.driver.findElements(By.css('.zoom-control, .tree-controls button'));
      if (zoomControls.length > 0) {
        runner.pass('找到缩放控制按钮');
      }
    } catch (e) {
      runner.info('未找到缩放控制按钮');
    }

  } catch (error) {
    runner.fail(`测试过程出错: ${error.message}`);
  } finally {
    await runner.sleep(2000);
    await runner.teardown();
    runner.printSummary();
  }
}

async function testMembersPage() {
  const runner = new SeleniumTestRunner('成员管理页面测试');
  await runner.setup(false);

  try {
    await login(runner);

    await runner.navigateTo('/members');
    await runner.sleep(2000);

    const currentUrl = await runner.getCurrentUrl();
    if (currentUrl.includes('/members')) {
      runner.pass('成功打开成员管理页面');
    } else {
      runner.fail(`未能打开成员管理页面: ${currentUrl}`);
    }

    try {
      const addButton = await runner.driver.findElement(By.xpath("//button[contains(text(),'添加成员')]"));
      if (await addButton.isDisplayed()) {
        runner.pass('添加成员按钮存在');
      }
    } catch (e) {
      runner.info('未找到添加成员按钮');
    }

    try {
      const memberItems = await runner.driver.findElements(By.css('.member-item, .member-card'));
      if (memberItems.length > 0) {
        runner.pass(`找到 ${memberItems.length} 个成员`);
      }
    } catch (e) {
      runner.info('未找到成员列表');
    }

  } catch (error) {
    runner.fail(`测试过程出错: ${error.message}`);
  } finally {
    await runner.sleep(2000);
    await runner.teardown();
    runner.printSummary();
  }
}

async function testRelationshipsPage() {
  const runner = new SeleniumTestRunner('关系管理页面测试');
  await runner.setup(false);

  try {
    await login(runner);

    await runner.navigateTo('/relationships');
    await runner.sleep(2000);

    const currentUrl = await runner.getCurrentUrl();
    if (currentUrl.includes('/relationships')) {
      runner.pass('成功打开关系管理页面');
    } else {
      runner.fail(`未能打开关系管理页面: ${currentUrl}`);
    }

    try {
      const addButton = await runner.driver.findElement(By.xpath("//button[contains(text(),'添加关系')]"));
      if (await addButton.isDisplayed()) {
        runner.pass('添加关系按钮存在');
      }
    } catch (e) {
      runner.info('未找到添加关系按钮');
    }

    try {
      const memberSelect = await runner.driver.findElement(By.id('member1') || By.id('memberA'));
      if (await memberSelect.isDisplayed()) {
        runner.pass('成员选择器存在');
      }
    } catch (e) {
      runner.info('未找到成员选择器');
    }

  } catch (error) {
    runner.fail(`测试过程出错: ${error.message}`);
  } finally {
    await runner.sleep(2000);
    await runner.teardown();
    runner.printSummary();
  }
}

async function testEventsPage() {
  const runner = new SeleniumTestRunner('历史记录页面测试');
  await runner.setup(false);

  try {
    await login(runner);

    await runner.navigateTo('/events');
    await runner.sleep(2000);

    const currentUrl = await runner.getCurrentUrl();
    if (currentUrl.includes('/events')) {
      runner.pass('成功打开历史记录页面');
    } else {
      runner.fail(`未能打开历史记录页面: ${currentUrl}`);
    }

    try {
      const timeline = await runner.driver.findElement(By.css('.timeline, .events-timeline'));
      if (await timeline.isDisplayed()) {
        runner.pass('时间轴容器存在');
      }
    } catch (e) {
      runner.info('未找到时间轴容器');
    }

    try {
      const addButton = await runner.driver.findElement(By.xpath("//button[contains(text(),'添加事件')]"));
      if (await addButton.isDisplayed()) {
        runner.pass('添加事件按钮存在');
      }
    } catch (e) {
      runner.info('未找到添加事件按钮');
    }

  } catch (error) {
    runner.fail(`测试过程出错: ${error.message}`);
  } finally {
    await runner.sleep(2000);
    await runner.teardown();
    runner.printSummary();
  }
}

async function testMediaPage() {
  const runner = new SeleniumTestRunner('多媒体库页面测试');
  await runner.setup(false);

  try {
    await login(runner);

    await runner.navigateTo('/media');
    await runner.sleep(2000);

    const currentUrl = await runner.getCurrentUrl();
    if (currentUrl.includes('/media')) {
      runner.pass('成功打开多媒体库页面');
    } else {
      runner.fail(`未能打开多媒体库页面: ${currentUrl}`);
    }

    try {
      const uploadButton = await runner.driver.findElement(By.xpath("//button[contains(text(),'上传')]"));
      if (await uploadButton.isDisplayed()) {
        runner.pass('上传按钮存在');
      }
    } catch (e) {
      runner.info('未找到上传按钮');
    }

    try {
      const dropzone = await runner.driver.findElement(By.css('.dropzone, .upload-area'));
      if (await dropzone.isDisplayed()) {
        runner.pass('拖拽上传区域存在');
      }
    } catch (e) {
      runner.info('未找到拖拽上传区域');
    }

    try {
      const imageGrid = await runner.driver.findElements(By.css('.media-item, .photo-item'));
      if (imageGrid.length > 0) {
        runner.pass(`找到 ${imageGrid.length} 个媒体文件`);
      }
    } catch (e) {
      runner.info('未找到媒体文件');
    }

  } catch (error) {
    runner.fail(`测试过程出错: ${error.message}`);
  } finally {
    await runner.sleep(2000);
    await runner.teardown();
    runner.printSummary();
  }
}

async function testMilestonesPage() {
  const runner = new SeleniumTestRunner('成员大事件页面测试');
  await runner.setup(false);

  try {
    await login(runner);

    await runner.navigateTo('/milestones');
    await runner.sleep(2000);

    const currentUrl = await runner.getCurrentUrl();
    if (currentUrl.includes('/milestones')) {
      runner.pass('成功打开成员大事件页面');
    } else {
      runner.fail(`未能打开成员大事件页面: ${currentUrl}`);
    }

    try {
      const addButton = await runner.driver.findElement(By.xpath("//button[contains(text(),'添加大事件')]"));
      if (await addButton.isDisplayed()) {
        runner.pass('添加大事件按钮存在');
      }
    } catch (e) {
      runner.info('未找到添加大事件按钮');
    }

  } catch (error) {
    runner.fail(`测试过程出错: ${error.message}`);
  } finally {
    await runner.sleep(2000);
    await runner.teardown();
    runner.printSummary();
  }
}

async function testLocationMapPage() {
  const runner = new SeleniumTestRunner('成员位置页面测试');
  await runner.setup(false);

  try {
    await login(runner);

    await runner.navigateTo('/location-map');
    await runner.sleep(3000);

    const currentUrl = await runner.getCurrentUrl();
    if (currentUrl.includes('/location-map')) {
      runner.pass('成功打开成员位置页面');
    } else {
      runner.fail(`未能打开成员位置页面: ${currentUrl}`);
    }

    try {
      const mapContainer = await runner.driver.findElement(By.css('.map-container, #map'));
      if (await mapContainer.isDisplayed()) {
        runner.pass('地图容器存在');
      }
    } catch (e) {
      runner.info('未找到地图容器');
    }

    try {
      const memberList = await runner.driver.findElement(By.css('.member-location-list'));
      if (await memberList.isDisplayed()) {
        runner.pass('成员位置列表存在');
      }
    } catch (e) {
      runner.info('未找到成员位置列表');
    }

  } catch (error) {
    runner.fail(`测试过程出错: ${error.message}`);
  } finally {
    await runner.sleep(2000);
    await runner.teardown();
    runner.printSummary();
  }
}

async function testAiRelationshipAnalysisPage() {
  const runner = new SeleniumTestRunner('AI关系分析页面测试');
  await runner.setup(false);

  try {
    await login(runner);

    await runner.navigateTo('/ai-relationship-analysis');
    await runner.sleep(2000);

    const currentUrl = await runner.getCurrentUrl();
    if (currentUrl.includes('/ai-relationship-analysis')) {
      runner.pass('成功打开AI关系分析页面');
    } else {
      runner.fail(`未能打开AI关系分析页面: ${currentUrl}`);
    }

    try {
      const analyzeButton = await runner.driver.findElement(By.xpath("//button[contains(text(),'分析')]"));
      if (await analyzeButton.isDisplayed()) {
        runner.pass('分析按钮存在');
      }
    } catch (e) {
      runner.info('未找到分析按钮');
    }

    try {
      const familySelect = await runner.driver.findElement(By.css('select[name="family"], #family-select'));
      if (await familySelect.isDisplayed()) {
        runner.pass('家族选择器存在');
      }
    } catch (e) {
      runner.info('未找到家族选择器');
    }

  } catch (error) {
    runner.fail(`测试过程出错: ${error.message}`);
  } finally {
    await runner.sleep(2000);
    await runner.teardown();
    runner.printSummary();
  }
}

async function testFamilyStoriesPage() {
  const runner = new SeleniumTestRunner('家族故事页面测试');
  await runner.setup(false);

  try {
    await login(runner);

    await runner.navigateTo('/family-stories');
    await runner.sleep(2000);

    const currentUrl = await runner.getCurrentUrl();
    if (currentUrl.includes('/family-stories')) {
      runner.pass('成功打开家族故事页面');
    } else {
      runner.fail(`未能打开家族故事页面: ${currentUrl}`);
    }

    try {
      const generateButton = await runner.driver.findElement(By.xpath("//button[contains(text(),'生成')]"));
      if (await generateButton.isDisplayed()) {
        runner.pass('生成按钮存在');
      }
    } catch (e) {
      runner.info('未找到生成按钮');
    }

  } catch (error) {
    runner.fail(`测试过程出错: ${error.message}`);
  } finally {
    await runner.sleep(2000);
    await runner.teardown();
    runner.printSummary();
  }
}

async function testImageImportPage() {
  const runner = new SeleniumTestRunner('图片导入页面测试');
  await runner.setup(false);

  try {
    await login(runner);

    await runner.navigateTo('/image-import-analysis');
    await runner.sleep(2000);

    const currentUrl = await runner.getCurrentUrl();
    if (currentUrl.includes('/image-import-analysis')) {
      runner.pass('成功打开图片导入页面');
    } else {
      runner.fail(`未能打开图片导入页面: ${currentUrl}`);
    }

    try {
      const uploadArea = await runner.driver.findElement(By.css('.upload-area, .dropzone-area'));
      if (await uploadArea.isDisplayed()) {
        runner.pass('图片上传区域存在');
      }
    } catch (e) {
      runner.info('未找到图片上传区域');
    }

    try {
      const parseButton = await runner.driver.findElement(By.xpath("//button[contains(text(),'解析')]"));
      if (await parseButton.isDisplayed()) {
        runner.pass('解析按钮存在');
      }
    } catch (e) {
      runner.info('未找到解析按钮');
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
    await login(runner);

    await runner.navigateTo('/operation-logs');
    await runner.sleep(2000);

    const currentUrl = await runner.getCurrentUrl();
    if (currentUrl.includes('/operation-logs')) {
      runner.pass('成功打开操作日志页面');
    } else {
      runner.fail(`未能打开操作日志页面: ${currentUrl}`);
    }

    try {
      const table = await runner.driver.findElement(By.css('table, .log-table'));
      if (await table.isDisplayed()) {
        runner.pass('日志表格存在');
      }
    } catch (e) {
      runner.info('未找到日志表格');
    }

  } catch (error) {
    runner.fail(`测试过程出错: ${error.message}`);
  } finally {
    await runner.sleep(2000);
    await runner.teardown();
    runner.printSummary();
  }
}

async function testProfilePage() {
  const runner = new SeleniumTestRunner('个人信息页面测试');
  await runner.setup(false);

  try {
    await login(runner);

    await runner.navigateTo('/profile');
    await runner.sleep(2000);

    const currentUrl = await runner.getCurrentUrl();
    if (currentUrl.includes('/profile')) {
      runner.pass('成功打开个人信息页面');
    } else {
      runner.fail(`未能打开个人信息页面: ${currentUrl}`);
    }

    try {
      const avatar = await runner.driver.findElement(By.css('.avatar, .user-avatar'));
      if (await avatar.isDisplayed()) {
        runner.pass('用户头像存在');
      }
    } catch (e) {
      runner.info('未找到用户头像');
    }

    try {
      const nicknameInput = await runner.driver.findElement(By.id('nickname'));
      if (await nicknameInput.isDisplayed()) {
        runner.pass('昵称输入框存在');
      }
    } catch (e) {
      runner.info('未找到昵称输入框');
    }

    try {
      const saveButton = await runner.driver.findElement(By.xpath("//button[contains(text(),'保存')]"));
      if (await saveButton.isDisplayed()) {
        runner.pass('保存按钮存在');
      }
    } catch (e) {
      runner.info('未找到保存按钮');
    }

  } catch (error) {
    runner.fail(`测试过程出错: ${error.message}`);
  } finally {
    await runner.sleep(2000);
    await runner.teardown();
    runner.printSummary();
  }
}

async function testProgressPage() {
  const runner = new SeleniumTestRunner('项目进度页面测试');
  await runner.setup(false);

  try {
    await login(runner);

    await runner.navigateTo('/progress');
    await runner.sleep(2000);

    const currentUrl = await runner.getCurrentUrl();
    if (currentUrl.includes('/progress')) {
      runner.pass('成功打开项目进度页面');
    } else {
      runner.fail(`未能打开项目进度页面: ${currentUrl}`);
    }

    try {
      const progressBar = await runner.driver.findElement(By.css('.progress-bar, .progress-container'));
      if (await progressBar.isDisplayed()) {
        runner.pass('进度条存在');
      }
    } catch (e) {
      runner.info('未找到进度条');
    }

  } catch (error) {
    runner.fail(`测试过程出错: ${error.message}`);
  } finally {
    await runner.sleep(2000);
    await runner.teardown();
    runner.printSummary();
  }
}

async function testSettingsPage() {
  const runner = new SeleniumTestRunner('设置页面测试');
  await runner.setup(false);

  try {
    await login(runner);

    await runner.navigateTo('/settings');
    await runner.sleep(2000);

    const currentUrl = await runner.getCurrentUrl();
    if (currentUrl.includes('/settings')) {
      runner.pass('成功打开设置页面');
    } else {
      runner.fail(`未能打开设置页面: ${currentUrl}`);
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
  testHomePage,
  testFamilyManagementPage,
  testFamilyTreePage,
  testMembersPage,
  testRelationshipsPage,
  testEventsPage,
  testMediaPage,
  testMilestonesPage,
  testLocationMapPage,
  testAiRelationshipAnalysisPage,
  testFamilyStoriesPage,
  testImageImportPage,
  testOperationLogsPage,
  testProfilePage,
  testProgressPage,
  testSettingsPage
};

const isMainModule = process.argv[1]?.includes('selenium-pages-test.js');
if (isMainModule) {
  console.log('\n=== 开始运行页面测试 ===\n');

  const tests = [
    testHomePage,
    testFamilyManagementPage,
    testFamilyTreePage,
    testMembersPage,
    testRelationshipsPage,
    testEventsPage,
    testMediaPage,
    testMilestonesPage,
    testLocationMapPage,
    testAiRelationshipAnalysisPage,
    testFamilyStoriesPage,
    testImageImportPage,
    testOperationLogsPage,
    testProfilePage,
    testProgressPage,
    testSettingsPage
  ];

  for (const test of tests) {
    await test();
    await new Promise(resolve => setTimeout(resolve, 1000));
  }

  console.log('\n=== 所有页面测试完成 ===');
  process.exit(0);
}