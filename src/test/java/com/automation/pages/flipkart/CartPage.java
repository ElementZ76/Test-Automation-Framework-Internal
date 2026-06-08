package com.automation.pages.flipkart;

import com.automation.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static com.automation.driver.DriverManager.getDriver;

public class CartPage extends BasePage {

    @FindBy(xpath = "//div[@dir='auto' and starts-with(normalize-space(.), 'Price (')]")
    private WebElement priceItemCountLabel;

    @FindBy(xpath = "//div[@dir='auto' and normalize-space(text())='Place order']")
    private WebElement placeOrderButton;

    @FindBy(xpath = "//div[contains(text(), 'Log in to complete your shopping')]")
    private WebElement loginPromptHeader;

    @FindBy(xpath = "//div[@style[contains(.,'cursor: pointer') or contains(.,'cursor:pointer')]][.//div[text()='Remove']]")
    private WebElement removeProductButton;

    @FindBy(xpath = "//div[@dir='auto'][text()='Missing Cart items?']")
    private WebElement missingCartItemsMessage;

    private final String PRODUCT_TITLE_XPATH =
            "//div[@dir='auto' and contains(string(), 'SEARCH_TOKEN')]";

    public boolean isProductPresentInCart(String searchTerm) {
        String xpath = PRODUCT_TITLE_XPATH.replace("SEARCH_TOKEN", searchTerm);
        try {
            new WebDriverWait(getDriver(), Duration.ofSeconds(10))
                    .until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
            WebElement productElement = getDriver().findElement(By.xpath(xpath));
            log.info("Product found in cart. Search term: '{}', Cart title: '{}'",
                    searchTerm, productElement.getText().trim());
            return true;
        } catch (Exception e) {
            log.error("Product not found in cart. Search term: '{}'", searchTerm);
            return false;
        }
    }

    public void refreshCartPage() {
        getDriver().navigate().refresh();
    }

    public int extractItemCount(String rawLabelText) {
        log.debug("Extracting item count from text: '{}'", rawLabelText);
        String numericPart = rawLabelText.replaceAll("[^0-9]", "");
        if (numericPart.isEmpty()) {
            throw new RuntimeException(
                    "extractItemCount: no digits found in label text: '" + rawLabelText + "'. " +
                            "Check if the priceItemCountLabel locator matched the correct element.");
        }
        int itemCount = Integer.parseInt(numericPart);
        log.info("Extracted item count: {}", itemCount);
        return itemCount;
    }

    public boolean compareItemCount(int countBefore, int countAfter) {
        log.info("Comparing cart item counts - Before: {}, After: {}", countBefore, countAfter);
        boolean isMatch = (countBefore == countAfter);
        if (isMatch) {
            log.info("Success: Cart item count persisted after refresh.");
        } else {
            log.error("Failure: Cart item count changed or was lost after refresh.");
        }
        return isMatch;
    }

    public void clickPlaceOrder() {
        waitForClickability(placeOrderButton);
        try {
            clickOn(placeOrderButton);
            log.info("Successfully clicked the 'Place order' button.");
        } catch (Exception e) {
            log.error("Failed to click the 'Place order' button. Exception: {}", e.getMessage());
            throw e;
        }
    }

    public boolean isLoginPromptHeaderPresent() {
        log.info("Checking if the login prompt is displayed.");
        try {
            boolean isDisplayed = loginPromptHeader.isDisplayed();
            log.info("Login prompt display status: {}", isDisplayed);
            return isDisplayed;
        } catch (Exception e) {
            log.error("Login prompt element not visible or found: {}", e.getMessage());
            return false;
        }
    }

    public void removeProductFromCart() {
        log.info("Attempting to click the product item 'Remove' button in the cart dashboard.");
        try {
            waitForClickability(removeProductButton);
            clickOn(removeProductButton);
            log.info("Successfully clicked the 'Remove' button target block.");
        } catch (Exception e) {
            log.error("Failed to interact with the cart item 'Remove' button element: {}", e.getMessage());
            throw e;
        }
    }

    public boolean isCartEmptyMessageDisplayed() {
        try {
            boolean isDisplayed = missingCartItemsMessage.isDisplayed();
            log.info("Empty cart state assertion check completed. Display status: {}", isDisplayed);
            return isDisplayed;
        } catch (Exception e) {
            log.error("Failed to detect or assert visibility for the empty cart state header node: {}", e.getMessage());
            return false;
        }
    }

    public String getPriceItemCountLabelText() {
        waitForVisibility(priceItemCountLabel);
        String text = priceItemCountLabel.getText();
        log.info("priceItemCountLabel raw text: '{}'", text);
        return text;
    }

    public CartPage() {
        PageFactory.initElements(getDriver(), this);
    }
}
