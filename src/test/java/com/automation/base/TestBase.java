package com.automation.base;

import java.io.FileInputStream;
import java.net.URL;
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
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;

public class TestBase {
	
	public static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
	
	public static WebDriverWait wait;
	public static Properties prop;
	public static Logger log = LogManager.getLogger(TestBase.class);
	
	public static WebDriver getDriver() {
		return driverThreadLocal.get();
	}
	private static void setDriver(WebDriver driver) {
		driverThreadLocal.set(driver);
	}
	
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
		String browserName = System.getProperty("browser", prop.getProperty("browser"));
		String executionMode = System.getProperty("executionMode", prop.getProperty("gridUrl"));
		String gridUrl = System.getProperty("gridUrl", prop.getProperty("gridUrl"));
		WebDriver driver;
		
		if(browserName.equalsIgnoreCase("chrome")) {
		    ChromeOptions options = new ChromeOptions();
		    options.addArguments(prop.getProperty("passwordBubble"));
		    options.setExperimentalOption(
		        "prefs",
		        Map.of(
		            "credentials_enable_service", false,
		            "profile.password_manager_enabled", false,
		            "profile.password_manager_leak_detection", false
		        )
		    );

		    boolean isCI = System.getenv("CI") != null;
		    if (isCI) {
		        options.addArguments(prop.getProperty("headless"));
		        options.addArguments(prop.getProperty("nosandbox"));
		        options.addArguments(prop.getProperty("shmUsage"));
		        options.addArguments(prop.getProperty("windowSize"));
		        log.info("Running in CI mode - headless enabled");
		    } else {
		        log.info("Running locally - headed mode, browser will be visible");
		    }
		    
		    if("grid".equalsIgnoreCase(executionMode)) {
		    	try {
		    		driver = new RemoteWebDriver(new URL(gridUrl), options);
		    		log.info("[Thread {}] RemoteWebDriver launched on grid: {}", Thread.currentThread().getId(), gridUrl);
		    	} catch(Exception e) {
		    		throw new RuntimeException("Failed to connect to Selenium Grid at:"+gridUrl, e);
		    	}
		    } else {
		    	driver = new ChromeDriver(options);
		    }
		}
		
		else if (browserName.equals("edge")) {
			EdgeOptions edgeOptions = new EdgeOptions();
			if("grid".equalsIgnoreCase(executionMode)) {
				try {
					driver = new RemoteWebDriver(new URL(gridUrl), edgeOptions);
					log.info("[Thread {}] RemoteWebDriver(Edge) launched on Grid:{}", Thread.currentThread().getId(), gridUrl);
				} catch (Exception e) {
					log.info("Failed to connect Selenium Grid at:"+gridUrl, e);
				}
			}
			driver = new EdgeDriver();
		}
		
		else if(browserName.equals("firefox")) {
			FirefoxOptions ffoptions = new FirefoxOptions();
			if("grid".equalsIgnoreCase(executionMode)) {
				try {
					driver = new RemoteWebDriver(new URL(gridUrl), ffoptions);
					log.info("[Thread {}] RemoteWebDriver Firefox launched on Grid: {}", Thread.currentThread().getId(), gridUrl);
				} catch (Exception e) {
					log.info("Failed to connect Selenium grid at:"+gridUrl, e);
				}
			}
			driver = new FirefoxDriver();
		}
		else {
			log.error("Browser name mismatch. Given browser name '{}' is wrong. Defaulting to Chrome (local)", browserName);
			driver = new ChromeDriver();
		}
		
		setDriver(driver);
		
		getDriver().manage().window().maximize();
		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		getDriver().manage().deleteAllCookies();
		log.info("[Thread {}] Browser launched: {}", Thread.currentThread().getId(), browserName);
		
	}
	
	public static void quitDriver() {
		if (getDriver() != null) {
            getDriver().quit();
            driverThreadLocal.remove();
            log.info("[Thread {}] Browser closed and ThreadLocal cleared.", Thread.currentThread().getId());
        }
	}
	
	/**
	 * Method to wait for element to be visible
	 * @param element
	 */
	public void waitForVisibility(WebElement element) {
		WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOf(element));
	}
	
	/**
	 * Method to wait for element to be clickable
	 * @param element
	 */
	public void waitForClickability(WebElement element) {
		new WebDriverWait(getDriver(), Duration.ofSeconds(10))
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
		WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
		wait.until(ExpectedConditions.invisibilityOf(element));
	}
}
