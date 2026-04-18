import { test, expect } from '@playwright/test';

// 测试登录和注册功能
test.describe('Authentication Tests', () => {
  // 测试登录功能
  test('should successfully login with valid credentials', async ({ page }) => {
    // 导航到登录页面
    await page.goto('/login');
    
    // 验证登录页面是否正确加载（检查登录按钮）
    await expect(page.locator('button[type="submit"]')).toContainText('登录');
    
    // 输入登录凭据
    await page.fill('input[id="email"]', 'bage@qq.com');
    await page.fill('input[id="password"]', 'bage1234');
    
    // 点击登录按钮
    await page.click('button[type="submit"]');
    
    // 验证是否成功跳转到首页
    await expect(page).toHaveURL('/home');
    
    // 验证首页是否显示用户信息
    await expect(page.locator('header')).toContainText('bage@qq.com');
  });
  
  // 测试注册功能
  test('should successfully register a new user', async ({ page }) => {
    // 生成唯一的邮箱地址
    const uniqueEmail = `test${Date.now()}@example.com`;
    
    // 导航到注册页面
    await page.goto('/register');
    
    // 验证注册页面是否正确加载（检查注册按钮）
    await expect(page.locator('button[type="submit"]')).toContainText('注册');
    
    // 填写注册表单
    await page.fill('input[id="email"]', uniqueEmail);
    await page.fill('input[id="password"]', 'password123');
    await page.fill('input[id="confirmPassword"]', 'password123');
    await page.fill('input[id="nickname"]', '测试用户');
    
    // 点击注册按钮
    await page.click('button[type="submit"]');
    
    // 验证是否成功跳转到首页（注册后会自动登录）
    await expect(page).toHaveURL('/home');
    
    // 验证首页是否显示用户信息
    await expect(page.locator('header')).toContainText('测试用户');
  });
  
  // 测试登录失败场景
  test('should fail login with invalid credentials', async ({ page }) => {
    // 导航到登录页面
    await page.goto('/login');
    
    // 验证登录页面是否正确加载
    await expect(page.locator('button[type="submit"]')).toContainText('登录');
    
    // 输入无效的登录凭据
    await page.fill('input[id="email"]', 'invalid@example.com');
    await page.fill('input[id="password"]', 'invalidpassword');
    
    // 点击登录按钮
    await page.click('button[type="submit"]');
    
    // 验证是否仍然在登录页面（登录失败）
    await expect(page).toHaveURL('/login');
  });
  
  // 测试注册失败场景 - 密码不匹配
  test('should fail registration when passwords do not match', async ({ page }) => {
    // 导航到注册页面
    await page.goto('/register');
    
    // 验证注册页面是否正确加载
    await expect(page.locator('button[type="submit"]')).toContainText('注册');
    
    // 填写注册表单，密码不匹配
    await page.fill('input[id="email"]', 'test@example.com');
    await page.fill('input[id="password"]', 'password123');
    await page.fill('input[id="confirmPassword"]', 'differentpassword');
    await page.fill('input[id="nickname"]', '测试用户');
    
    // 验证提交按钮是否被禁用
    const submitButton = page.locator('button[type="submit"]');
    await expect(submitButton).toBeDisabled();
  });
  
  // 测试注册失败场景 - 邮箱已存在
  test('should fail registration when email already exists', async ({ page }) => {
    // 导航到注册页面
    await page.goto('/register');
    
    // 验证注册页面是否正确加载
    await expect(page.locator('button[type="submit"]')).toContainText('注册');
    
    // 填写注册表单，使用已存在的邮箱
    await page.fill('input[id="email"]', 'bage@qq.com');
    await page.fill('input[id="password"]', 'password123');
    await page.fill('input[id="confirmPassword"]', 'password123');
    await page.fill('input[id="nickname"]', '测试用户');
    
    // 点击注册按钮
    await page.click('button[type="submit"]');
    
    // 验证是否仍然在注册页面（注册失败）
    await expect(page).toHaveURL('/register');
  });
});