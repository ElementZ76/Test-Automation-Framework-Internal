package com.automation.stepdef;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.automation.base.TestBase;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;

public class ApplicationHooks extends TestBase {
	@Before
	public void launchBrowser() {
		initialization();
	}
	
	@After
	public void tearDown(Scenario scenario) {
		if (scenario.isFailed()) {
            // Take screenshot
            byte[] screenshot = ((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.BYTES);
            
            // Attach to Cucumber report
            scenario.attach(screenshot, "image/png", scenario.getName());
            
            // Attach to Allure report
            Allure.addAttachment(
                scenario.getName() + " - Failed", 
                "image/png", 
                new java.io.ByteArrayInputStream(screenshot), 
                ".png"
            );
        }
		
		if(driver!=null) {
			driver.quit();
		}
	}
}
