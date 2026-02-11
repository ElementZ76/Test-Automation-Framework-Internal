package com.automation.base;

import java.io.FileInputStream;
import java.time.Duration;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;

public class TestBase {
	public static WebDriver driver;
	public static WebDriverWait wait;
	public static Properties prop;
	public static Logger log = LogManager.getLogger(TestBase.class);
	
//	implict wait for 10
//	WebDriverWait waitFor10 = new WebDriverWait(driver, Duration.ofSeconds(10));
	
	//method to initialize the config file
	public TestBase() {
		try {
			prop = new Properties();
			FileInputStream ip = new FileInputStream("src/test/resources/config.properties");
			prop.load(ip);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//method to launch browser
	public void initialization() {
		String browserName = prop.getProperty("browser");
		if(browserName.equals("chrome")) {
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--disable-save-password-bubble");
			options.setExperimentalOption(
			    "prefs",
			    Map.of(
			        "credentials_enable_service", false,
			        "profile.password_manager_enabled", false,
			        "profile.password_manager_leak_detection", false 
			    )
			);
			driver = new ChromeDriver(options);
		}
		else if (browserName.equals("edge")) {
			driver = new EdgeDriver();
		}
		else if(browserName.equals("firefox")) {
			driver = new FirefoxDriver();
		}
		else {
			log.error("Browser name mismatch. Given browser name '{}' is wrong.", browserName);
		}
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.manage().deleteAllCookies();
//		driver.get(prop.getProperty("url"));
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		log.info("Launching Browser: " + prop.getProperty("browser"));
//        log.info("Navigating to URL: " + prop.getProperty("url"));
	}
	
	//wait for element to be visible
	public void waitForVisibility(WebElement element) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOf(element));
	}
	
	//wait for element to be clickable
	public void waitForClickability(WebElement element) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.elementToBeClickable(element));
		wait.ignoring(StaleElementReferenceException.class);
		waitForVisibility(element);
	}
	
	//method to retry clicking the element incase the element does not load. use attempts
	//prevention of stale element
	public void clickOn(WebElement element) {
		int attempts = 0;
		while(attempts<3) {
			try {
				waitForClickability(element);
				element.click();
				break;
			} catch (StaleElementReferenceException e) {
				attempts++;
				log.debug("Element was stale. Retrying...");
			} catch (Exception e) {
				attempts++;
			    log.error("ClickOn failed on attempt {}/3: {}", attempts, e.getMessage());
			    if (attempts >= 3) {
			        log.error("Max attempts reached. Throwing exception.");
			        throw new RuntimeException("Failed to send text after 3 attempts", e);
			    }
			}
		}
	}
	
	//method to enter text in input field with attempts to prevent stale element exception
	public void sendText(WebElement element, String text) {
		int attempts = 0;
		while(attempts<3) {
			try {
				waitForVisibility(element);
				element.click();
				element.sendKeys(Keys.CONTROL + "a");
				element.sendKeys(Keys.BACK_SPACE);
				element.sendKeys(text);
				break;
			} catch (StaleElementReferenceException e) {
				attempts++;
				log.debug("Element was stale. Retrying...");
			} catch(Exception e) {
				attempts++;
			    log.error("SendText failed on attempt {}/3: {}", attempts, e.getMessage());
			    if (attempts >= 3) {
			        log.error("Max attempts reached. Throwing exception.");
			        throw new RuntimeException("Failed to send text after 3 attempts", e);
			    }
			}
		}
	}
	
	//method to wait for element to disappear
	public void invisibilityOfElement(WebElement element) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.invisibilityOf(element));
	}
}
