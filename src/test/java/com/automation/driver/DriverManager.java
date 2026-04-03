package com.automation.driver;

import org.apache.logging.log4j.*;
import org.openqa.selenium.WebDriver;

public class DriverManager {
    private static final Logger log = LogManager.getLogger(DriverManager.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    private DriverManager(){}

    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    public static void setDriver(WebDriver driver) {
        driverThreadLocal.set(driver);
    }

    public static void quitDriver() {
        if(getDriver()!=null) {
            getDriver().quit();
            driverThreadLocal.remove();
            log.info("[Thread {}] Browser closed and ThreadLocal cleared.", Thread.currentThread().getId());
        }
    }
}
