package com.automation.pages;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.automation.base.TestBase;

public class productListPage extends TestBase {
	
	cartPage cart;
	@FindBy(className = "title")
	private WebElement pageTitle;
	    
	@FindBy(className = "shopping_cart_link")
	private WebElement shoppingCartIcon;
	    
	@FindBy(className = "shopping_cart_badge")
	private WebElement cartBadge;
	
	
	public productListPage() {
		PageFactory.initElements(driver, this);
	}
	
	// add 1  product to card
	private String addProductToCart(String productName) {
		return "add-to-cart-" + productName.toLowerCase()
		.replace(" ", "-")
        .replace(".", "")
        .replace("(", "")
        .replace(")", "");
	}
	
	// add multiple products to cart
	public cartPage addMultipleProductsToCart(List<String> productNames) {	
		productNames.forEach(this::addProductToCart);
		log.info("Adding {} products to cart", productNames.size());
		return new cartPage();
	}
	
	// click shopping cart btn
	public void clickShoppingCart() {
		waitForClickability(shoppingCartIcon);
		shoppingCartIcon.click();
		log.info("Clicked shopping cart icon");
	}
	
	// verify if we are on PLP
	public boolean isOnPLP() {
		boolean isDisplayed = pageTitle.isDisplayed() && pageTitle.getText().equals("Products");
		log.debug("On PLP {}", isDisplayed);
		return isDisplayed;
	}
	
	// verify if all products have been added to the cart
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