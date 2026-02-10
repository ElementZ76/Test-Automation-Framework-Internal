package com.automation.pages;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.automation.base.TestBase;

public class CheckoutOverviewPage extends TestBase {
	@FindBy(className = "title")
	WebElement pageTitle;
	
	@FindBy(className = "inventory_item_name")
	List<WebElement> itemNames;
	
	@FindBy(className = "cart_item")
	List<WebElement> orderItems;
	
	@FindBy(id = "finish")
	WebElement finishButton;
	
	public CheckoutOverviewPage() {
		PageFactory.initElements(driver, this);
	}
	
	/**
	 * verify if we reached checkout overview page
	 * @return true if reached and false if not reached
	 */
	public boolean isOnOverviewPage() {
		try {
			waitForVisibility(pageTitle);
			return pageTitle.isDisplayed() && pageTitle.getText().equalsIgnoreCase("Checkout: Overview");
		} catch (Exception e) {
			log.error("Did not reach checkout overview page.");
			return false;
		}
	}
	
	/**
	 * 
	 * @return order item names
	 */
	public List<String> getOrderItemNames() {
		return itemNames.stream().map(WebElement::getText).collect(Collectors.toList());
	}
	
	/**
	 * @return number of items in cart
	 */
	public int getOrderItemCount() {
		return orderItems.size();
	}
	
	/**
	 * verify order item summary
	 * @param expectedProducts - expected products from testdata
	 * @return true or false if matched or unmatched
	 */
	public boolean verifyOrderItemSummary(List<String> expectedProducts) {
		List<String> actualProducts = getOrderItemNames();
		boolean matches = actualProducts.containsAll(expectedProducts) && actualProducts.size() == expectedProducts.size();
		if(matches) {
			log.info("Order summary verified");
		} else {
			log.error("Mismatch - Expected = {} and Actual = {}", expectedProducts.size(), actualProducts.size());
		}
		return matches;
	}
}
