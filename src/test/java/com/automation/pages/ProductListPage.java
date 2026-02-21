package com.automation.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.automation.base.TestBase;

public class ProductListPage extends TestBase {
	
	CartPage cartPage;
	@FindBy(className = "title")
	WebElement pageTitle;
	    
	@FindBy(className = "shopping_cart_link")
	WebElement shoppingCartIcon;
	    
	@FindBy(className = "shopping_cart_badge")
	WebElement cartBadge;
	
	
	public ProductListPage() {
		PageFactory.initElements(getDriver(), this);
	}
	
	/**
	 * Add 1 product to cart using dynamic xpath
	 * @param productName
	 * @return ProductListPage
	 */
	
	public ProductListPage addProductToCart(String productName) {
		try {
			String dynamicLocator = "add-to-cart-" + productName.toLowerCase()
			.replace(" ", "-")
			.replace(".", "")
			.replace("(", "")
			.replace(")", "");
			WebElement addButton = getDriver().findElement(By.id(dynamicLocator));
			JavascriptExecutor js = (JavascriptExecutor) getDriver();
			js.executeScript("arguments[0].click();", addButton); 
			log.info("Added '{}' to cart", productName);
		} catch (Exception e) {
			log.error("Failed to add '{}': {}", productName, e.getMessage());
			throw new RuntimeException("Product '" + productName + "' not found", e);
		}
		return this;
	}
	
	/**
	 * Adding multiple products to cart
	 * @param productNames list of product names
	 * @return CartPage after adding all products
	 */
	public CartPage addMultipleProductsToCart(List<String> productNames) {	
		productNames.forEach(this::addProductToCart);
		log.info("Adding {} products to cart", productNames.size());
		return new CartPage();
	}
	
	/**
	 * Navigate to cart
	 * @return CartPage
	 */
	public CartPage clickShoppingCart() {
		clickOn(shoppingCartIcon);
		log.info("Clicked shopping cart icon");
		return new CartPage();
	}
	
	/**
	 * Verify if we are on PLP
	 * @return if we are on PLP or not
	 */
	public boolean isOnPLP() {
		boolean isDisplayed = pageTitle.isDisplayed() && pageTitle.getText().equals("Products");
		log.debug("On PLP {}", isDisplayed);
		return isDisplayed;
	}
	
	/**
	 * @return get cart item count. return 0n if empty.
	 */
	
	public int getItemCount() {
		try {
			waitForVisibility(cartBadge);
			int count = Integer.parseInt(cartBadge.getText());
			log.info("Added {} products to cart", count);
			return count;
		} catch (Exception e) {
			log.error("Cart is empty");
			return 0;
		}
	}
	
	
}