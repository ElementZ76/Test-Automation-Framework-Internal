package com.automation.pages.flipkart;

import com.automation.pages.BasePage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.NoSuchElementException;
import java.util.Objects;

import static com.automation.driver.DriverManager.getDriver;

public class ProductDetailsPage extends BasePage{
    private static final Logger log = LogManager.getLogger(ProductDetailsPage.class);

    @FindBy(xpath = "//h1[normalize-space(.) != '']")
    private WebElement productTitleHeader;

    @FindBy(xpath = "//h1/following::div[starts-with(normalize-space(),'₹')][1]")
    private WebElement productPriceLabel;

    @FindBy(xpath = "//picture//img[@alt='Image']")
    private WebElement productImage;

    @FindBy(xpath = "//a[contains(@href, '/ratings-reviews-details-page')]")
    private WebElement productRatingSection;

    @FindBy(xpath = "//a[@title='Cart']//span[normalize-space() and not(text()='Cart')]")
    private WebElement cartItemCountBadge;

    @FindBy(xpath = "//a[@title='Cart']")
    private WebElement cartIconLink;

    @FindBy (xpath = "//div[contains(@style,'height: 44px') and contains(@style,'width: 44px') and contains(@style,'z-index: 2') and contains(@style,'rgb(214, 214, 214)')]")
    private WebElement cartButton;


    public String getProductTitleHeader() { return productTitleHeader.getText(); }

    public boolean isProductNameVisible() {
        waitForVisibility(productTitleHeader);
        boolean isVisible = productTitleHeader.isDisplayed();
        if(isVisible) {
            log.info("Verified product name is visible. Product details page has loaded. Product name: {}", productTitleHeader.getText());
        } else {
            log.error("Product name is not visible");
        }
        return isVisible;
    }

    public boolean isProductPriceVisible() {
        waitForVisibility(productPriceLabel);
        boolean isVisible = productPriceLabel.isDisplayed();
        if(isVisible) {
            log.info("Verified product price is visible. Product price: {}", productPriceLabel);
        } else {
            log.error("Product price is not visible");
        }
        return isVisible;
    }

    public boolean isProductImageVisible() {
        waitForVisibility(productImage);
        boolean displayed = productImage.isDisplayed();
        String src = productImage.getAttribute("src");
        boolean imageLoaded = src != null && !src.isBlank();
        log.info("Verifying product image visibility on Product Detail Page");
        log.info("Image displayed: {}", displayed);
        log.info("Image source present: {}", imageLoaded);
        return displayed && imageLoaded;
    }

    public boolean isProductRatingsVisible() {
        waitForVisibility(productRatingSection);
        boolean isVisible = productRatingSection.isDisplayed();
        log.info("Verifying product rating visibility on Product Detail Page");
        log.info("Product rating section displayed: {}", isVisible);
        return isVisible;
    }

    public void addToCart() {
        waitForClickability(cartButton);
        clickOn(cartButton);
        log.info("Clicked Add to Cart button");
    }

    public boolean verifyIfCartIconHasProduct() {
        waitForVisibility(cartItemCountBadge);
        String countText = cartItemCountBadge.getText().trim();
        log.info("Cart badge count displayed: {}", cartItemCountBadge);
        try {
            int itemCount = Integer.parseInt(countText);
            boolean isAdded = itemCount > 0;
            if (isAdded) {
                log.info("Product successfully added to cart. Item count: {}", itemCount);
            } else {
                log.error("Cart count is zero");
            }
            return isAdded;
        } catch (NumberFormatException e) {
            log.error("Unable to parse cart count: {}", countText);
            return false;
        }
    }

    public CartPage goToCartPage() {
        waitForClickability(cartIconLink);
        log.info("Clicking cart icon");
        clickOn(cartIconLink);
        log.info("Successfully navigated to cart page");
        return new CartPage();
    }

    public ProductDetailsPage() {
        PageFactory.initElements(getDriver(), this);
    }
}
