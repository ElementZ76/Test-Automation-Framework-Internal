package com.automation.stepdef;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import java.io.ByteArrayInputStream;
import com.automation.base.TestBase;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;

public class ApplicationHooks extends TestBase {
	@Before
	public void launchBrowser() {
		initialization();
	}
	
    @After(order = 1)
    public void captureScreenshotOnFailure(Scenario scenario) {
        if (scenario.isFailed()) {
            log.error("Scenario FAILED: {}", scenario.getName());
            try {
                byte[] screenshot = ((TakesScreenshot) driver)
                        .getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", 
                        "Screenshot - " + scenario.getName());
                Allure.addAttachment(
                        "Screenshot on Failure",
                        "image/png",
                        new ByteArrayInputStream(screenshot),
                        "png"
                );

                log.info("Screenshot attached for: {}", scenario.getName());
            } catch (Exception e) {
                log.error("Screenshot capture failed: {}");
            }
        }
    }

    @After(order = 0)
    public void closeBrowser(Scenario scenario) {
        if (scenario.isFailed()) {
            log.error("Test completed with FAILURE status");
        } else {
            log.info("Scenario PASSED: {}", scenario.getName());
        }
        if (driver != null) {
            driver.quit();
            log.info("Browser closed.");
        }
    }
}
