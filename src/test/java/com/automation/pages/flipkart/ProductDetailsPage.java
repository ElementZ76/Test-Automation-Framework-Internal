package com.automation.pages.flipkart;

import com.automation.pages.BasePage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import static com.automation.driver.DriverManager.*;

import java.util.Objects;

import static com.automation.driver.DriverManager.getDriver;

public class ProductDetailsPage extends BasePage{
    private static final Logger log = LogManager.getLogger(ProductDetailsPage.class);

    @FindBy(tagName = "h1")
    private WebElement productTitle;

    @FindBy(xpath = "//h1/following::div[starts-with(normalize-space(),'₹')][1]")
    private WebElement productPriceLabel;

    @FindBy(xpath = "//picture//img[@alt='Image']")
    private WebElement productImage;

    @FindBy(xpath = "//svg[contains(@viewBox,'12 12')]/parent::div")
    private WebElement productRatingSection;

    @FindBy(xpath = "//div[.//clipPath[contains(@id,'AddToCart')]]")
    private WebElement addToCartButton;

    @FindBy(xpath = "//a[@title='Cart']//span[normalize-space() and not(text()='Cart')]")
    private WebElement cartItemCountBadge;

    public boolean isProductDetailsPageLoaded() {
        String currentUrl = Objects.requireNonNull(getDriver().getCurrentUrl());
        boolean loaded = currentUrl.contains("a/p/itm");
        if (loaded) {
            log.info("Product details page loaded successfully. URL: {}", currentUrl);
        } else {
            log.error("Search Results did not load correctly. Current URL: {}", currentUrl);
        }
        return loaded;
    }

    public boolean isProductNameVisible() {
        waitForVisibility(productTitle);
        boolean isVisible = productTitle.isDisplayed();
        if(isVisible) {
            log.info("Verified product name is visible. Product name: {}", productTitle.getText());
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
        waitForClickability(addToCartButton);
        addToCartButton.click();
        log.info("Clicked on add to cart button");
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

    public ProductDetailsPage() {
        PageFactory.initElements(getDriver(), this);
    }
}
