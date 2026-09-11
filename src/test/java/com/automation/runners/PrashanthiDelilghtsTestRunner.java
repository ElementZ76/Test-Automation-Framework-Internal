package com.automation.runners;

import com.automation.utils.ConfigManager;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;

@CucumberOptions(
        features = "src/test/resources/features/prashanthiDelights",
        glue = "com.automation.stepdef.prashanthiDelights",
        plugin = {
                "summary",
                "html:target/cucumber-reports/cucumber.html",
                "json:target/cucumber-reports/cucumber.json",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        monochrome = true
)
public class PrashanthiDelilghtsTestRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }

    @BeforeClass(alwaysRun = true)
    @Override
    public void setUpClass(ITestContext context) {
        int threads = Integer.parseInt(ConfigManager.get("threads", "1"));
        context.getCurrentXmlTest().getSuite().setDataProviderThreadCount(threads);
        context.getCurrentXmlTest().getSuite().setThreadCount(threads);
        super.setUpClass(context);
    }
}