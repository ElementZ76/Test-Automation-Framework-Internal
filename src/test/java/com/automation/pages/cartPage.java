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
	
	@FindBy(className = "checkout")
	WebElement checkoutButtotn;
	
	@FindBy (className = "cart_item")
	private List<WebElement> cartItem;

	public CartPage() {
		PageFactory.initElements(driver, this);
	}
	
	/** @return CheckoutInfoPage**/
	public CheckoutInfoPage proceedToCheckout() {
		clickOn(checkoutButtotn);
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
