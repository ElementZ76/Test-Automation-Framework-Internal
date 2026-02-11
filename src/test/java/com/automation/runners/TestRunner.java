package com.automation.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
    features = "src/test/resources/features/", //points to features
    glue = "com.automation.stepdef", //points to step definitions
    plugin = {
    		"pretty",
            "html:target/cucumber-reports/cucumber.html",
            "json:target/cucumber-reports/cucumber.json",
            "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm" 
    	    },
    monochrome = true
)
public class TestRunner extends AbstractTestNGCucumberTests {
}
