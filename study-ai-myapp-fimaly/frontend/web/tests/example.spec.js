// @ts-check
import { test, expect } from '@playwright/test';

test('login page has form', async ({ page }) => {
  await page.goto('/');
  
  // Expect login form elements to be present
  await expect(page.locator('input#email')).toBeVisible();
  await expect(page.locator('input#password')).toBeVisible();
  await expect(page.locator('button[type="submit"]')).toBeVisible();
});

test('register page has form', async ({ page }) => {
  await page.goto('/register');
  
  // Expect register form elements to be present
  await expect(page.locator('input#email')).toBeVisible();
  await expect(page.locator('input#password')).toBeVisible();
  await expect(page.locator('input#confirmPassword')).toBeVisible(); // Confirm password
  await expect(page.locator('input#nickname')).toBeVisible(); // Nickname
  await expect(page.locator('button[type="submit"]')).toBeVisible();
});

test('home page is accessible', async ({ page }) => {
  // Go directly to home page (skipping login for now since backend is not running)
  await page.goto('/home');
  
  // Expect home page elements to be present
  await expect(page.locator('h2')).toBeVisible();
});
