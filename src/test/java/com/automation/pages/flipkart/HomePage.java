package com.automation.pages.flipkart;

import com.automation.pages.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.Objects;

import static com.automation.driver.DriverManager.getDriver;

public class HomePage extends BasePage {

    @FindBy(xpath = "//span[@role='button'][text()='✕']")
    private WebElement dismissLoginModalButton;

    @FindBy(xpath = "//a[contains(@href,'flights-travel-uhp-at-store')]")
    private WebElement travelButton;

    @FindBy(xpath = "//input[@name='q' and @type='text']")
    private WebElement searchBarInput;

    @FindBy(xpath = "//ul[.//a[contains(@href,'as-searchtext=')]]")
    private WebElement searchSuggestionsDropdown;

    @FindBy(xpath = "//button[@type='submit' and contains(@aria-label,'Search')]")
    private WebElement searchButton;

    @FindBy(xpath = "//a[.//img[contains(@src, 'electronics.svg')]]")
    private WebElement electronicsCategoryButton;

    @FindBy(xpath = "//picture/img[contains(@src, '/fk-p-flap/') and @alt='Image']")
    private WebElement promotionalOffersBanner;

    @FindBy(xpath = "//a[contains(@href, 'suggested-product') and .//div[text()='Suggested For You']]")
    private WebElement suggestedForYouButton;

    public void dismissLoginPopup() {
        log.info("Attempting to close login pop up");
        try {
            waitForClickability(dismissLoginModalButton);
            dismissLoginModalButton.click();
            log.info("Closed login pop up");
        } catch (Exception e) {
            log.info("Failed closing login pop up: {}", e.getMessage());
        }
    }

    public boolean isOnHomePage() {
        try {
            waitForVisibility(travelButton);
            boolean button = travelButton.isDisplayed();
            String buttonText = travelButton.getText();
            log.info("On Homepage, unique element to homepage {} is displayed", buttonText);
            return button;
        } catch (Exception e) {
            log.info("Not on home page");
            return false;
        }
    }

    public boolean isSearchBarVisible() {
        try {
            waitForVisibility(searchBarInput);
            boolean searchVisible = searchBarInput.isDisplayed();
            log.info("Search bar is visible");
            return searchVisible;
        } catch (Exception e) {
            log.info("Search bar is not visible: {}", e);
            return false;
        }
    }

    public SearchResultsPage searchFor(String productName) {
        try {
            waitForClickability(searchBarInput);
            sendText(searchBarInput, productName);
            searchButton.click();
            log.info("Entered product name as {} and clicked search", productName);
            return new SearchResultsPage();
        } catch (Exception e) {
            log.info("Could not enter product name:{}", e);
            throw new RuntimeException("Failed to enter product name");
        }
    }

    public void enterPartialTerm(String partialTerm) {
        waitForVisibility(searchBarInput);
        sendText(searchBarInput, partialTerm);
        log.info("Entered partial search term: {}", partialTerm);
    }

    public boolean isSearchDropdownVisible() {
        waitForVisibility(searchSuggestionsDropdown);
        boolean isAppear = searchSuggestionsDropdown.isDisplayed();
        log.info("Verified search suggestions dropdown is visible");
        return isAppear;
    }

    public void clickElectronicsCategory() {
        log.info("Attempting to click the Electronics category button.");
        try {
            waitForClickability(electronicsCategoryButton);
            electronicsCategoryButton.click();
            log.info("Successfully clicked the Electronics category button.");
        } catch (Exception e) {
            log.error("Failed to click the Electronics category button: {}", e.getMessage());
            throw e;
        }
    }

    public boolean checkElectronicsURL() {
        String currentURL = Objects.requireNonNull(getDriver().getCurrentUrl());
        boolean isElectronicsURL = currentURL.contains("new-elec");
        if(isElectronicsURL){
            log.info("Electronics Page is displayed");
        } else {
            log.info("Electronics Page is not displayed");
        }
        return isElectronicsURL;
    }

    public boolean isOffersBannerDisplayed() {
        log.info("Verifying visibility of the homepage promotional offers banner.");
        try {
            boolean isVisible = promotionalOffersBanner.isDisplayed();
            log.info("Homepage promotional offers banner display status: {}", isVisible);
            return isVisible;
        } catch (Exception e) {
            log.error("Promotional offers banner could not be detected on the DOM: {}", e.getMessage());
            return false;
        }
    }

    public HomePage() {
        PageFactory.initElements(getDriver(),this);
        log.info("Homepage Initialized");
    }
}