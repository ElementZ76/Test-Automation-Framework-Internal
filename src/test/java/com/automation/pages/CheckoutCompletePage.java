package com.automation.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.automation.base.TestBase;

public class CheckoutCompletePage extends TestBase {
	@FindBy(className = "title")
	WebElement pageTitle;
	
	@FindBy(className = "complete-header")
	WebElement completeHeader;
	
	@FindBy(className = "complete-text")
	WebElement completeText;
	
	@FindBy(id = "back-to-products")
	WebElement backToProductsButton;
	
	
	public CheckoutCompletePage() {
		PageFactory.initElements(driver, this);
	}
	/**
	 * check if we are on checkout complete page
	 * @return true if we are on checkout complete page and false if we arent
	 */
	public boolean isOnCheckoutCompletePage() {
		try {
			waitForVisibility(pageTitle);
			return pageTitle.isDisplayed() && pageTitle.getText().equalsIgnoreCase("Checkout: Complete!");
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 
	 * @return confirmation header
	 */
	public String getConfirmationMessage() {
		waitForVisibility(completeText);
		return completeHeader.getText();
	}
	
	/**
	 * check if message matches actual message
	 * @param expectedMessage
	 * @return true if message matches and false if it doesnt match
	 */
	public boolean verifyConfirmationmessage(String expectedMessage) {
		String actualMessage = getConfirmationMessage();
		boolean matches = actualMessage.equalsIgnoreCase(expectedMessage);
		
		if(!matches) {
			log.error("Message mismatcH. Expected = {}. Actual = {}", expectedMessage, actualMessage);
		}
		return matches;
	}
	
	/** @return ProductListPage */
	public ProductListPage backToProducts() {
		clickOn(backToProductsButton);
		log.info("Back to products");
		return new ProductListPage();
	}
}
