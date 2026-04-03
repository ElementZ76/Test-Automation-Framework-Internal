package com.automation.stepdef;

import com.automation.driver.DriverManager;
import com.automation.pages.BasePage;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.automation.pages.BasePage.*;
import com.automation.driver.DriverManager.*;
import com.automation.driver.DriverFactory.*;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

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
            try {
                byte[] screenshot = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "Failed Screenshot");
                log.info("Screenshot attached for: {}", scenario.getName());
            } catch (Exception e) {
                log.error("Screenshot capture failed: {}", e.getMessage());
            }
        }
        if (scenario.isFailed()) {
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