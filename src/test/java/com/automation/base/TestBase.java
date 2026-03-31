package com.automation.base;

import java.io.FileInputStream;
import java.net.URL;
import java.time.Duration;
import java.util.Map;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
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

public class TestBase {
	
	public static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

	public static volatile Properties prop;
	private static int WAIT_TIMEOUT;
	public static Logger log = LogManager.getLogger(TestBase.class);
	
	/**
	 * Method to initialize config file
	 */
	public TestBase() {
		if(prop == null) {
			synchronized (TestBase.class) {
				if(prop==null) {
					Properties p = new Properties();
					try (FileInputStream fis = new FileInputStream("src/test/resources/config.properties")) {
						p.load(fis);
						prop=p;
						log.info("config.properties loaded successfully");
					} catch (Exception e) {
						throw new RuntimeException("Cannot read config.properties. Aborting.",e);
					}
				}
			}
		}
	}

	/**
	 * Method to resolve to given key. If no key is found in config file, resort to default value.
	 * @param key - value to take from config
	 * @param hardDefault - value to use if key is missing from config
	 * @return hardDefault if key is wrong
	 */
	private String resolve(String key, String hardDefault) {
		String sysProp = System.getProperty(key);
		if(sysProp!=null && !sysProp.trim().isEmpty()) {
			log.debug("'{}'->-D system property: '{}'", key, sysProp.trim());
			return sysProp.trim();
		}
		String configVal = prop.getProperty(key);
		if(configVal!=null && !configVal.trim().isEmpty()) {
			log.debug("'{}'->config.properties: '{}'", key, configVal.trim());
			return configVal.trim();
		}
		log.warn("'{}' not set in -D args or config.properties. Using built in default: '{}'", key, hardDefault);
		return hardDefault;
	}

	public void initialization() {
		WAIT_TIMEOUT = Integer.parseInt(resolve("implicitWait", "10"));
		String browserName = resolve("browser", "chrome");
		String executionMode = resolve("executionMode", "local");
		String gridUrl = resolve("gridUrl", "http://localhost:4444");

		log.info(" --- Driver Init | browser='{}' | executionMode='{}' | gridUrl='{}' ---", browserName, executionMode, gridUrl);
		WebDriver driver;

		if(browserName.equalsIgnoreCase("chrome")) {
			driver=buildChrome(executionMode, gridUrl);
		} else if (browserName.equalsIgnoreCase("firefox")) {
			driver = buildFirefox(executionMode, gridUrl);
		} else if (browserName.equalsIgnoreCase("edge")) {
			driver = buildEdge(executionMode, gridUrl);
		} else {
			throw new RuntimeException( "Unsupported Browser: '" + browserName + "'" +
					"Accepted values: chrome | firefox | edge" +
					"Set browser=<value> in config.properties or pass -Dbrowser=<value>");
		}

		setDriver(driver);
		getDriver().manage().window().maximize();
		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(WAIT_TIMEOUT));
		getDriver().manage().deleteAllCookies();
		log.info("[Thread {}] Browser launched: {}", Thread.currentThread().getId(), browserName);
		
	}

	public WebDriver buildChrome(String executionMode, String gridUrl) {
		ChromeOptions options = new ChromeOptions();
		options.addArguments(prop.getProperty("passwordBubble", "--disable-save-password-bubble"));
		options.setExperimentalOption("prefs", Map.of(
				"credentials_enable_service", false,
				"profile.password_manager_enabled", false,
				"profile.password_manager_leak_detection", false
		));
		if(System.getenv("CI")!=null) {
			options.addArguments("--start-maximized");
			options.addArguments(
					prop.getProperty("headless", "--headless"),
					prop.getProperty("nosandbox", "--no-sandbox"),
					prop.getProperty("shmUsage", "--disable-dev-shm-usage"),
					prop.getProperty("windowSize", "--window-size=1920, 720"));
			log.info("CI environment detected - Chrome running headless");
		}
		if("grid".equalsIgnoreCase(executionMode)) {
			return connectToGrid(gridUrl, options);
		}
		return new ChromeDriver(options);
	}

	public WebDriver buildEdge(String executionMode, String gridUrl) {
		EdgeOptions options = new EdgeOptions();
		options.addArguments("--start-maximized");    
		if("grid".equalsIgnoreCase(executionMode)) {
			return connectToGrid(gridUrl, options);
		}
		return new EdgeDriver();
	}

	public WebDriver buildFirefox(String executionMode, String gridUrl) {
		FirefoxOptions options = new FirefoxOptions();
		options.addArguments("--start-maximized");    
		if("grid".equalsIgnoreCase(executionMode)){
			return connectToGrid(gridUrl, options);
		}
		return new FirefoxDriver();
	}

	public static WebDriver getDriver() {
	        return driverThreadLocal.get();
	}                                                                    

	private WebDriver connectToGrid(String gridUrl, org.openqa.selenium.MutableCapabilities options) {
		try {
			WebDriver driver = new RemoteWebDriver(new URL(gridUrl), options);
			log.info("[thread{}] RemoteWebDriver connected to Grid: {}", Thread.currentThread().getId(), gridUrl);
			return driver;
		} catch (Exception e) {
            throw new RuntimeException("Failed to connect to Selenium Grid at: "+gridUrl, e);
        }
    }

	public static void quitDriver() {
		if (getDriver() != null) {
            getDriver().quit();
            driverThreadLocal.remove();
            log.info("[Thread {}] Browser closed and ThreadLocal cleared.", Thread.currentThread().getId());
        }
	}


	private static void setDriver(WebDriver driver) {
	        driverThreadLocal.set(driver);
	}

	/**
	 * Method to wait for element to be visible
     */
	public void waitForVisibility(WebElement element) {
		WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(WAIT_TIMEOUT));
		wait.until(ExpectedConditions.visibilityOf(element));
	}
	
	/**
	 * Method to wait for element to be clickable
     */
	public void waitForClickability(WebElement element) {
		new WebDriverWait(getDriver(), Duration.ofSeconds(WAIT_TIMEOUT))
		.ignoring(StaleElementReferenceException.class)
		.until(ExpectedConditions.elementToBeClickable(element));
	}
	
	/**
	 * Method to click element with retry attempts
	 * Logic added for StaleElementReferenceException
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
     */
	public void invisibilityOfElement(WebElement element) {
		WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(WAIT_TIMEOUT));
		wait.until(ExpectedConditions.invisibilityOf(element));
	}
}
