package com.automation.driver;

import com.automation.utils.ConfigManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.Map;

public class DriverFactory {
    private static final Logger log = LogManager.getLogger(DriverFactory.class);

    public static void initializeDriver() {
        int waitTimeout = Integer.parseInt(ConfigManager.get("implicitWait", "10"));
        String browserName = ConfigManager.get("browser", "chrome").toLowerCase().trim();
        String executionMode = ConfigManager.get("executionMode", "local").toLowerCase().trim();
        String gridUrl = ConfigManager.get("gridUrl", "http://localhost:4444");
        String threadCount = ConfigManager.get("threads", String.valueOf(3));
        boolean fallbackToLocal = Boolean.parseBoolean(ConfigManager.get("fallbackToLocal", "false"));

        log.info("--- Driver Initialization | browser='{}' | mode='{}' | fallback={} | threads={} ---", browserName, executionMode, fallbackToLocal, threadCount);

        WebDriver driver;
        try {
            driver = createDriver(browserName, executionMode, gridUrl);
        } catch (Exception e) {
            if (fallbackToLocal && "grid".equals(executionMode)) {
                log.warn("Grid failed! Falling back to LOCAL execution. Error: {}", e.getMessage());
                driver = createDriver(browserName, "local", gridUrl);
            } else {
                log.error("Driver initialization failed. No fallback permitted.");
                throw new RuntimeException("Fatal Driver Initialization Failure", e);
            }
        }

        DriverManager.setDriver(driver);
        DriverManager.getDriver().manage().window().maximize();
        DriverManager.getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(waitTimeout));
        DriverManager.getDriver().manage().deleteAllCookies();
    }

    private static WebDriver createDriver(String browser, String mode, String gridUrl) {
        if (!mode.equals("local") && !mode.equals("grid")) {
            throw new IllegalArgumentException("Invalid executionMode: " + mode);
        }

        switch (browser) {
            case "chrome": return buildChrome(mode, gridUrl);
            case "firefox": return buildFirefox(mode, gridUrl);
            case "edge": return buildEdge(mode, gridUrl);
            default: throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
    }

    private static WebDriver buildChrome(String mode, String gridUrl) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments(ConfigManager.get("passwordBubble", "--disable-save-password-bubble"));
        options.addArguments("--start-maximized");
        options.addArguments("--disable-features=PasswordLeakDetection");
        options.addArguments("--password-store=basic");
        options.addArguments("--disable-save-password-bubble");
        options.setExperimentalOption("excludeSwitches", List.of("enable-automation", "enable-logging"));
        options.setExperimentalOption("prefs", Map.of(
                "credentials_enable_service", false,
                "profile.password_manager_enabled", false,
                "profile.password_manager_leak_detection", false,
                "safebrowsing_for_trusted_sources_enabled", false,
                "safebrowsing.enabled", false,
                "excludeSwitches", List.of("enable-automation", "enable-logging")
        ));

        if (System.getenv("CI") != null) {
            options.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage", "--window-size=1920,1080");
        }
        return "grid".equals(mode) ? connectToGrid(gridUrl, options) : new ChromeDriver(options);
    }

    private static WebDriver buildFirefox(String mode, String gridUrl) {
        FirefoxOptions options = new FirefoxOptions();
        if (System.getenv("CI") != null) {
            options.addArguments("-headless");
        }
        return "grid".equals(mode) ? connectToGrid(gridUrl, options) : new FirefoxDriver(options);
    }

    private static WebDriver buildEdge(String mode, String gridUrl) {
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--start-maximized");
        if (System.getenv("CI") != null) {
            options.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage", "--window-size=1920,1080");
        }
        options.addArguments("--start-maximized");
        return "grid".equals(mode) ? connectToGrid(gridUrl, options) : new EdgeDriver(options);
    }

    private static WebDriver connectToGrid(String gridUrl, org.openqa.selenium.MutableCapabilities options) {
        try {
            return new RemoteWebDriver(new URL(gridUrl), options);
        } catch (Exception e) {
            throw new RuntimeException("Grid connection refused at " + gridUrl, e);
        }
    }
}