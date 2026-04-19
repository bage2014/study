import {
  testLoginPage,
  testSuccessfulLogin,
  testFailedLogin,
  testLogout,
  testRegisterPage,
  testSuccessfulRegister,
  testPasswordMismatch
} from './auth/selenium-auth-test.js';

import {
  testFamilyManagementPage,
  testFamilyListDisplay,
  testFamilyTreePage,
  testCreateFamilyButton
} from './family/selenium-family-test.js';

import {
  testMemberManagementPage,
  testMemberListDisplay,
  testAddMemberButton,
  testMemberDetailPage
} from './member/selenium-member-test.js';

import {
  testMemberEventsPage,
  testOperationLogsPage,
  testFamilyStoryPage,
  testFamilyLocationPage,
  testAIRelationPage
} from './pages/selenium-pages-test.js';

async function sleep(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

async function runAllTests() {
  console.log('\n' + '='.repeat(60));
  console.log('      Selenium 自动化测试 - 全部测试用例');
  console.log('='.repeat(60) + '\n');

  console.log('提示: 浏览器将以可视化模式运行，您可以观察到整个测试过程\n');
  console.log('每个测试模块执行完成后会暂停3秒，您可以查看测试结果\n');

  const allTests = [
    // 认证测试
    { name: '认证测试 - 登录页面', fn: testLoginPage },
    { name: '认证测试 - 成功登录', fn: testSuccessfulLogin },
    { name: '认证测试 - 登录失败', fn: testFailedLogin },
    { name: '认证测试 - 退出登录', fn: testLogout },
    { name: '认证测试 - 注册页面', fn: testRegisterPage },
    { name: '认证测试 - 成功注册', fn: testSuccessfulRegister },
    { name: '认证测试 - 密码不匹配', fn: testPasswordMismatch },

    // 家族管理测试
    { name: '家族管理 - 页面加载', fn: testFamilyManagementPage },
    { name: '家族管理 - 列表显示', fn: testFamilyListDisplay },
    { name: '家族管理 - 家族树', fn: testFamilyTreePage },
    { name: '家族管理 - 创建按钮', fn: testCreateFamilyButton },

    // 成员管理测试
    { name: '成员管理 - 页面加载', fn: testMemberManagementPage },
    { name: '成员管理 - 列表显示', fn: testMemberListDisplay },
    { name: '成员管理 - 添加按钮', fn: testAddMemberButton },
    { name: '成员管理 - 详情页', fn: testMemberDetailPage },

    // 其他页面测试
    { name: '其他页面 - 成员大事件', fn: testMemberEventsPage },
    { name: '其他页面 - 操作日志', fn: testOperationLogsPage },
    { name: '其他页面 - 家庭故事', fn: testFamilyStoryPage },
    { name: '其他页面 - 家族地理', fn: testFamilyLocationPage },
    { name: '其他页面 - AI关系分析', fn: testAIRelationPage }
  ];

  let passedCount = 0;
  let failedCount = 0;

  for (const test of allTests) {
    console.log('\n' + '-'.repeat(60));
    console.log(`执行: ${test.name}`);
    console.log('-'.repeat(60));

    try {
      await test.fn();
    } catch (error) {
      console.error(`测试执行出错: ${error.message}`);
      failedCount++;
    }

    // 测试间隔
    console.log('\n⏳ 3秒后执行下一个测试...');
    await sleep(3000);
  }

  console.log('\n' + '='.repeat(60));
  console.log('      全部测试执行完成');
  console.log('='.repeat(60));
  console.log(`\n总计: 通过 ${passedCount} 个，失败 ${failedCount} 个`);
  console.log('');

  if (failedCount === 0) {
    console.log('🎉 所有测试全部通过！');
  } else {
    console.log(`⚠️ 有 ${failedCount} 个测试失败，请检查上述输出。`);
  }

  process.exit(failedCount > 0 ? 1 : 0);
}

runAllTests();
