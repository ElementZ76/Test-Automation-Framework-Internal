package com.automation.stepdef.flipkart;

import com.automation.driver.DriverManager;
import com.automation.pages.BasePage;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.ByteArrayInputStream;

import static com.automation.driver.DriverFactory.initializeDriver;
import static com.automation.driver.DriverManager.getDriver;

public class ApplicationHooks extends BasePage {

    @Before
    public void launchBrowser() {
        initializeDriver();
    }

    @After(order = 1)
    public void captureScreenshotOnFailure(Scenario scenario) {
        if (scenario.isFailed()) {
            log.error("[Thread {}] Scenario FAILED: {}", Thread.currentThread().getId(), scenario.getName());
            if (getDriver() == null) {
                log.warn("Driver is null; skipping screenshot for: {}", scenario.getName());
            } else {
                try {
                    byte[] screenshot = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BYTES);
                    Allure.addAttachment("Failed Screenshot", "image/png", new ByteArrayInputStream(screenshot), "png");
                    log.info("Screenshot attached for: {}", scenario.getName());
                } catch (Exception e) {
                    log.error("Screenshot capture failed: {}", e.getMessage());
                }
            }
            log.error("Test completed with FAILURE status");
        } else {
            log.info("Scenario PASSED: {}", scenario.getName());
        }
    }

    @After(order = 0)
    public void closeBrowser(Scenario scenario) {
        DriverManager.quitDriver();
    }
}