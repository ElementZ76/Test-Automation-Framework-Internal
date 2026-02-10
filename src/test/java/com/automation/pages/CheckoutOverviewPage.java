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
	
	@FindBy(className = "summary_subtotal_label")
	WebElement subTotal;
	
	@FindBy(className = "summary_tax_label")
	WebElement taxLabel;
	
	@FindBy(className = "summary_total_label")
	WebElement totalLabel;
	
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
	
	/**
	 * @return sub total
	 */
	public double getSubTotal() {
		String subTotalText = subTotal.getText();
		return extractPrice(subTotalText);
	}
	
	/**
	 * @return tax
	 */
	public double getTax() {
		String taxText = taxLabel.getText();
		return extractPrice(taxText);
	}
	
	/**
	 * @return total 
	 */
	public double getTotal() {
		String totalText = totalLabel.getText();
		return extractPrice(totalText);
	}
	
	/** @return true if subtotal+tax = total */
	public boolean validatePriceCalculation() {
		double subtotal = getSubTotal();
		double tax = getTax();
		double total = getTotal();
		
		double calculated = Math.round((subtotal + tax) * 100.0) / 100.0;
		double actual = Math.round(total * 100.0) / 100.0;
		
		boolean valid = Math.abs(calculated - actual) < 0.01;
		
		if (valid) {
			log.info("Price calculation valid: ${} + ${} = ${}", subtotal, tax, total);
		} else {
			log.error("Price error - Expected: ${}, Actual: ${}", calculated, actual);
		}
		return valid;
	}
	
	/** @param text Price text
	 *  @return Numeric price value 
	 */
	private double extractPrice(String text) {
		try {
			return Double.parseDouble(text.replaceAll("[^0-9.]", ""));
		} catch (NumberFormatException e) {
			log.error("Failed to extract price from: {}", text);
			return 0.0;
		}
	}
}
