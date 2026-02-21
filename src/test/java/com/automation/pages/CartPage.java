package com.automation.pages;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.automation.base.TestBase;

public class CartPage extends TestBase {
	
	@FindBy(className = "title")
	WebElement pageTitle;
	
	@FindBy(id = "checkout")
	WebElement checkoutButton;
	
	@FindBy (className = "inventory_item_name")
	private List<WebElement> cartItem;

	public CartPage() {
		PageFactory.initElements(getDriver(), this);
	}
	
	public boolean isOnCartPage() {
		try {
			waitForVisibility(pageTitle);
			return pageTitle.isDisplayed() && 
			       pageTitle.getText().equalsIgnoreCase("Your Cart");
		} catch (Exception e) {
			return false;
		}
	}
	
	/** @return CheckoutInfoPage**/
	public CheckoutInfoPage proceedToCheckout() {
		clickOn(checkoutButton);
		log.info("Proceeding to checkout");
		return new CheckoutInfoPage();
	}
	
	/** @return Names of all products in cart */
	public List<String> getProductsInCart() {
		return cartItem.stream().map(WebElement::getText)
				.collect(Collectors.toList());
	}
	
	/** Verify cart contains expected products
	 * @param expectedProducts List of product names
	 * @reutrn true if all the products are found
	 */
	public boolean verifyCartHasProducts(List<String> expectedProducts) {
		List<String> actualProducts = getProductsInCart();
		boolean allPresent = actualProducts.containsAll(expectedProducts);
		if(allPresent) {
			log.info("All products present in cart. Passed");
		} else {
			log.error("Failed cart verification");
		}
		return allPresent;
	}
	
}
