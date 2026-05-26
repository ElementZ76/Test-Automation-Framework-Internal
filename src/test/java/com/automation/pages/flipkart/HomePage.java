package com.automation.pages.flipkart;

import com.automation.pages.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.automation.driver.DriverManager.*;

import static com.automation.driver.DriverManager.getDriver;

public class HomePage extends BasePage {

    @FindBy(xpath = "//div[.//span[text()='Login']]//span[@role='button']")
    private WebElement closeLoginButton;

    @FindBy(xpath = "//a[contains(@href,'flights-travel-uhp-at-store')]")
    private WebElement travelButton;

    @FindBy(xpath = "//input[@name='q' and @type='text']")
    private WebElement searchBarInput;

    public void dismissLoginPopup() {
        closeLoginButton.click();
    }

    public boolean isOnHomePage() {
        try {
            waitForVisibility(travelButton);
            return travelButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSearchBarVisible() {
        try {
            waitForVisibility(searchBarInput);
            return searchBarInput.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public HomePage() {
        PageFactory.initElements(getDriver(),this);
        log.info("Homepage Initialized");
    }
}
