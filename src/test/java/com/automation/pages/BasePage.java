package com.automation.pages;

import com.automation.driver.DriverManager;
import com.automation.utils.ConfigManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class BasePage {
    protected Logger log = LogManager.getLogger(this.getClass());
    private final int waitTimeout = Integer.parseInt(ConfigManager.get("implicitWait", String.valueOf(10)));
    private final int MAX_RETRIES = 3;

    /**
     * Method to wait for element to be visible
     */
    protected void waitForVisibility(WebElement element) {
        new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(waitTimeout))
                .until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Method to wait for element to be clickable
     */
    protected void waitForClickability(WebElement element) {
        new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(waitTimeout))
                .ignoring(StaleElementReferenceException.class)
                .until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Method to click element with retry attempts
     * Logic added for StaleElementReferenceException
     */
    protected void clickOn(WebElement element) {
        int attempts = 0;
        while (attempts<MAX_RETRIES) {
            try {
                waitForClickability(element);
                element.click();
                return;
            } catch (StaleElementReferenceException e) {
                attempts++;
                log.debug("Stale Element. Retrying........ {}/{}", attempts, MAX_RETRIES);
            } catch (Exception e) {
                log.error("Click Failed: {}", e.getMessage());
                throw new RuntimeException("Failed to click element after " + MAX_RETRIES + "attempts.", e);
            }
        }
    }

    /**
     * Method to enter text in an input field
     */
    protected void sendText(WebElement element, String text) {
        int attempts = 0;
        while (attempts < MAX_RETRIES) {
            try {
                waitForVisibility(element);
                element.click();
                element.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
                element.sendKeys(text);
                return;
            } catch (StaleElementReferenceException e) {
                attempts++;
                log.debug("Element stale. Retrying... {}/{}", attempts, MAX_RETRIES);
            } catch (Exception e) {
                log.error("SendText failed: {}", e.getMessage());
                throw new RuntimeException("Failed to send text after " + MAX_RETRIES + " attempts", e);
            }
        }
    }

    /**
     * Waits for a new tab to open and switches driver focus to it.
     * Use after clicking any element that opens a link in a new tab (target="_blank").
     */
    protected void switchToNewTab(String originalTab) {
        new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(waitTimeout))
                .until(ExpectedConditions.numberOfWindowsToBe(2));

        for (String tab : DriverManager.getDriver().getWindowHandles()) {
            if (!tab.equals(originalTab)) {
                DriverManager.getDriver().switchTo().window(tab);
                log.info("Switched to new tab. Current URL: {}", DriverManager.getDriver().getCurrentUrl());
                return;
            }
        }
        throw new RuntimeException("New tab did not open within " + waitTimeout + " seconds.");
    }
}

