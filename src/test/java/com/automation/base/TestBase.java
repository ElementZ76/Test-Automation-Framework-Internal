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
	
	/**
	 * Method to initialize config file
	 */
	public TestBase() {
		try {
			prop = new Properties();
			FileInputStream ip = new FileInputStream("src/test/resources/config.properties");
			prop.load(ip);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to launch browser with pop up's disabled
	 */
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

		    //Only run headless in CI environment
		    boolean isCI = System.getenv("CI") != null;
		    if (isCI) {
		        options.addArguments("--headless");
		        options.addArguments("--no-sandbox");
		        options.addArguments("--disable-dev-shm-usage");
		        options.addArguments("--window-size=1920,1080");
		        log.info("Running in CI mode - headless enabled");
		    } else {
		        log.info("Running locally - headed mode, browser will be visible");
		    }
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
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		log.info("Launching Browser: " + prop.getProperty("browser"));
	}
	
	/**
	 * Method to wait for element to be visible
	 * @param element
	 */
	public void waitForVisibility(WebElement element) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOf(element));
	}
	
	/**
	 * Method to wait for element to be clickable
	 * @param element
	 */
	public void waitForClickability(WebElement element) {
		new WebDriverWait(driver, Duration.ofSeconds(10))
		.ignoring(StaleElementReferenceException.class)
		.until(ExpectedConditions.elementToBeClickable(element));
	}
	
	/**
	 * Method to click element with retry attempts
	 * Logic added for StaleElementReferenceException
	 * @param element
	 */
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
	
	/**
	 * Method to enter text in an input field
	 * @param textbox
	 * @param text
	 */
	public void sendText(WebElement element, String text) {
		int attempts = 0;
		while(attempts<3) {
			try {
				waitForVisibility(element);
				element.click();
				element.sendKeys(Keys.chord(Keys.CONTROL, "a"));
				element.sendKeys(Keys.DELETE);
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
	
	/**
	 * Method to wait for element to disappear
	 * @param element
	 */
	public void invisibilityOfElement(WebElement element) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.invisibilityOf(element));
	}
}
