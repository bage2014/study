package com.bage.study.ai.config;

import com.microsoft.playwright.*;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Slf4j
@Configuration
public class RpaConfig {

    @Value("${rpa.enabled:true}")
    private boolean rpaEnabled;

    @Value("${rpa.headless:true}")
    private boolean headless;

    @Value("${rpa.timeout:30000}")
    private int timeout;

    @Value("${rpa.page-load-timeout:60000}")
    private int pageLoadTimeout;

    private Playwright playwright;
    private Browser browser;

    @Bean
    public Playwright playwright() {
        if (!rpaEnabled) {
            log.info("RPA is disabled, skipping Playwright initialization");
            return null;
        }
        try {
            playwright = Playwright.create();
            log.info("Playwright initialized successfully");
            return playwright;
        } catch (Exception e) {
            log.error("Failed to initialize Playwright: {}", e.getMessage());
            return null;
        }
    }

    @Bean
    public Browser browser(Playwright playwright) {
        if (!rpaEnabled || playwright == null) {
            log.info("RPA is disabled or Playwright not initialized, skipping browser creation");
            return null;
        }
        try {
            browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                    .setHeadless(headless)
                    .setTimeout(timeout)
                    .setArgs(java.util.Arrays.asList(
                            "--disable-blink-features=AutomationControlled",
                            "--start-maximized",
                            "--no-sandbox",
                            "--disable-dev-shm-usage",
                            "--disable-gpu",
                            "--disable-infobars",
                            "--disable-extensions"
                    )));
            log.info("Browser launched successfully (headless: {})", headless);
            return browser;
        } catch (Exception e) {
            log.error("Failed to launch browser: {}", e.getMessage());
            return null;
        }
    }

    @Bean
    public BrowserContext context(Browser browser) {
        if (!rpaEnabled || browser == null) {
            return null;
        }
        BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(1920, 1080)
                .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"));
        context.setDefaultTimeout(timeout);
        context.addInitScript("() => { Object.defineProperty(navigator, 'webdriver', { get: () => undefined }); }");
        return context;
    }

    @Bean
    public ScheduledExecutorService rpaExecutor() {
        return Executors.newScheduledThreadPool(2);
    }

    @PreDestroy
    public void destroy() {
        if (browser != null) {
            try {
                browser.close();
                log.info("Browser closed");
            } catch (Exception e) {
                log.error("Error closing browser: {}", e.getMessage());
            }
        }
        if (playwright != null) {
            try {
                playwright.close();
                log.info("Playwright closed");
            } catch (Exception e) {
                log.error("Error closing Playwright: {}", e.getMessage());
            }
        }
    }

    public boolean isRpaEnabled() {
        return rpaEnabled;
    }

    public int getTimeout() {
        return timeout;
    }

    public int getPageLoadTimeout() {
        return pageLoadTimeout;
    }
}
