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

public class CartPage extends BasePage {

    private final String PRODUCT_TITLE_XPATH =
            "//div[contains(normalize-space(),'%s')]";

    public boolean isProductPresentInCart(String expectedProduct) {
        String xpath = String.format(PRODUCT_TITLE_XPATH, expectedProduct);
        try {
            WebElement productElement = getDriver().findElement(By.xpath(xpath));
            String actualProduct = productElement.getText().trim();
            boolean matches = actualProduct.equalsIgnoreCase(expectedProduct);
            log.info("Cart product verification for '{}': {}", expectedProduct, matches);
            return matches;
        } catch (NoSuchElementException e) {
            log.error("Product '{}' not found in cart.", expectedProduct);
            return false;
        }
    }

    public void refreshCartPage() {
        getDriver().navigate().refresh();
    }

    public void checkNumberOfItemsInCart() {

    }

    public CartPage() {
        PageFactory.initElements(getDriver(), this);
    }
}
