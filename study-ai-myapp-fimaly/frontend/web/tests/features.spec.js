import { test, expect } from '@playwright/test';

test.describe('Location Map Tests', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/login');
    await page.fill('input[id="email"]', 'bage@qq.com');
    await page.fill('input[id="password"]', 'bage1234');
    await page.click('button[type="submit"]');
    await expect(page).toHaveURL('/home');
  });

  test('should navigate to location map page', async ({ page }) => {
    await page.click('a[href="/location-map"]');
    await expect(page).toHaveURL('/location-map');
  });

  test('should display location map', async ({ page }) => {
    await page.click('a[href="/location-map"]');
    await expect(page).toHaveURL('/location-map');
  });
});

test.describe('AI Relationship Analysis Tests', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/login');
    await page.fill('input[id="email"]', 'bage@qq.com');
    await page.fill('input[id="password"]', 'bage1234');
    await page.click('button[type="submit"]');
    await expect(page).toHaveURL('/home');
  });

  test('should navigate to AI relationship analysis page', async ({ page }) => {
    await page.click('a[href="/ai-relationship"]');
    await expect(page).toHaveURL('/ai-relationship');
  });

  test('should display AI relationship analysis', async ({ page }) => {
    await page.click('a[href="/ai-relationship"]');
    await expect(page).toHaveURL('/ai-relationship');
  });
});

test.describe('Operation Logs Tests', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/login');
    await page.fill('input[id="email"]', 'bage@qq.com');
    await page.fill('input[id="password"]', 'bage1234');
    await page.click('button[type="submit"]');
    await expect(page).toHaveURL('/home');
  });

  test('should navigate to operation logs page', async ({ page }) => {
    await page.click('a[href="/operation-logs"]');
    await expect(page).toHaveURL('/operation-logs');
  });

  test('should display operation logs', async ({ page }) => {
    await page.click('a[href="/operation-logs"]');
    await expect(page).toHaveURL('/operation-logs');
  });
});

test.describe('Milestones Tests', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/login');
    await page.fill('input[id="email"]', 'bage@qq.com');
    await page.fill('input[id="password"]', 'bage1234');
    await page.click('button[type="submit"]');
    await expect(page).toHaveURL('/home');
  });

  test('should navigate to milestones page', async ({ page }) => {
    await page.click('a[href="/milestones"]');
    await expect(page).toHaveURL('/milestones');
  });

  test('should display milestones', async ({ page }) => {
    await page.click('a[href="/milestones"]');
    await expect(page).toHaveURL('/milestones');
  });
});

test.describe('Family Stories Tests', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/login');
    await page.fill('input[id="email"]', 'bage@qq.com');
    await page.fill('input[id="password"]', 'bage1234');
    await page.click('button[type="submit"]');
    await expect(page).toHaveURL('/home');
  });

  test('should navigate to family stories page', async ({ page }) => {
    await page.click('a[href="/family-stories"]');
    await expect(page).toHaveURL('/family-stories');
  });

  test('should display family stories', async ({ page }) => {
    await page.click('a[href="/family-stories"]');
    await expect(page).toHaveURL('/family-stories');
  });
});
