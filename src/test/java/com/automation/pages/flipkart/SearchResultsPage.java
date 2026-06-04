package com.automation.pages.flipkart;

import com.automation.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.Collections;
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

    @FindBy(xpath = "//div[starts-with(normalize-space(),'₹')]")
    private List<WebElement> productPrices;

    @FindBy(xpath = "//a[@href='/']/img[@alt='Flipkart']")
    private WebElement flipkartHomeLogoButton;

    private final String BRAND_FILTER_XPATH =
            "//section[.//div[normalize-space()='Brand']]//label[.//div[normalize-space()='%s']]";

    private final String ACTIVE_BRAND_FILTER_XPATH =
            "//div[./div[normalize-space()='✕'] and ./div[normalize-space()='%s']]";

    private final String SORT_OPTION_XPATH =
            "//div[normalize-space()='%s']";

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
        String[] parts = text.split("of ");
        String totalResults = parts.length > 1 ? parts[1].split(" results")[0] : "0";
        String cleaned = totalResults.trim().replaceAll("[^0-9]", "");
        int resultsCount = cleaned.isEmpty() ? 0 : Integer.parseInt(cleaned);
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

    public void applyBrandFilter(String brandName) {
        String xpath = String.format(BRAND_FILTER_XPATH, brandName);
        WebElement brandFilter = getDriver().findElement(By.xpath(xpath));
        waitForClickability(brandFilter);
        log.info("Applying brand filter: {}", brandName);
        brandFilter.click();
        log.info("Successfully selected brand filter: {}", brandName);
    }

    public boolean isBrandFilterApplied(String brandName) {
        String xpath = String.format(ACTIVE_BRAND_FILTER_XPATH, brandName);
        try {
            WebElement activeFilter = getDriver().findElement(By.xpath(xpath));
            boolean isDisplayed = activeFilter.isDisplayed();
            log.info("Verifying active brand filter chip: {}", brandName);
            log.info("Brand filter '{}' applied status: {}", brandName, isDisplayed);
            return isDisplayed;
        } catch (NoSuchElementException e) {
            log.error("Brand filter chip '{}' not found.", brandName);
            return false;
        }
    }

    public void selectSortOption(String sortOption) {
        String xpath = String.format(SORT_OPTION_XPATH, sortOption);
        WebElement option = getDriver().findElement(By.xpath(xpath));
        waitForClickability(option);
        log.info("Selecting sort option: {}", sortOption);
        option.click();
        log.info("Successfully selected sort option: {}", sortOption);
    }

    public boolean areProductPricesSortedByAscending() {
        waitForVisibility(productPrices.get(0));
        List<Integer> actualPrices = new ArrayList<>();
        for (WebElement priceElement : productPrices) {
            String priceText = priceElement.getText()
                    .replace("₹", "")
                    .replace(",", "")
                    .trim();

            actualPrices.add((int) Long.parseLong(priceText));
        }
        log.info("Actual prices displayed: {}", actualPrices);
        List<Integer> sortedPrices = new ArrayList<>(actualPrices);
        Collections.sort(sortedPrices);
        boolean isSorted = actualPrices.equals(sortedPrices);
        if(isSorted) {
            log.info("Products are displayed in ascending price order");
        } else {
            log.error("Products are NOT displayed in ascending price order");
            log.error("Expected sorted prices: {}", sortedPrices);
            log.error("Actual prices: {}", actualPrices);
        }
        return isSorted;
    }

    public HomePage clickOnFlipkartLogo() {
        log.info("Attempting to click the Flipkart navbar logo button.");
        try {
            waitForClickability(flipkartHomeLogoButton);
            flipkartHomeLogoButton.click();
            log.info("Successfully clicked the Flipkart navbar logo button.");
        } catch (Exception e) {
            log.error("Failed to click the Flipkart navbar logo button: {}", e.getMessage());
            throw e;
        }
        return new HomePage();
    }

    public SearchResultsPage() {PageFactory.initElements(getDriver(), this);}
}
