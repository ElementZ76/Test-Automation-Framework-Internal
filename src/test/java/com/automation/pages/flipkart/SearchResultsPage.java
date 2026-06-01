package com.automation.pages.flipkart;

import com.automation.pages.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.Objects;

import static com.automation.driver.DriverManager.getDriver;

public class SearchResultsPage extends BasePage {

    @FindBy(xpath = "//span[contains(text(),'Showing') and contains(text(),'results for')]")
    private WebElement searchResultsText;

    @FindBy(xpath = "//div[text()='Sorry, no results found!']")
    private WebElement noResultsMessage;

    @FindBy(xpath = "//a[contains(@href,'/p/itm')]")
    private List<WebElement> productNames;


    public boolean isSearchResultsPageLoaded() {
        String currentUrl = Objects.requireNonNull(getDriver().getCurrentUrl());
        boolean loaded = currentUrl.contains("/search?q=");
        if (loaded) {
            log.info("Search Results page loaded successfully. URL: {}", currentUrl);
        } else {
            log.error("Search Results page did not load correctly. Current URL: {}", currentUrl);
        }
        return loaded;
    }

    public int getResultsCount() {
        waitForVisibility(searchResultsText);
        String text = searchResultsText.getText();
        String totalResults = text.split("of ")[1].split(" results")[0];
        int resultsCount = Integer.parseInt(totalResults.trim());
        log.info("Total search results displayed: {}", resultsCount);
        return resultsCount;
    }

    public boolean isNoResultsMessageDisplayed() {
        waitForVisibility(noResultsMessage);
        boolean isDisplayed = noResultsMessage.isDisplayed();
        if (isDisplayed) {
            log.info("Verified 'No Results' message is displayed");
        } else {
            log.error("'No Results' message is NOT displayed");
        }
        return isDisplayed;
    }

    public String getNoResultsMessageText() {
        waitForVisibility(noResultsMessage);
        String message = noResultsMessage.getText();
        log.info("Captured no-results message text: {}", message);
        return message;
    }

    public ProductDetailsPage clickFirstProduct() {
        if(productNames.isEmpty()) {
            log.error("No product results available to click");
            throw new RuntimeException("Prodcut results is empty");
        }
        WebElement firstProduct = productNames.get(0);
        waitForClickability(firstProduct);
        String productUrl = firstProduct.getAttribute("href");
        log.info("Clicking first product. URL: {}", productUrl);
        firstProduct.click();
        log.info("Successfully navigated to product detail page");
        return new ProductDetailsPage();
    }

    public SearchResultsPage() {PageFactory.initElements(getDriver(), this);}
}
