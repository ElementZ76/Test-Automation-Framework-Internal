package com.automation.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.automation.base.TestBase;

public class ProductListPage extends TestBase {
	
	CartPage cartPage;
	@FindBy(className = "title")
	private WebElement pageTitle;
	    
	@FindBy(className = "shopping_cart_link")
	private WebElement shoppingCartIcon;
	    
	@FindBy(className = "shopping_cart_badge")
	private WebElement cartBadge;
	
	
	public ProductListPage() {
		PageFactory.initElements(driver, this);
	}
	
	/**
	 * Add 1 product to cart using dynamic xpath
	 * @param productName
	 * @return ProductListPage
	 */
	
	private ProductListPage addProductToCart(String productName) {
		String xpath = String.format(
		        "//div[@class='inventory_item'][.//div[@class='inventory_item_name' and text()='%s']]" +
		        "//button[contains(@id, 'add-to-cart')]", 
		        productName);
		WebElement addButton = driver.findElement(By.xpath(xpath));
		clickOn(addButton);  
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
		waitForClickability(shoppingCartIcon);
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
			int count = Integer.parseInt(cartBadge.getText());
			log.info("Added {} products to cart", count);
			return count;
		} catch (Exception e) {
			log.error("Cart is empty");
			return 0;
		}
	}
	
	
}