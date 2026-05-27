package com.automation.pages.flipkart;

import com.automation.pages.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.Objects;

import static com.automation.driver.DriverManager.getDriver;

public class SearchResultsPage extends BasePage {

    @FindBy(xpath = "//span[contains(text(),'Showing') and contains(text(),'results for')]")
    private WebElement searchResultsText;


    public boolean isLoaded() {
        boolean loaded = Objects.requireNonNull(getDriver().getCurrentUrl()).contains("/search?q=");
        log.info("Search Results page has loaded successfully");
        return loaded;
    }

    public int hasResults() {
        waitForVisibility(searchResultsText);
        String text = searchResultsText.getText();
        String total = text.split("of ")[1].split(" results")[0];
        int count = Integer.parseInt(total.trim());
        log.info("Search results page is displaying relevant results");
        return count;
    }

    public SearchResultsPage() {PageFactory.initElements(getDriver(), this);}
}
